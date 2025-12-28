package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.DoctorOfficeSpecialty;

import java.util.List;

public interface DoctorOfficeSpecialtyService {
    List<DoctorOfficeSpecialty> getByOfficeId(long officeId);
}
