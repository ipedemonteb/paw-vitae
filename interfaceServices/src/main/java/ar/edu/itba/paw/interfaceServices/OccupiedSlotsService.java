package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.OccupiedSlots;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


public interface OccupiedSlotsService {
    OccupiedSlots create(long doctorId, LocalDate slotDate, LocalTime startTime);
    Optional<OccupiedSlots> getById(long id);
    List<OccupiedSlots> getByDoctorIdInDateRange(long doctorId, LocalDate startDate, LocalDate endDate);
    void delete(long doctorId, LocalDate slotDate,LocalTime startTime);
}