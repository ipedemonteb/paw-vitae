package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Doctor;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    Doctor create(String name, String lastName ,String email, String password, String phone, String specialty,List<Coverage> coverages);

    Optional<Doctor> findById(final long id);

    List<Doctor> getBySpecialty(String specialty);
}
