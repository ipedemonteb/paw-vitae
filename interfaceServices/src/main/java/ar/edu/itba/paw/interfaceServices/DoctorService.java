package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

public interface DoctorService {

    Doctor create(String name, String lastName, String email, String password, String phone, List<Locale> locales, List<Long> specialties, List<Long> coverages, List<DoctorOfficeForm> doctorOfficeForm);

    Optional<Doctor> getById(final long id);

    Optional<Doctor> getByIdWithAvailableOffices(final long id);

    Page<Doctor> getBySpecialty(long specialtyId, int page, int pageSize);

    Optional<Doctor> getByEmail(String email);

     void updateDoctor(Doctor doctor, String name, String lastName, String phone, List<Long> specialties, List<Long> coverages);

    void UpdateDoctorRating(long id, long rating);

    void setImage(long doctorId, long imageId);

    Optional<Images> getDoctorImage(long doctorId);

    Page<Doctor> getWithFilters(long specialtyId, long coverageId, List<Integer> weekdays, String keyword, String orderBy, String direction, int page, int pageSize);

    String getAllDoctorsDisplayCount();

    String search(String keyword, int results);

}
