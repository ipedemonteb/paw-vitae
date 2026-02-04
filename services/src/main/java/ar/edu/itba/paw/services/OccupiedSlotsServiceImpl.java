package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.OccupiedSlotsDao;
import ar.edu.itba.paw.interfaceServices.OccupiedSlotsService;
import ar.edu.itba.paw.interfaceServices.DoctorOfficeAvailabilityService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.models.OccupiedSlots;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorOfficeAvailability;
import ar.edu.itba.paw.models.Page; // Tu modelo de paginación
import ar.edu.itba.paw.models.exception.BussinesRuleException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class OccupiedSlotsServiceImpl implements OccupiedSlotsService {


    private final OccupiedSlotsDao occupiedSlotsDao;
    private final DoctorService doctorService;

    @Autowired
    public OccupiedSlotsServiceImpl(OccupiedSlotsDao occupiedSlotsDao, DoctorService doctorService) {
        this.doctorService = doctorService;
        this.occupiedSlotsDao = occupiedSlotsDao;
    }
    @Transactional
    @Override
    public OccupiedSlots create(long doctorId,LocalDate slotDate,LocalTime startTime) {
        Doctor doctor= doctorService.getById(doctorId).orElseThrow(UserNotFoundException::new);
        return occupiedSlotsDao.create(doctor, slotDate, startTime);
    }
    @Transactional(readOnly = true)
    @Override
    public Optional<OccupiedSlots> getById(long occupiedSlotId) {
        return occupiedSlotsDao.getById(occupiedSlotId);
    }
    @Transactional(readOnly = true)
    @Override
    public List<OccupiedSlots> getByDoctorIdInDateRange(long doctorId, LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate) || startDate.datesUntil(endDate.plusDays(1)).count() > 31) {
            throw new BussinesRuleException("exception.occupiedSlots.invalidDateRange");
        }
        return occupiedSlotsDao.getByDoctorIdInDateRange(doctorId, startDate, endDate);
    }
    @Transactional
    @Override
    public void delete(long doctorId, LocalDate slotDate, LocalTime startTime) {
        Optional<OccupiedSlots> occupiedSlotOpt = occupiedSlotsDao.getByDoctorIdInDateRange(doctorId, slotDate, slotDate)
                .stream()
                .filter(slot -> slot.getStartTime().equals(startTime))
                .findFirst();
        occupiedSlotOpt.ifPresent(occupiedSlot -> occupiedSlotsDao.delete(occupiedSlot.getId()));
    }


}