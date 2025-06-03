package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.*;


import java.util.List;
import java.util.Optional;

public interface DoctorDao {

    Doctor create( String name, String lastName, String email, String password, String phone, String language, Long imageId, List<Specialty> specialties, List<Coverage> coverages);

    Optional<Doctor> getById(final long id);

    List<Doctor> getBySpecialty(long specialtyId, int page, int pageSize);

    int countBySpecialty(long specialtyId);

    void updateImage(long id, Long imageId);

    Optional<Doctor> getByEmail(String email);

    void UpdateDoctorRating(long id, long rating);

    void updateDoctor(long id, List<Long> specialties, List<Long> coverages);

    List<Doctor> getWithFilters(long specialtyId, long coverageId, List<Integer> weekdays, String orderBy, String direction, int page, int pageSize);

    int countWithFilters(long specialtyId, long coverageId, List<Integer> weekdays, String orderBy, String direction);

    Optional<Doctor> getByVerificationToken(String token);

    Optional<Doctor> getByResetToken(String token);

    int countAll();

    List<Doctor> search(String keyword, int results);

}
