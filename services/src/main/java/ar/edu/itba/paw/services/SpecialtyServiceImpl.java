package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.CoverageDao;
import ar.edu.itba.paw.interfacePersistence.SpecialtyDao;
import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Specialty;
import org.springframework.aop.aspectj.AspectJAopUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SpecialtyServiceImpl implements SpecialtyService {

//    public List<String> getSpecialties() {
//        return List.of(
//                "General", "Cardiologia", "Dermatologia",
//                "Endocrinologia", "Gastroenterología", "Hematologia",
//                "Enfermedades Infecciosas", "Nefrologia", "Neurologia",
//                "Oncologia", "Pulmonologia", "Reumatologia",
//                "Urologia", "Pediatria", "Ginecologia", "Traumatologia"
//        );
//    }
//
private final SpecialtyDao specialtyDao;

    @Autowired
    public SpecialtyServiceImpl(SpecialtyDao specialtyDao) {
        this.specialtyDao = specialtyDao;
    }

    @Override
    public Optional<Specialty> findById(long id) {
        return specialtyDao.findById(id);
    }

    @Override
    public Optional<Specialty> findByName(String name) {
        return specialtyDao.findByName(name);
    }

    @Override
    public Optional<List<Specialty>> getAll() {
        return specialtyDao.getAll();
    }

}

