package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.AvailabilitySlot;

import java.util.List;

public interface AvailabilitySlotsDao {

    AvailabilitySlot create(long docId, AvailabilitySlot slot);

    void updateDoctorAvailability(long id, List<AvailabilitySlot> availabilitySlots);

    List<AvailabilitySlot> getAvailabilityByDoctorId(long doctorId);
}
