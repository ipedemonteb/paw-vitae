package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.Specialty;

import java.util.List;
import java.util.Optional;

public interface SpecialtyDao {

    Optional<Specialty> getById(long id);
    Optional<List<Specialty>> getByIds(List<Long> ids);

    Optional<Specialty> getByName(String name);

    Optional<List<Specialty>> getAll();

}
