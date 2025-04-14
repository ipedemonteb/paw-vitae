package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.*;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    Doctor create(String name, String lastName , String email, String password, String phone, List<String> specialties, List<String> coverages);

    Optional<Doctor> getById(final long id);

    List<Doctor> getBySpecialty(String specialty);

    Optional<Doctor> getByEmail(String email);

    List<Doctor> getAll();
    List<Doctor> getByAppointments(List<Appointment> appointments);
    void updateDoctor(long id, String name, String lastName, String email, String phone, List<Specialty> specialties, List<Coverage> coverages);

}
