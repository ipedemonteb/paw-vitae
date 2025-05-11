package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.PatientDao;
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
    private final CoverageService coverageService;
    private final UserService userService;
    Logger LOGGER = LoggerFactory.getLogger(PatientServiceImpl.class);

    @Autowired
    public PatientServiceImpl(PatientDao patientDao, PasswordEncoder passwordEncoder, CoverageService coverageService, UserService userService) {
        this.patientDao = patientDao;
        this.passwordEncoder = passwordEncoder;
        this.coverageService = coverageService;
        this.userService = userService;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Patient> getById(long id) {
        LOGGER.debug("Getting patient with id {}", id);
        Optional<Patient> patient = patientDao.getById(id);
        if (patient.isEmpty()) {
            LOGGER.warn("No patient found with id {}", id);
        }
        return patient;
    }

    @Transactional
    @Override
    public Patient create(String name, String lastName, String email, String password, String phone, String language, long coverageId) {
        LOGGER.debug("Creating patient with name: {}, lastName: {}, email: {}, phone: {}, language: {}, coverageId: {}", name, lastName, email, phone, language, coverageId);
        Coverage coverage = coverageService.findById(coverageId).orElse(null); //TODO coverageNotFoundException?
        long id = userService.create(name, lastName, email, passwordEncoder.encode(password), phone, language);
        Patient patient = this.patientDao.create(id, name, lastName, email, passwordEncoder.encode(password), phone, language, coverage);
        LOGGER.info("Patient created successfully: id={}, email={}", patient.getId(), patient.getEmail());
        return patient;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Patient> getByEmail(String email) {
        LOGGER.debug("Getting patient with email {}", email);
        return patientDao.getByEmail(email);
    }

    @Transactional
    @Override
    public void updatePatient(Patient patient, String name, String lastName, String phone, long coverageId) {
        LOGGER.debug("Updating patient with id {}: name={}, lastName={}, phone={}, coverageId={}", patient.getId(), name, lastName, phone, coverageId);
        boolean hasChangedPatient = patient.getCoverage().getId() != coverageId;
        boolean hasChangedUser = !patient.getName().equals(name) || !patient.getLastName().equals(lastName) || !patient.getPhone().equals(phone);
        if (hasChangedPatient) {
            patientDao.updatePatient(patient.getId(), coverageId);
            LOGGER.info("Patient updated successfully: id={}", patient.getId());
        }
        if (hasChangedUser) {
            userService.update(patient.getId(), name, lastName, phone);
            LOGGER.info("User updated successfully: id={}", patient.getId());
        }
    }
}
