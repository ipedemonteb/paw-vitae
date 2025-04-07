package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Specialty;

import java.util.List;
import java.util.Optional;

public interface SpecialtyService {

//    List<String> getSpecialties();

    Optional<Specialty> findById(long id);

    Optional<Specialty> findByName(String name);

    Optional<List<Specialty>> getAll();

}
