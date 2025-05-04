package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Specialty;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface SpecialtyService {

//    List<String> getSpecialties();

    Optional<Specialty> getById(long id);
    List<Specialty> getByIds(List<Long> ids);

    List<Specialty> getByDoctorId(long id);

    Optional<Specialty> getByName(String name);

    List<Specialty> getAll();

}
