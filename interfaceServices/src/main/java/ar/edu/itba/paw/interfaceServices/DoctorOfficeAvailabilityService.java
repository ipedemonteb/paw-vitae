package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.DoctorOfficeAvailability;
import ar.edu.itba.paw.models.DoctorOfficeAvailabilityForm;

import java.time.LocalDate;
import java.util.List;

public interface DoctorOfficeAvailabilityService {

    void update(List<DoctorOfficeAvailabilityForm> slots, Long doctorId);

    List<DoctorOfficeAvailability> getWithFilters(long doctorId, Long officeId);

    boolean isAvailableAtDayAndTime(long officeId, LocalDate date, Integer hour);

    List<DoctorOfficeAvailability> getByDoctorId(long doctorId);

}
