package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Patient;

import java.util.Optional;

public interface PatientService {

    Optional<Patient> getById(long id);

    Patient create(String name, String lastName, String email, String password, String phone, String language, long coverageId);

    Optional<Patient> getByEmail(String email);

    void updatePatient(Patient patient, String name, String lastName, String phone, long coverageId);

    String getAllPatientsDisplayCount();

}
