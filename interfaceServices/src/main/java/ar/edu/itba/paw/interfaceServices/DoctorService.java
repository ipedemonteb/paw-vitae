package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.*;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    Doctor create(String name, String lastName , String email, String password, String phone, String language, List<String> specialties, List<String> coverages, List<AvailabilitySlot> availabilitySlots);

    Optional<Doctor> getById(final long id);

    List<Doctor> getBySpecialty(String specialty);

    Optional<Doctor> getByEmail(String email);

    List<Doctor> getAll();
    List<Doctor> getByAppointments(List<Appointment> appointments);
    void updateDoctor(long id, String name, String lastName, String phone, List<String> specialties, List<String> coverages, List<AvailabilitySlot> availabilitySlots);
    void updateDoctorAvailability(long id, List<AvailabilitySlot> availabilitySlots);
}
