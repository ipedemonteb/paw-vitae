package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.AvailabilitySlots;

import java.util.List;
import java.util.Optional;


public interface AvailabilitySlotsService {
    Optional<AvailabilitySlots> getById(long availabilitySlotId);

    List<AvailabilitySlots> getAvailableSlots(long doctorId);

    void reloadAvailability(long doctorId);

    void generateDailySlots();

    void setAvailabilitySlotUnavailable(long availabilitySlotId);
}