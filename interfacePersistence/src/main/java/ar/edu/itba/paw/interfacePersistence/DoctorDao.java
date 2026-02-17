package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.*;


import java.util.List;
import java.util.Optional;

public interface DoctorDao {

    Doctor create( String name, String lastName, String email, String password, String phone, String language, List<Specialty> specialties, List<Coverage> coverages);
    Optional<Doctor> getById(final long id);


    Optional<Doctor> getByEmail(String email);

    List<Doctor> getWithFilters(long specialtyId, long coverageId, List<Integer> weekdays, String keyword, String orderBy, String direction, int page, int pageSize);

    int countWithFilters(long specialtyId, long coverageId, List<Integer> weekdays, String keyword, String orderBy, String direction);

    Optional<Doctor> getByVerificationToken(String token);

    Optional<Doctor> getByResetToken(String token);

    long countAll();

    List<Doctor> search(String keyword, int results);

}
