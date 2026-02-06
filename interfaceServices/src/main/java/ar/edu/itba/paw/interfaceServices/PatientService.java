package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Patient;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface PatientService {

    Optional<Patient> getById(long id);

    Patient create(String name, String lastName, String email, String password, String phone, List<Locale> locales, long coverageId, long neighborhoodId);


    void updatePatient(Patient patient, String name, String lastName, String phone, Long coverageId);

    long getAllPatientsDisplayCount();
    void changePassword(long userId,String password);
    Optional<Patient> getByEmail(String email);

}
