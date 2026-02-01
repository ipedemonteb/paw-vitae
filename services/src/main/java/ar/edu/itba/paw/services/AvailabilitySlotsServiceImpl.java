package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AvailabilitySlotsDao;
import ar.edu.itba.paw.interfaceServices.AvailabilitySlotsService;
import ar.edu.itba.paw.interfaceServices.DoctorOfficeAvailabilityService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.models.AvailabilitySlots;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorOfficeAvailability;
import ar.edu.itba.paw.models.Page; // Tu modelo de paginación
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
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class AvailabilitySlotsServiceImpl implements AvailabilitySlotsService {

    private final static Logger LOGGER = LoggerFactory.getLogger(AvailabilitySlotsServiceImpl.class);
    private final static int WINDOW_DAYS = 30;
    private final static int BATCH_SIZE = 50;

    private final AvailabilitySlotsDao availabilitySlotsDao;
    private final DoctorOfficeAvailabilityService doctorOfficeAvailabilityService;
    private final DoctorService doctorService;

    @Autowired
    public AvailabilitySlotsServiceImpl(AvailabilitySlotsDao availabilitySlotsDao,
                                        DoctorOfficeAvailabilityService doctorOfficeAvailabilityService,
                                        DoctorService doctorService) {
        this.availabilitySlotsDao = availabilitySlotsDao;
        this.doctorOfficeAvailabilityService = doctorOfficeAvailabilityService;
        this.doctorService = doctorService;
    }

    @Override
    public Optional<AvailabilitySlots> getById(long availabilitySlotId) {
        return availabilitySlotsDao.getById(availabilitySlotId);
    }

    @Override
    public List<AvailabilitySlots> getAvailableSlots(long doctorId) {
        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(WINDOW_DAYS);
        return availabilitySlotsDao.getByDoctorIdInDateRange(doctorId, start, end);
    }

    @Override
    @Async
    @Transactional
    @Scheduled(cron = "0 * * * * ?") //TODO: change everyday at 00:00
    public void generateDailySlots() {
        LOGGER.info("Iniciando generación diaria de slots (Ventana +{} días)", WINDOW_DAYS);
        LocalDate targetDate = LocalDate.now().plusDays(WINDOW_DAYS);
        int page = 1;
        int currentPage = 1;

        while (true) {
            Page<Doctor> doctorPage = doctorService.getWithFilters(
                    -1,
                    -1,
                    null,
                    null,
                    "id",
                    "asc",
                    currentPage,
                    BATCH_SIZE
            );

            List<Doctor> batchDoctors = doctorPage.getContent();

            if (batchDoctors.isEmpty()) {
                break;
            }

            for (Doctor doctor : batchDoctors) {
                try {
                    generateSlotsForSingleDaySafely(doctor, targetDate);
                } catch (Exception e) {
                    LOGGER.error("Error generando slots para doctor {}: {}", doctor.getId(), e.getMessage());
                }
            }

            if ( batchDoctors.size() < BATCH_SIZE){
                break;
            }

            currentPage++;
        }

        LOGGER.info("Finalizada generación diaria de slots.");
    }

    @Transactional
    public void generateSlotsForSingleDaySafely(Doctor doctor, LocalDate date) {
        List<DoctorOfficeAvailability> availabilities = doctorOfficeAvailabilityService.getByDoctorId(doctor.getId());
        for (DoctorOfficeAvailability rule : availabilities) {
            if (rule.getDayOfWeek() == date.getDayOfWeek().getValue()) {
                createSlotsFromRule(doctor, date, rule);
            }
        }
    }

    @Override
    @Transactional
    public void reloadAvailability(long doctorId) {
        LOGGER.info("Recalculando ventana para médico {}", doctorId);
        availabilitySlotsDao.deleteUnbookedSlots(doctorId, LocalDate.now());

        Doctor doctor = doctorService.getById(doctorId).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        LocalDate start = LocalDate.now();
        LocalDate end = start.plusDays(WINDOW_DAYS);

        Stream.iterate(start, date -> date.plusDays(1))
                .limit(ChronoUnit.DAYS.between(start, end) + 1)
                .forEach(date -> generateSlotsForSingleDaySafely(doctor, date));
    }

    private void createSlotsFromRule(Doctor doctor, LocalDate date, DoctorOfficeAvailability rule) {
        LocalTime current = rule.getStartTime();
        LocalTime end = rule.getEndTime();

        while (current.isBefore(end)) {
            if (!availabilitySlotsDao.exists(doctor.getId(), date, current)) {
                AvailabilitySlots slot = new AvailabilitySlots(
                        doctor,
                        date,
                        current,
                        AvailabilitySlots.SlotStatus.AVAILABLE
                );
                availabilitySlotsDao.create(slot);
            }
            current = current.plusHours(1);
        }
    }

    @Override
    @Transactional
    public void setAvailabilitySlotUnavailable(long availabilitySlotId) {
        availabilitySlotsDao.getById(availabilitySlotId).ifPresent(slot -> {
            slot.setStatus(AvailabilitySlots.SlotStatus.UNAVAILABLE);
        });
    }
}