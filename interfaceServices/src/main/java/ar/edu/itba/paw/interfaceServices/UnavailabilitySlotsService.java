package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface UnavailabilitySlotsService {

    UnavailabilitySlot create(UnavailabilitySlot slot);

    void updateDoctorUnavailability(Doctor doctor, List<UnavailabilitySlotForm> unavailabilitySlots);

    Page<UnavailabilitySlot> getUnavailabilityByDoctorId(long doctorId,String from, String to,int page,int pageSize);

    List<UnavailabilitySlot> create(List<UnavailabilitySlot> slots);

    boolean isUnavailableAtDate(long doctorId, LocalDate date);

    List<UnavailabilitySlot> transformToUnavailabilitySlots(Doctor doctor,List<UnavailabilitySlotForm> unavailabilitySlots);

}
