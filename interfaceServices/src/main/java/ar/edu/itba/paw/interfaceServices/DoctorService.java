package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Doctor;

import java.sql.Array;
import java.util.List;
import java.util.Optional;

public interface DoctorService {
    Doctor create(String name, String lastName ,String email, String password, String phone, List<String> specialty,List<Coverage> coverages);

    Optional<Doctor> getById(final long id);

    Optional<Doctor> getByEmail(String email);
}
