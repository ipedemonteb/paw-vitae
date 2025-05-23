package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.Neighborhood;

import java.util.List;
import java.util.Optional;

public interface NeighborhoodDao {
    Optional<Neighborhood> getById(long id);

    List<Neighborhood> getAll();
}
