package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.AvailabilitySlot;
import ar.edu.itba.paw.models.AvailabilitySlotForm;
import ar.edu.itba.paw.models.Doctor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface AvailabilitySlotsService {

    AvailabilitySlot create( AvailabilitySlot slot);

    void updateDoctorAvailability(Doctor doctor, List<AvailabilitySlotForm> availabilitySlots);

    List<AvailabilitySlot> getAvailabilityByDoctorId(long doctorId);

    List<AvailabilitySlot> create(List<AvailabilitySlot> slots);

    boolean isAvailableAtDateAndTime(long doctorId, LocalDate date, int time);
    List<AvailabilitySlotForm> getDoctorAvailabilitySlots(Doctor doctor);
    List<AvailabilitySlot> transformToAvailabilitySlots(Doctor doctor,List<AvailabilitySlotForm> availabilitySlots);
    List<AvailabilitySlotForm> transformToAvailabilitySlotForms(List<AvailabilitySlot> availabilitySlots);
}
