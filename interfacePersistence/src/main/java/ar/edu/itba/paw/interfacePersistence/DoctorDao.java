package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.*;


import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface DoctorDao {

    Doctor create(String name, String lastName, String email, String password, String phone, String language,long imageId,
                  List<Specialty> specialties, List<Coverage> coverages, List<AvailabilitySlot> availabilityList);

    Optional<Doctor> getById(final long id);

    List<Doctor> getBySpecialty(long specialtyId, int page, int pageSize);

    int countBySpecialty(long specialtyId);

    Optional<Doctor> getByEmail(String email);

    List<Doctor> getAll();

    void UpdateDoctorRating(long id, double rating);

    List<Doctor> getByIds(Set<Long> ids);

    void updateDoctor(long id, String name, String lastName, String phone, List<Specialty> specialties, List<Coverage> coverages, List<AvailabilitySlot> availabilityList);

    void addAvailability(long doctorId, List<AvailabilitySlot> availabilityList);

    List<AvailabilitySlot> getAvailabilityByDoctorId(long doctorId);

    void updateDoctorAvailability(long id, List<AvailabilitySlot> availabilitySlots);

    void changePassword(long id, String password);

    String getLanguage(long id);

    void changeLanguage(long id, String language);

    List<Doctor> getWithFilters(long specialtyId, long coverageId, List<Integer> weekdays, String orderBy, String direction, int page, int pageSize);

    int countWithFilters(long specialtyId, long coverageId, List<Integer> weekdays, String orderBy, String direction);
}
