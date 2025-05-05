package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Coverage;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PatientDao {

    Optional<Patient> getById(long id);

    Patient create(String name, String lastName, String email, String password, String phone, String language, Coverage coverage);

    Optional<Patient> getByEmail(String email);

    void updatePatient(long id, String name, String lastName, String phone, Coverage coverage);

    List<Patient> getByIds(Set<Long> ids);

    String getLanguage(long id);

    Optional<Patient> getByVerificationToken(String token);

    void changeLanguage(long id, String language);

    Optional<Patient> getByResetToken(String token);
}
