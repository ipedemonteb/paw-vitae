package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.UnavailabilitySlot;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface UnavailabilitySlotsDao {

    UnavailabilitySlot create(UnavailabilitySlot slot);
    Optional<UnavailabilitySlot> getById(long unavailabilitySlotId);
    void delete(UnavailabilitySlot slot);

    List<UnavailabilitySlot> getUnavailabilityByDoctorId(long doctorId);
    List<UnavailabilitySlot> getUnavailabilityByDoctorIdPaginated(long doctorId, int page, int pageSize);

    int countUnavailabilityByDoctorId(long doctorId);
    List<UnavailabilitySlot> getUnavailabilityByDoctorIdInDateRange(long doctorId, LocalDate from, LocalDate to);

    boolean isUnavailableAtDate(long doctorId, LocalDate date);

     List<UnavailabilitySlot> getUnavailabilityByDoctorIdCurrentAndNextMonth(long doctorId);

     List<UnavailabilitySlot> getUnavailabilityByDoctorIdAndMonthAndYear(long doctorId, int month, int year);
     boolean hasOverlap(long doctorId, LocalDate start, LocalDate end);
}
