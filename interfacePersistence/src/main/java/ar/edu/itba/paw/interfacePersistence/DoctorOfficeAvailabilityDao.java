package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.DoctorOfficeAvailabilitySlot;

import java.util.List;

public interface DoctorOfficeAvailabilityDao {

    DoctorOfficeAvailabilitySlot create(DoctorOfficeAvailabilitySlot slot);

    void update(DoctorOfficeAvailabilitySlot slot);

    void delete(DoctorOfficeAvailabilitySlot slot);

    List<DoctorOfficeAvailabilitySlot> getByOfficeId(long officeId);

    List<DoctorOfficeAvailabilitySlot> getActiveByOfficeId(long officeId);

    List<DoctorOfficeAvailabilitySlot> getByDoctorId(long doctorId);
}
