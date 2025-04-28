package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Doctor;

import java.util.List;
import java.util.Optional;

public interface CoverageService {

    Optional<Coverage> findById(long id);
   List<Coverage> findByIds(List<Long> ids);

    Coverage create(String name);

    Optional<Coverage> findByName(String name);
    List<Coverage> getAll();
}
