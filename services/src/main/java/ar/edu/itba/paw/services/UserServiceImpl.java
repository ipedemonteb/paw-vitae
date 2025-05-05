package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.PatientDao;
import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.interfacePersistence.UserDao;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.MailService;
import ar.edu.itba.paw.interfaceServices.PatientService;
import ar.edu.itba.paw.interfaceServices.UserService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Value("${app.base-url}")
    private String BASE_URL;

    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;
    private final MailService ms;
    private final PatientService ps;
    private final DoctorService ds;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserDao userDao, MailService ms, PatientService ps, DoctorService ds) {
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
        this.ms = ms;
        this.ps = ps;
        this.ds = ds;
    }

    @Override
    public Optional<? extends User> getByEmail(String email) {
        LOGGER.debug("Fetching user by email: {}", email);
        Optional<Patient> patient = ps.getByEmail(email);
        return patient.isPresent() ? patient : ds.getByEmail(email);
    }

    public String getLanguageById(long id) {
        return userDao.getLanguageById(id);
    }

    @Transactional
    public void changeLanguage(long id, String language) {
        userDao.changeLanguage(id, language);
        LOGGER.info("Language changed to '{}' for user with id={}", language, id);
    }

    @Transactional
    @Override
    public void setVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        String verificationLink = BASE_URL + "/verify-confirmation?token=" + token;
        ms.sendVerificationRegisterEmail(user, verificationLink);
        userDao.setVerificationToken(user.getId(), token);
        LOGGER.info("Verification token set and email sent for user id={}", user.getId());
    }

    @Transactional
    @Override
    public void setVerificationStatus(User user, boolean status) {
        userDao.setVerificationStatus(user.getId(), status);
        LOGGER.info("Verification status updated to {} for user id={}", status, user.getId());
    }

    @Transactional
    @Override
    public void setResetPasswordToken(User user) {
        String token = UUID.randomUUID().toString();
        String resetPasswordLink = BASE_URL + "/change-password?token=" + token;
        ms.sendRecoverPasswordEmail(user, resetPasswordLink);
        userDao.setResetPasswordToken(user.getId(), token);
        LOGGER.info("Password reset token set and email sent for user id={}", user.getId());
    }

    @Transactional
    @Override
    public Optional<? extends User> getByResetToken(String token) {
        LOGGER.debug("Retrieving user by reset token");
        Optional<Patient> patient = ps.getByResetToken(token);
        return patient.isPresent() ? patient : ds.getByResetToken(token);
    }

    @Transactional
    @Override
    public Optional<? extends User> verifyValidationToken(String token) {
        Optional<Patient> patient = ps.getByVerificationToken(token);
        if (patient.isPresent()) {
            setVerificationStatus(patient.get(), true);
            userDao.removeVerificationToken(token);
            LOGGER.info("Verification token valid for patient id={}", patient.get().getId());
            return patient;
        }

        Optional<Doctor> doctor = ds.getByVerificationToken(token);
        if (doctor.isPresent()) {
            setVerificationStatus(doctor.get(), true);
            userDao.removeVerificationToken(token);
            LOGGER.info("Verification token valid for doctor id={}", doctor.get().getId());
            return doctor;
        }

        LOGGER.warn("Invalid verification token: {}", token);
        return Optional.empty();
    }

    @Transactional
    @Override
    public boolean verifyRecoveryToken(String token) {
        boolean valid = ps.getByResetToken(token).isPresent() || ds.getByResetToken(token).isPresent();
        if (!valid) {
            LOGGER.warn("Invalid recovery token: {}", token);
        }
        return valid;
    }

    @Transactional
    @Override
    public boolean changePassword(String token, String password) {
        if (verifyRecoveryToken(token)) {
            Optional<? extends User> user = getByResetToken(token);
            if (user.isPresent()) {
                String newPassword = passwordEncoder.encode(password);
                userDao.changePassword(user.get().getId(), newPassword);
                userDao.removeResetToken(token);
                LOGGER.info("Password changed successfully for user id={}", user.get().getId());
                return true;
            }
            LOGGER.warn("User not found for valid token");
            return false;
        }
        LOGGER.warn("Password change failed due to invalid token");
        return false;
    }

    @Override
    public Long getImageId(User user) {
        if (user == null || user instanceof Patient) {
            return -1L;
        }
        return ((Doctor) user).getImageId();
    }
}

