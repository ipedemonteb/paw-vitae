package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.Doctor;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    Doctor create(String name, String email, String password, String phone, List<String> specialty);
    Optional<Doctor> findById(final long id);

    Optional<Doctor> findByEmail(String email);
}
