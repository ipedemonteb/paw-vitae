package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.CoverageDao;
import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.models.Coverage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@CacheConfig(cacheNames = "coverage")
@Service
public class CoverageServiceImpl implements CoverageService {

    private final CoverageDao coverageDao;

    @Autowired
    public CoverageServiceImpl(CoverageDao coverageDao) {
        this.coverageDao = coverageDao;
    }

    @Transactional(readOnly = true)
    @Cacheable
    @Override
    public Optional<Coverage> findById(long id) {
        return coverageDao.findById(id);
    }

    @Transactional(readOnly = true)
    @Cacheable
    @Override
    public List<Coverage> getAll() {
        return coverageDao.getAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Coverage> findByIds(List<Long> ids) {
        return coverageDao.findByIds(ids);
    }

    @Transactional(readOnly = true)
    @Override
    public List<Coverage> findByDoctorId(long id) {
        return coverageDao.findByDoctorId(id);
    }
}
