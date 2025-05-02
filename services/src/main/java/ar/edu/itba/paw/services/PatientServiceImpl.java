package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.PatientDao;
import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.PatientService;
import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    Logger LOGGER = LoggerFactory.getLogger(PatientServiceImpl.class);

    private final PatientDao patientDao;

    private final PasswordEncoder passwordEncoder;

    private final CoverageService cs;

    private final AppointmentService as;

    @Autowired
    public PatientServiceImpl(PatientDao patientDao, PasswordEncoder passwordEncoder, CoverageService cs, AppointmentService as) {
        this.patientDao = patientDao;
        this.passwordEncoder = passwordEncoder;
        this.cs = cs;
        this.as = as;
    }

    @Override
    public Optional<Patient> getById(long id) {
        return patientDao.getById(id);
    }

    @Transactional
    @Override
    public Patient create(String name, String lastName, String email, String password, String phone, String language, String coverage) {
        Coverage cov = cs.findById(Long.parseLong(coverage)).orElse(null);
        Patient patient = this.patientDao.create(name, lastName, email, passwordEncoder.encode(password), phone, language, cov);
        LOGGER.debug("Patient created successfully: id={}, email={}", patient.getId(), patient.getEmail());
        return patient;

    }

    @Override
    public Optional<Patient> getByEmail(String email) {
        return patientDao.getByEmail(email);
    }

    @Transactional
    @Override
    public void updatePatient(long id, String name, String lastName, String phone, Coverage coverage) {
        Patient patient = getById(id).orElseThrow(UserNotFoundException::new);
        boolean hasChanged = !patient.getName().equals(name)
                || !patient.getLastName().equals(lastName)
                || !patient.getPhone().equals(phone)
                || !patient.getCoverage().getName().equals(coverage.getName());
        if (hasChanged) {
            patientDao.updatePatient(id, name, lastName, phone, coverage);
            LOGGER.debug("Patient updated successfully: id={}, email={}", patient.getId(), patient.getEmail());
        }

    }

    @Override
    public Optional<Patient> getByIdWithAppointments(long id) {
        Optional<Patient> patient = patientDao.getById(id);
        patient.ifPresent(c -> c.setAppointments(as.getByPatientId(id)));
        return patient;
    }
}
