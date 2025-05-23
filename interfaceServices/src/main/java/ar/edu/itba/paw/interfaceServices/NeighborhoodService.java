package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Neighborhood;

import java.util.List;
import java.util.Optional;

public interface NeighborhoodService {
    Optional<Neighborhood> getById(long id);

    List<Neighborhood> getAll();
}
