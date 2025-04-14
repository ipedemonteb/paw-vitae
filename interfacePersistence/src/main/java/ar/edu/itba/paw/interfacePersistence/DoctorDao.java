package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Specialty;

import java.util.List;
import java.util.Optional;

public interface DoctorDao {

    Doctor create(String name, String lastName , String email, String password, String phone, List<Specialty> specialty, List<Coverage> coverages);

    Optional<Doctor> getById(final long id);

    List<Doctor> getBySpecialty(String specialty);

    Optional<Doctor> getByEmail(String email);

    List<Doctor> getAll();

    void updateDoctor(long id, String name, String lastName, String email, String phone, List<Specialty> specialties, List<Coverage> coverages);
}
