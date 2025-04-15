package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.AvailabilitySlot;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AvailabilitySlotsService {
    // Add new availability slots for a doctor
    void addAvailability(long doctorId, List<AvailabilitySlot> availabilitySlots);

    // Get all availability slots for a doctor
    List<AvailabilitySlot> getAvailabilityByDoctorId(long doctorId);

    // Update existing availability slots for a doctor
    void updateAvailability(long doctorId, List<AvailabilitySlot> updatedAvailabilitySlots);
}
