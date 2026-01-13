package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.interfacePersistence.PatientDao;
import ar.edu.itba.paw.interfacePersistence.UserDao;
import ar.edu.itba.paw.interfaceServices.MailService;
import ar.edu.itba.paw.interfaceServices.UserService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;
    private final MailService mailService;
    private final PatientDao patientDao;
    private final DoctorDao doctorDao;
    @Value("${app.base-url}")
    private String BASE_URL;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserDao userDao, MailService mailService, PatientDao patientDao, DoctorDao doctorDao) {
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
        this.mailService = mailService;
        this.patientDao = patientDao;
        this.doctorDao = doctorDao;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<? extends User> getByEmail(String email) {
        LOGGER.debug("Fetching user by email: {}", email);
        Optional<Patient> patient = patientDao.getByEmail(email);
        return patient.isPresent() ? patient : doctorDao.getByEmail(email);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<? extends User> getById(long id) {
        LOGGER.debug("Fetching user by id: {}", id);
        Optional<Patient> patient = patientDao.getById(id);
        return patient.isPresent() ? patient : doctorDao.getById(id);
    }

    @Transactional
    public void changeLanguage(long id, String language) {
        User user = getById(id).orElseThrow(UserNotFoundException::new);
        user.setLanguage(language);
        LOGGER.info("Language changed to '{}' for user with id={}", language, id);
    }

    @Transactional
    @Override
    public Optional<? extends User> getByVerificationToken(String token){
        LOGGER.debug("Retrieving user by verification token");
        Optional<Patient> patient = patientDao.getByVerificationToken(token);
        return patient.isPresent() ? patient : doctorDao.getByVerificationToken(token);
    }
    @Transactional
    @Override
    public void setVerificationToken(String email) {
        LOGGER.debug("Setting verification token for email: {}", email);
        User user = getByEmail(email).orElseThrow(UserNotFoundException::new);
        String token = UUID.randomUUID().toString();
        String verificationLink = BASE_URL + "/verify?token=" + token + "&email=" + email;
        mailService.sendVerificationRegisterEmail(user, verificationLink);
        user.setVerificationToken(token);
        user.setTokenExpiration(LocalDateTime.now(ZoneId.systemDefault()).plusDays(30));
        LOGGER.info("Verification token set and email sent for user id={}", user.getId());
    }

    @Transactional
    @Override
    public void setVerificationStatus(User user, boolean status) {
        LOGGER.debug("Setting verification for user id={}", user.getId());
        user.setVerified(status);
        LOGGER.info("Verification status updated to {} for user id={}", status, user.getId());
    }

    @Transactional
    @Override
    public void setResetPasswordToken(String email) {
        LOGGER.debug("Setting reset password token for email: {}", email);
        Optional<? extends User> optionalUser = getByEmail(email);
        if (optionalUser.isEmpty()) {
            LOGGER.warn("User with email {} not found", email);
            return;
        }
        User user =optionalUser.get();
        if (!user.isVerified()) {
            LOGGER.warn("User with email {} is not verified", email);
            return;
        }
        String token = UUID.randomUUID().toString();
        String resetPasswordLink = BASE_URL + "/change-password?token=" + token + "&email=" + email;
        user.setResetPasswordToken(token);
        user.setTokenExpiration(LocalDateTime.now(ZoneId.systemDefault()).plusHours(1));
        mailService.sendRecoverPasswordEmail(user, resetPasswordLink);
        LOGGER.info("Password reset token set and email sent for user id={}", user.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<? extends User> getByResetToken(String token) {
        LOGGER.debug("Retrieving user by reset token");
        Optional<Patient> patient = patientDao.getByResetToken(token);
        return patient.isPresent() ? patient : doctorDao.getByResetToken(token);
    }

    @Transactional
    @Override
    public Optional<? extends User> verifyValidationToken(String token) {
        LOGGER.debug("Verifying validation token");
        return checkToken(token,true);
    }

    @Transactional
    @Override
    public boolean verifyRecoveryToken(String token) {
        LOGGER.debug("Verifying recovery token");
        return checkToken(token, false).isPresent();
    }

    @Transactional
    @Override
    public void changePassword(long userId,String password) {
        User user = getById(userId).orElseThrow(UserNotFoundException::new);
        String encodedPassword = passwordEncoder.encode(password);
        user.setPassword(encodedPassword);
        user.setResetPasswordToken(null);
        user.setTokenExpiration(null);
        LOGGER.info("Password changed for user id={}", userId);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Long> getImageId(User user) {
        if (user == null || user instanceof Patient) {
            return Optional.empty();
        }
        LOGGER.debug("Getting image id for user id={}", user.getId());
        return Optional.of(((Doctor) user).getImageId());
    }

    @Transactional
    @Override
    public Optional<? extends User> checkToken(String token,boolean isVerification) {
        LocalDateTime now = LocalDateTime.now(ZoneId.systemDefault());

        Optional<Patient> patient = isVerification
                ? patientDao.getByVerificationToken(token)
                : patientDao.getByResetToken(token);
        if (patient.isPresent()) {
            if (userDao.tokenExpirationDate(token).isBefore(now)) {
                LOGGER.info("Token expired");
                if(isVerification) {
                    setVerificationToken(patient.get().getEmail());
                }
                else {
                    setResetPasswordToken(patient.get().getEmail());
                    return Optional.empty();
                }
                return Optional.empty();
            }
            if(isVerification){
                setVerificationStatus(patient.get(), true);
                userDao.removeVerificationToken(token);
                LOGGER.info("Verification token valid for patient id={}", patient.get().getId());
                patient.get().setVerified(true);
            }
            return patient;
        }
        Optional<Doctor> doctor = isVerification
                ? doctorDao.getByVerificationToken(token)
                : doctorDao.getByResetToken(token);
        if (doctor.isPresent()) {
            if (userDao.tokenExpirationDate(token).isBefore(now)) {
                LOGGER.info("Token expired");
                if(isVerification) {
                    setVerificationToken(doctor.get().getEmail());
                }
                else {
                    setResetPasswordToken(doctor.get().getEmail());
                    return Optional.empty();
                }
                return Optional.empty();
            }
            if(isVerification){
            setVerificationStatus(doctor.get(), true);
            userDao.removeVerificationToken(token);
            LOGGER.info("Verification token valid for doctor id={}", doctor.get().getId());
            doctor.get().setVerified(true);
            }
            return doctor;
        }
        LOGGER.warn("No user found with token");
        return Optional.empty();
    }
}

