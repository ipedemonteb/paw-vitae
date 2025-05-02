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

    Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);
    @Value("${app.base-url}")
    private String BASE_URL;
    private PasswordEncoder passwordEncoder;
    private UserDao userDao;
    private MailService ms;
    private PatientService ps;
    private DoctorService ds;

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
        Optional<Patient> patient = ps.getByEmail(email);
        return patient.isPresent() ? patient : ds.getByEmail(email);
    }

    public String getLanguageById(long id) {
        return userDao.getLanguageById(id);
    }

    @Transactional
    public void changeLanguage(long id, String language) {
        userDao.changeLanguage(id, language);
        LOGGER.info("Language changed successfully for user with id: {}", id);
    }

    @Transactional
    @Override
    public void setVerificationToken(User user) {
        String token = UUID.randomUUID().toString();
        String verificationLink = BASE_URL + "/verify-confirmation?token=" + token;
        ms.sendVerificationRegisterEmail(user, verificationLink);
        userDao.setVerificationToken(user.getId(), token);
    }

    @Transactional
    @Override
    public void setVerificationStatus(User user, boolean status) {
        userDao.setVerificationStatus(user.getId(), status);
    }

    @Transactional
    @Override
    public void setResetPasswordToken(User user) {
        String token = UUID.randomUUID().toString();
        String resetPasswordLink = BASE_URL + "/change-password?token=" + token;
        ms.sendRecoverPasswordEmail(user, resetPasswordLink);
        userDao.setResetPasswordToken(user.getId(), token);
    }

    @Transactional
    @Override
    public Optional<? extends User> getByResetToken(String token) {
        Optional<Patient> patient = ps.getByResetToken(token);
        if (patient.isPresent()) {
            return patient;
        } else {
            return ds.getByResetToken(token);
        }
    }

    @Transactional
    @Override
    public Optional<? extends User> verifyValidationToken(String token) {
        Optional<Patient> patient = ps.getByVerificationToken(token);
        if (patient.isPresent()) {
            setVerificationStatus(patient.get(), true);
            userDao.removeVerificationToken(token);
            return patient;
        } else {
            Optional<Doctor> doctor = ds.getByVerificationToken(token);
            if (doctor.isPresent()) {
                setVerificationStatus(doctor.get(), true);
                userDao.removeVerificationToken(token);
                return doctor;
            }
        }
        return Optional.empty();
    }

    @Transactional
    @Override
    public boolean verifyRecoveryToken(String token) {
        Optional<Patient> patient = ps.getByResetToken(token);
        if (patient.isPresent()) {
            return true;
        } else {
            Optional<Doctor> doctor = ds.getByResetToken(token);
            return doctor.isPresent();
        }
    }

    @Transactional
    @Override
    public boolean changePassword(String token, String password) {
        if(verifyRecoveryToken(token)){
            Optional<? extends User> user = getByResetToken(token);
            if (user.isPresent()) {
                String newPassword = passwordEncoder.encode(password);
                userDao.changePassword(user.get().getId(), newPassword);
                userDao.removeResetToken(token);
                return true;
            }
            return false;
        }

        return false;
    }

}
