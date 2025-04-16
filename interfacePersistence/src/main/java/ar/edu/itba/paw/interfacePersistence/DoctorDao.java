package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.*;


import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DoctorDao {

    Doctor create(String name, String lastName , String email, String password, String phone, List<Specialty> specialty, List<Coverage> coverages, List<AvailabilitySlot> availabilitySlots);

    Optional<Doctor> getById(final long id);

    List<Doctor> getBySpecialty(String specialty);

    Optional<Doctor> getByEmail(String email);

    List<Doctor> getAll();
    List<Doctor> getByIds(Set<Long> ids);
    void updateDoctor(long id, String name, String lastName, String phone, List<Specialty> specialties, List<Coverage> coverages, List<AvailabilitySlot> availabilityList);

    void addAvailability(long doctorId, List<AvailabilitySlot> availabilityList);
    List<AvailabilitySlot> getAvailabilityByDoctorId(long doctorId);
    void updateDoctorAvailability(long id, List<AvailabilitySlot> availabilitySlots);

}
