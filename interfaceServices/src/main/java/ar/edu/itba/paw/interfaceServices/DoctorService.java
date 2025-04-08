package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Specialty;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    Doctor create(String name, String lastName , String email, String password, String phone, List<String> specialties, List<String> coverages);

    Optional<Doctor> getById(final long id);

    List<Doctor> getBySpecialty(String specialty);
}
