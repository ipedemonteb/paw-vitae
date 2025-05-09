package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Patient;

import java.util.Optional;

public interface PatientService {

    Optional<Patient> getById(long id);

    Patient create(String name, String lastName, String email, String password, String phone, String language, String coverageId);

    Optional<Patient> getByEmail(String email);

    void updatePatient(Patient patient, String name, String lastName, String phone, Long coverageId);

    Optional<Patient> getByIdWithAppointments(long id);

    Optional<Patient> getByResetToken(String token);

    Optional<Patient> getByVerificationToken(String token);
}
