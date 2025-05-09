package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.UserDao;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.MailService;
import ar.edu.itba.paw.interfaceServices.PatientService;
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
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    private final PasswordEncoder passwordEncoder;
    private final UserDao userDao;
    private final MailService ms;
    private final PatientService ps;
    private final DoctorService ds;
    @Value("${app.base-url}")
    private String BASE_URL;

    @Autowired
    public UserServiceImpl(PasswordEncoder passwordEncoder, UserDao userDao, MailService ms, PatientService ps, DoctorService ds) {
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
        this.ms = ms;
        this.ps = ps;
        this.ds = ds;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<? extends User> getByEmail(String email) { //TODO possibly implement isPatient method instead of this
        LOGGER.debug("Fetching user by email: {}", email);
        Optional<Patient> patient = ps.getByEmail(email);
        return patient.isPresent() ? patient : ds.getByEmail(email);
    }

    @Override
    public Optional<? extends User> getById(long id) {
        LOGGER.debug("Fetching user by id: {}", id);
        Optional<Patient> patient = ps.getById(id);
        return patient.isPresent() ? patient : ds.getById(id);
    }

    @Transactional
    public void changeLanguage(long id, String language) {
        userDao.changeLanguage(id, language);
        LOGGER.info("Language changed to '{}' for user with id={}", language, id);
    }

    @Transactional
    @Override
    public void setVerificationToken(String email) {
        User user = getByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
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
    public void setResetPasswordToken(String email) {
        User user = getByEmail(email).orElseThrow(() -> new UserNotFoundException("User not found with email: " + email));
        String token = UUID.randomUUID().toString();
        String resetPasswordLink = BASE_URL + "/change-password?token=" + token;
        ms.sendRecoverPasswordEmail(user, resetPasswordLink);
        userDao.setResetPasswordToken(user.getId(), token);
        LOGGER.info("Password reset token set and email sent for user id={}", user.getId());
    }

    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
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

//    @Override
//    public void update(long id, String name, String lastName, String phone, String password) {
//        if (name == null || lastName == null || phone == null || password == null) {
//            throw new IllegalArgumentException("Name, last name, phone, and password cannot be null");
//        }
//        String encodedPassword = passwordEncoder.encode(password);
//        userDao.update(id, name, lastName, phone, encodedPassword);
//        LOGGER.info("User updated successfully with id={}", id);
//    }


    @Override
    public void update(User user, String name, String lastName, String phone, List<String> specialties, List<String> coverages, MultipartFile image) {
        if (name == null || lastName == null || phone == null || specialties == null || coverages == null) {
            throw new IllegalArgumentException("Name, last name, phone, specialties, and coverages cannot be null");
        }
        List<Long> specialtyIds = specialties.stream()
                .map(Long::parseLong)
                .toList();
        List<Long> coverageIds = coverages.stream()
                .map(Long::parseLong)
                .toList();

        userDao.update(user.getId(), name, lastName, phone);
        ds.updateDoctor((Doctor) user, specialtyIds, coverageIds, image);
        LOGGER.info("User updated successfully with id={}", user.getId());
    }

    @Override
    public void update(User user, String name, String lastName, String phone, Long coverageId) {
        if (name == null || lastName == null || phone == null) {
            throw new IllegalArgumentException("Name, last name, and phone cannot be null");
        }
        userDao.update(user.getId(), name, lastName, phone);
        ps.updatePatient((Patient) user, coverageId);
        LOGGER.info("User updated successfully with id={}", user);
    }

    @Override
    public User create(String name, String lastName, String email, String password, String phone, String language, MultipartFile image, List<String> specialties, List<String> coverages, List<AvailabilitySlot> availabilitySlots) {
        if (name == null || lastName == null || email == null || password == null || phone == null || language == null) {
            throw new IllegalArgumentException("Name, last name, email, password, phone, and language cannot be null");
        }
        String encodedPassword = passwordEncoder.encode(password);
        List<Long> specialtyIds = specialties.stream()
                .map(Long::parseLong)
                .toList();
        List<Long> coverageIds = coverages.stream()
                .map(Long::parseLong)
                .toList();
        long id = userDao.create(name, lastName, email, encodedPassword, phone, language).longValue();
        Doctor doctor = ds.create(id, name, lastName, email, encodedPassword, phone, language, image, specialtyIds, coverageIds, availabilitySlots);
        LOGGER.info("Doctor created successfully with id={}", doctor.getId());
        return doctor;
    }

    @Override
    public User create(String name, String lastName, String email, String password, String phone, String language, String coverage) {
        if (name == null || lastName == null || email == null || password == null || phone == null || language == null) {
            throw new IllegalArgumentException("Name, last name, email, password, phone, and language cannot be null");
        }
        String encodedPassword = passwordEncoder.encode(password);
        long id = userDao.create(name, lastName, email, encodedPassword, phone, language).longValue();
        Patient patient = ps.create(id, name, lastName, email, encodedPassword, phone, language, Long.parseLong(coverage));
        LOGGER.info("Patient created successfully with id={}", patient.getId());
        return patient;
    }
}

