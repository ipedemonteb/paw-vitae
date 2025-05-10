package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.PatientDao;
import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.PatientService;
import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.interfaceServices.UserService;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Patient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    private final PatientDao patientDao;
    private final PasswordEncoder passwordEncoder;
    private final CoverageService cs;
    private final AppointmentService as;
    private final UserService us;
    Logger LOGGER = LoggerFactory.getLogger(PatientServiceImpl.class);

    @Autowired
    public PatientServiceImpl(PatientDao patientDao, PasswordEncoder passwordEncoder, CoverageService cs, AppointmentService as, UserService us) {
        this.patientDao = patientDao;
        this.passwordEncoder = passwordEncoder;
        this.cs = cs;
        this.as = as;
        this.us = us;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Patient> getById(long id) {
        Optional<Patient> patient = patientDao.getById(id);
        if (patient.isEmpty()) {
            LOGGER.warn("No patient found with id {}", id);
        }
        return patient;
    }

    @Transactional
    @Override
    public Patient create(String name, String lastName, String email, String password, String phone, String language, long coverageId) {
        Coverage coverage = cs.findById(coverageId).orElse(null); //TODO coverageNotFoundException?
        long id = us.create(name, lastName, email, passwordEncoder.encode(password), phone, language);
        Patient patient = this.patientDao.create(id, name, lastName, email, passwordEncoder.encode(password), phone, language, coverage);
        LOGGER.info("Patient created successfully: id={}, email={}", patient.getId(), patient.getEmail());
        return patient;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Patient> getByEmail(String email) {
        return patientDao.getByEmail(email);
    }

    @Transactional
    @Override
    public void updatePatient(Patient patient, String name, String lastName, String phone, long coverageId) { //TODO unify how these things are handled, doctoDao does something else entirely
        boolean hasChangedPatient = patient.getCoverage().getId() != coverageId;
        boolean hasChangedUser = !patient.getName().equals(name) || !patient.getLastName().equals(lastName) || !patient.getPhone().equals(phone);
        if (hasChangedPatient) {
            patientDao.updatePatient(patient.getId(), coverageId);
            LOGGER.info("Patient updated successfully: id={}", patient);
        }
        if (hasChangedUser) {
            us.update(patient.getId(), name, lastName, phone);
            LOGGER.info("User updated successfully: id={}", patient);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Patient> getByIdWithAppointments(long id) {
        Optional<Patient> patient = patientDao.getById(id);
        patient.ifPresent(c -> c.setAppointments(as.getByPatientId(id)));
        return patient;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Patient> getByResetToken(String token) {
        return patientDao.getByResetToken(token);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Patient> getByVerificationToken(String token) {
        return patientDao.getByVerificationToken(token);
    }
}
