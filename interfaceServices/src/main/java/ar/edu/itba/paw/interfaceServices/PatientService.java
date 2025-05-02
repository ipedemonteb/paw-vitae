package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Coverage;

import java.util.Optional;

public interface PatientService {

    Optional<Patient> getById(long id);

    Patient create(String name, String lastName, String email, String password, String phone, String language, String coverage);

    Optional<Patient> getByEmail(String email);

     void updatePatient(Patient patient, String name, String lastName, String phone, Coverage coverage);

     Optional<Patient> getByIdWithAppointments(long id);
}
