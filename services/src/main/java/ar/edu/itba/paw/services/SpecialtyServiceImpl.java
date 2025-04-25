package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.SpecialtyDao;
import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import ar.edu.itba.paw.models.Specialty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@CacheConfig(cacheNames = "specialty")
@Service
public class SpecialtyServiceImpl implements SpecialtyService {

private final SpecialtyDao specialtyDao;

    @Autowired
    public SpecialtyServiceImpl(SpecialtyDao specialtyDao) {
        this.specialtyDao = specialtyDao;
    }

    @Cacheable
    @Override
    public Optional<Specialty> getById(long id) {
        return specialtyDao.getById(id);
    }

    @Cacheable
    @Override
    public Optional<Specialty> getByName(String name) {
        return specialtyDao.getByName(name);
    }

    @Cacheable
    @Override
    public Optional<List<Specialty>> getAll() {
        return specialtyDao.getAll();
    }

    @Override
    public Optional<List<Specialty>> getByIds(List<Long> ids) {
        return specialtyDao.getByIds(ids);
    }
}

