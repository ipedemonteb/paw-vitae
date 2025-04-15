package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.CoverageDao;
import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class CoverageServiceImpl implements CoverageService {

     private final CoverageDao coverageDao;

     @Autowired
     public CoverageServiceImpl(CoverageDao coverageDao) {
         this.coverageDao = coverageDao;
     }


    @Override
    public Optional<Coverage> findById(long id) {
        return coverageDao.findById(id);
    }

    @Transactional
    @Override
    public Coverage create(String name) {
        return coverageDao.create(name);
    }

    @Override
    public Optional<Coverage> findByName(String name) {
        return coverageDao.findByName(name);
    }

    @Override
    public Optional<List<Coverage>> getAll() {
        return coverageDao.getAll();
    }
}
