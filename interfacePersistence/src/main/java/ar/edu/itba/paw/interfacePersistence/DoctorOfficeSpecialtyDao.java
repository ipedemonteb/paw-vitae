package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.DoctorOfficeSpecialty;

import java.util.List;

public interface DoctorOfficeSpecialtyDao {
    List<DoctorOfficeSpecialty> getByOfficeId(long officeId);
}
