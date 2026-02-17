package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.DoctorOfficeAvailability;

import java.util.List;

public interface DoctorOfficeAvailabilityDao {


    List<DoctorOfficeAvailability> getByOfficeId(long officeId);

    List<DoctorOfficeAvailability> getActiveByOfficeId(long officeId);

    List<DoctorOfficeAvailability> getByDoctorId(long doctorId);

}
