package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.Doctor;

import java.util.Optional;

public interface DoctorDao {
    Doctor create(String name, String email, String password, String phone);
    Optional<Doctor> findById(final long id);

    Optional<Doctor> findByEmail(String email);
}
