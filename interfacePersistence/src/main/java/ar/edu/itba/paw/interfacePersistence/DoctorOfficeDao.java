package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.DoctorOffice;

import java.util.List;
import java.util.Optional;

public interface DoctorOfficeDao {
    DoctorOffice create(DoctorOffice doctorOffice);
    Optional<DoctorOffice> getById(long id);
    List<DoctorOffice> getByDoctorId(long doctorId);
    List<DoctorOffice> getActiveByDoctorId(long doctorId);
    List<DoctorOffice> getByNameAndNeighborhoodId(String officeName, long neighborhoodId, long doctorId);
    DoctorOffice update(DoctorOffice o);
    void softDelete(long id);
}
