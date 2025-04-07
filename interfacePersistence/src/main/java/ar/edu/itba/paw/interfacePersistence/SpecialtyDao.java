package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.Specialty;

import java.util.List;
import java.util.Optional;

public interface SpecialtyDao {

    Optional<Specialty> findById(long id);

    Optional<Specialty> findByName(String name);

    Optional<List<Specialty>> getAll();

}
