package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface DoctorService {

    Doctor create(String name, String lastName, String email, String password, String phone, String language, MultipartFile image, List<String> specialties, List<String> coverages, List<AvailabilitySlot> availabilitySlots);

    Optional<Doctor> getById(final long id);

    Page<Doctor> getBySpecialty(long specialtyId, int page, int pageSize);

    Page<Doctor> getBySpecialtyWithAppointments(long specialtyId, int page, int pageSize);

    Optional<Doctor> getByEmail(String email);

    void updateDoctor(long id, String name, String lastName, String phone, List<String> specialties, List<String> coverages);

    void UpdateDoctorRating(long id, long rating);

    Optional<Doctor> getByIdWithAppointments(long id);

    Page<Doctor> getWithFilters(Long specialtyId, Long coverageId, List<Integer> weekdays, String orderBy, String direction, int page, int pageSize);

    Optional<Doctor> getByResetToken(String token);

    Optional<Doctor> getByVerificationToken(String token);
}
