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
        userDao.changeLanguage(id, language);
        LOGGER.info("Language changed to '{}' for user with id={}", language, id);
    }

    @Transactional
    @Override
    public void setVerificationToken(String email) {
        LOGGER.debug("Setting verification token for email: {}", email);
        User user = getByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        String token = UUID.randomUUID().toString();
        String verificationLink = BASE_URL + "/verify-confirmation?token=" + token;
        mailService.sendVerificationRegisterEmail(user, verificationLink);
        userDao.setVerificationToken(user.getId(), token, LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")).plusDays(30));
        LOGGER.info("Verification token set and email sent for user id={}", user.getId());
    }

    @Transactional
    @Override
    public void setVerificationStatus(User user, boolean status) {
        LOGGER.debug("Setting verification for user id={}", user.getId());
        userDao.setVerificationStatus(user.getId(), status);
        LOGGER.info("Verification status updated to {} for user id={}", status, user.getId());
    }

    @Transactional
    @Override
    public void setResetPasswordToken(String email) {
        LOGGER.debug("Setting reset password token for email: {}", email);
        User user = getByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        if (!user.isVerified()) {
            LOGGER.warn("User with email {} is not verified", email);
            return;
        }
        String token = UUID.randomUUID().toString();
        String resetPasswordLink = BASE_URL + "/change-password?token=" + token;
        userDao.setResetPasswordToken(user.getId(), token, LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")).plusHours(1));
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

    @Transactional(readOnly = true)
    @Override
    public Optional<? extends User> verifyValidationToken(String token) {
        LOGGER.debug("Verifying validation token");
        return checkToken(token,true);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean verifyRecoveryToken(String token) {
        LOGGER.debug("Verifying recovery token");
        return checkToken(token, false).isPresent();
    }

    @Transactional
    @Override
    public boolean changePassword(String token, String password) {
        Optional<? extends User> user = getByResetToken(token);
        if (user.isPresent()) {
                LOGGER.debug("Changing password for user id={}", user.get().getId());
                String newPassword = passwordEncoder.encode(password);
                userDao.changePassword(user.get().getId(), newPassword);
                userDao.removeResetToken(token);
                LOGGER.info("Password changed successfully for user id={}", user.get().getId());
                return true;
        }
            LOGGER.warn("User not found for valid token");
            return false;
    }

    @Override
    public Long getImageId(User user) {
        if (user == null || user instanceof Patient) {
            return -1L;
        }
        LOGGER.debug("Getting image id for user id={}", user.getId());
        return ((Doctor) user).getImageId();
    }

    @Transactional
    @Override
    public long create(String name, String lastName, String email, String password, String phone, String language) {
        LOGGER.debug("Creating user with name: {}, lastName: {}, email: {}, phone: {}, language: {}", name, lastName, email, phone, language);
        return userDao.create(name, lastName, email, password, phone, language).longValue();
    }

    @Transactional
    @Override
    public void update(long id, String name, String lastName, String phone) {
        LOGGER.debug("Updating user with id: {}, name: {}, lastName: {}, phone: {}", id, name, lastName, phone);
        userDao.update(id, name, lastName, phone);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<? extends User> checkToken(String token,boolean isVerification) {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));

        Optional<Patient> patient = isVerification
                ? patientDao.getByVerificationToken(token)
                : patientDao.getByResetToken(token);
        if (patient.isPresent()) {
            if (userDao.tokenExpirationDate(token).isBefore(now)) {
                LOGGER.info("Token expired");
                setVerificationToken(patient.get().getEmail());
                return patient;
            }
            setVerificationStatus(patient.get(), true);
            userDao.removeVerificationToken(token);
            LOGGER.info("Verification token valid for patient id={}", patient.get().getId());
            patient.get().setVerified(true);
            return patient;
        }
        Optional<Doctor> doctor = isVerification
                ? doctorDao.getByVerificationToken(token)
                : doctorDao.getByResetToken(token);
        if (doctor.isPresent()) {
            if (userDao.tokenExpirationDate(token).isBefore(now)) {
                LOGGER.info("Token expired");
                setVerificationToken(doctor.get().getEmail());
                return doctor;
            }
            setVerificationStatus(doctor.get(), true);
            userDao.removeVerificationToken(token);
            LOGGER.info("Verification token valid for doctor id={}", doctor.get().getId());
            doctor.get().setVerified(true);
            return doctor;
        }
        LOGGER.warn("No user found with token");
        return Optional.empty();
    }
}

