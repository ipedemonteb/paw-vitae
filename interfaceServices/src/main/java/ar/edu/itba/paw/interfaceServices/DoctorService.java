package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.*;

import java.util.List;
import java.util.Optional;

public interface DoctorService {
    Doctor create(String name, String lastName , String email, String password, String phone, String language, List<String> specialties, List<String> coverages, List<AvailabilitySlot> availabilitySlots);

    Optional<Doctor> getById(final long id);

    Page<Doctor> getBySpecialty(long specialtyId, int page, int pageSize);

    Page<Doctor> getBySpecialtyWithAppointments(long specialtyId, int page, int pageSize);

    Optional<Doctor> getByEmail(String email);

    void updateDoctor(long id, String name, String lastName, String phone, List<String> specialties, List<String> coverages, List<AvailabilitySlot> availabilitySlots);

    void updateDoctorAvailability(long id, List<AvailabilitySlot> availabilitySlots);

    Optional<Doctor> getByIdWithAppointments(long id);
}
