package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.UnavailabilitySlot;

import java.util.List;

public interface UnavailabilitySlotsDao {

    UnavailabilitySlot create(UnavailabilitySlot slot);

    void updateDoctorUnavailability(long doctorId, List<UnavailabilitySlot> unavailabilitySlots);

    List<UnavailabilitySlot> getUnavailabilityByDoctorId(long doctorId);
}
