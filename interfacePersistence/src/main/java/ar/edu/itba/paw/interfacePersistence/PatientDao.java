package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.Neighborhood;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Coverage;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface PatientDao {

    Optional<Patient> getById(long id);

    Patient create(String name, String lastName, String email, String password, String phone, String language, Coverage coverage, Neighborhood neighborhood);

    Optional<Patient> getByEmail(String email);

    Optional<Patient> getByVerificationToken(String token);

    Optional<Patient> getByResetToken(String token);

    int countAll();
}
