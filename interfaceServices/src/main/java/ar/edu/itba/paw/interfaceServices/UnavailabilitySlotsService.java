package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.*;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public interface UnavailabilitySlotsService {

    UnavailabilitySlot create(Doctor doctor, UnavailabilitySlotForm form);
    void deleteDoctorUnavailability(long doctorId, long unavailabilitySlotId);
    Page<UnavailabilitySlot> getUnavailabilityByDoctorId(long doctorId,String from, String to,int page,int pageSize);
    boolean isUnavailableAtDate(long doctorId, LocalDate date);
}
