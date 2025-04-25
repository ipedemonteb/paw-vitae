package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.PatientDao;
import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.PatientService;
import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Coverage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

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
        return this.patientDao.create(name, lastName, email, passwordEncoder.encode(password), phone, language, cov);
    }

    @Override
    public Optional<Patient> getByEmail(String email) {
        return patientDao.getByEmail(email);
    }

    @Transactional
    @Override
    public void updatePatient(Patient currentPatient, String name, String lastName, String phone, Coverage coverage) {
        boolean hasChanged = !currentPatient.getName().equals(name)
                || !currentPatient.getLastName().equals(lastName)
                || !currentPatient.getPhone().equals(phone)
                || !currentPatient.getCoverage().getName().equals(coverage.getName());
        if (hasChanged) {
            patientDao.updatePatient(currentPatient.getId(), name, lastName, phone, coverage);
        }

    }

    @Override
    public Optional<Patient> getByIdWithAppointments(long id) {
        Optional<Patient> patient = patientDao.getById(id);
        patient.ifPresent(c -> c.setAppointments(as.getByPatientId(id).orElse(Collections.emptyList())));
        return patient;
    }
}
