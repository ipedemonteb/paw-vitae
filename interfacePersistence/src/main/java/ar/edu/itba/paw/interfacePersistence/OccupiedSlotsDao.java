package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.OccupiedSlots;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface OccupiedSlotsDao {
    OccupiedSlots create(Doctor doctor, LocalDate slotDate, LocalTime startTime);
    Optional<OccupiedSlots> getById(long id);
    List<OccupiedSlots> getByDoctorIdInDateRange(long doctorId, LocalDate startDate, LocalDate endDate);
    void delete(long slotId);

}
