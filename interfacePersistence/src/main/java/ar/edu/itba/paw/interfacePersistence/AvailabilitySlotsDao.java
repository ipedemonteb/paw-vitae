package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.AvailabilitySlots;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface AvailabilitySlotsDao {
    AvailabilitySlots create(AvailabilitySlots slot);
    Optional<AvailabilitySlots> getById(long id);
    List<AvailabilitySlots> getByDoctorIdInDateRange(long doctorId, LocalDate startDate, LocalDate endDate);
    boolean exists(long doctorId, LocalDate date, LocalTime time);
    int deleteUnbookedSlots(long doctorId, LocalDate fromDate);
}
