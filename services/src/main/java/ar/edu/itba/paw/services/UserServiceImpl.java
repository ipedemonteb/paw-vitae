package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.PatientDao;
import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.interfacePersistence.UserDao;
import ar.edu.itba.paw.interfaceServices.MailService;
import ar.edu.itba.paw.interfaceServices.UserService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;
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
    private PatientDao patientDao;
    private DoctorDao doctorDao;
    private PasswordEncoder passwordEncoder;
    private UserDao userDao;
    private MailService ms;
    @Autowired
    public UserServiceImpl(PatientDao patientDao, DoctorDao doctorDao, PasswordEncoder passwordEncoder, UserDao userDao,
                           MailService ms) {
        this.patientDao = patientDao;
        this.doctorDao = doctorDao;
        this.passwordEncoder = passwordEncoder;
        this.userDao = userDao;
        this.ms = ms;
    }

    @Override
    public Optional<? extends User> getByEmail(String email) {
        Optional<Patient> patient = patientDao.getByEmail(email);
        return patient.isPresent() ? patient : doctorDao.getByEmail(email);
    }
    @Transactional
    @Override
    public void changePassword(Long id, String password) {
        Optional<Patient> patient = patientDao.getById(id);
        String newPassword = passwordEncoder.encode(password);
        if (patient.isPresent()) {
            patientDao.changePassword(id, newPassword);
            LOGGER.debug("Password changed successfully for patient with id: {}", id);
        } else {
            doctorDao.changePassword(id, newPassword);
            LOGGER.debug("Password changed successfully for doctor with id: {}", id);
        }
    }

    public String getLanguageById(long id) {
        Optional<Patient> patient = patientDao.getById(id);
        if (patient.isPresent()) {
            return patientDao.getLanguage(id);
        } else {
            return doctorDao.getLanguage(id);
        }
    }
    @Transactional
    public void changeLanguage(long id, String language) {
        Optional<Patient> patient = patientDao.getById(id);
        if (patient.isPresent()) {
            patientDao.changeLanguage(id, language);
            LOGGER.debug("Language changed successfully for patient with id: {}", id);
        } else {
            doctorDao.changeLanguage(id, language);
            LOGGER.debug("Language changed successfully for doctor with id: {}", id);
        }
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
    public void setVerificationStatus(User user,boolean status) {
        userDao.setVerificationStatus(user.getId(), status);
    }
    @Transactional
    @Override
    public void setResetPasswordToken(User user) {
        String token = UUID.randomUUID().toString();
        String resetPasswordLink = BASE_URL + "/reset-password?token=" + token;
        ms.sendRecoverPasswordEmail(user, resetPasswordLink);
        userDao.setResetPasswordToken(user.getId(), token);
    }

    @Transactional
    @Override
    public Optional<? extends User> verifyValidationToken(String token) {
        Optional<Patient> patient = patientDao.getByVerificationToken(token);
        if (patient.isPresent()) {
            setVerificationStatus(patient.get(), true);
            userDao.removeVerificationToken(token);
            return patient;
        } else{
            Optional<Doctor> doctor = doctorDao.getByVerificationToken(token);
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
    public void removeVerificationToken(String token) {
        userDao.removeVerificationToken(token);
    }
}
