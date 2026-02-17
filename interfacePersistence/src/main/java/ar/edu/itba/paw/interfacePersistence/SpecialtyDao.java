package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.Specialty;

import java.util.List;
import java.util.Optional;

public interface SpecialtyDao {

    Optional<Specialty> getById(long id);

    List<Specialty> getByIds(List<Long> ids);

    List<Specialty> getByDoctorId(long id);

    List<Specialty> getAll();

}
