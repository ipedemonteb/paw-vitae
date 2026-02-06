package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.DoctorOffice;
import ar.edu.itba.paw.models.DoctorOfficeAvailability;
import ar.edu.itba.paw.models.DoctorOfficeAvailabilityForm;

import java.time.LocalDate;
import java.util.List;

public interface DoctorOfficeAvailabilityService {

    DoctorOfficeAvailability create(DoctorOfficeAvailabilityForm slot, DoctorOffice doctorOffice);

    List<DoctorOfficeAvailability> create(List<DoctorOfficeAvailabilityForm> slots, DoctorOffice doctorOffice);

    void update(List<DoctorOfficeAvailabilityForm> slots, Long doctorId);

    List<DoctorOfficeAvailability> getWithFilters(long doctorId, Long officeId);

    boolean isAvailableAtDayAndTime(long officeId, LocalDate date, Integer hour);

    String getJsonByDoctorId(long doctorId);

    List<DoctorOfficeAvailability> getByDoctorId(long doctorId);

}
