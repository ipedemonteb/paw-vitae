package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.DoctorOffice;
import ar.edu.itba.paw.models.DoctorOfficeAvailabilitySlot;
import ar.edu.itba.paw.models.DoctorOfficeAvailabilitySlotForm;

import java.time.LocalDate;
import java.util.List;

public interface DoctorOfficeAvailabilityService {

    DoctorOfficeAvailabilitySlot create(DoctorOfficeAvailabilitySlotForm slot, DoctorOffice doctorOffice);

    List<DoctorOfficeAvailabilitySlot> create(List<DoctorOfficeAvailabilitySlotForm> slots, DoctorOffice doctorOffice);

    void update(List<DoctorOfficeAvailabilitySlotForm> slots, Long doctorId);

    List<DoctorOfficeAvailabilitySlot> getByOfficeId(long officeId);

    boolean isAvailableAtDayAndTime(long officeId, LocalDate date, Integer hour);

    String getJsonByDoctorId(long doctorId);

    List<DoctorOfficeAvailabilitySlot> getByDoctorId(long doctorId);

}
