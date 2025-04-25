package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Doctor;

import java.util.List;
import java.util.Optional;

public interface CoverageDao {

    Optional<Coverage> findById(long id);
    Optional<List<Coverage>> findByIds(List<Long> ids);

    Coverage create(String name);

    Optional<Coverage> findByName(String name);
    Optional<List<Coverage>> getAll();
}
