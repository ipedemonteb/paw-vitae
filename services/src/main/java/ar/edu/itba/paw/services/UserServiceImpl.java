package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.PatientDao;
import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.interfaceServices.UserService;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private PatientDao patientDao;
    private DoctorDao doctorDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(PatientDao patientDao, DoctorDao doctorDao, PasswordEncoder passwordEncoder) {
        this.patientDao = patientDao;
        this.doctorDao = doctorDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<? extends User> getByEmail(String email) {
        Optional<Patient> patient = patientDao.getByEmail(email);
        return patient.isPresent() ? patient : doctorDao.getByEmail(email);
    }

    @Override
    public void changePassword(Long id, String password) {
        Optional<Patient> patient = patientDao.getById(id);
        String newPassword = passwordEncoder.encode(password);
        if (patient.isPresent()) {
            patientDao.changePassword(id, newPassword);
        } else {
            doctorDao.changePassword(id, newPassword);
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

    public void changeLanguage(long id, String language) {
        Optional<Patient> patient = patientDao.getById(id);
        if (patient.isPresent()) {
            patientDao.changeLanguage(id, language);
        } else {
            doctorDao.changeLanguage(id, language);
        }
    }
}
