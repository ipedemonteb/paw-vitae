package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.AvailabilitySlot;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface AvailabilitySlotsService {

    AvailabilitySlot create(long docId, AvailabilitySlot slot);

    void updateDoctorAvailability(long id, List<AvailabilitySlot> availabilitySlots);

    List<AvailabilitySlot> getAvailabilityByDoctorId(long doctorId);

    List<AvailabilitySlot> create(long docId, List<AvailabilitySlot> slots);
}
