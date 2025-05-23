package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.NeighborhoodDao;
import ar.edu.itba.paw.interfaceServices.NeighborhoodService;
import ar.edu.itba.paw.models.Neighborhood;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@CacheConfig(cacheNames = "neighborhood")
@Service
public class NeighborhoodServiceImpl implements NeighborhoodService {

    private final NeighborhoodDao neighborhoodDao;

    @Autowired
    public NeighborhoodServiceImpl(final NeighborhoodDao neighborhoodDao) {
        this.neighborhoodDao = neighborhoodDao;
    }

    @Transactional(readOnly = true)
    @Cacheable
    @Override
    public Optional<Neighborhood> getById(long id) {
        return neighborhoodDao.getById(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Neighborhood> getAll() {
        return neighborhoodDao.getAll();
    }
}
