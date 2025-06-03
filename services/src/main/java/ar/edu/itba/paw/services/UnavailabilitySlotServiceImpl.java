package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.UnavailabilitySlotsDao;
import ar.edu.itba.paw.interfaceServices.UnavailabilitySlotsService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.JsonUtils;
import ar.edu.itba.paw.models.UnavailabilitySlot;
import ar.edu.itba.paw.models.UnavailabilitySlotForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

@Service
public class UnavailabilitySlotServiceImpl implements UnavailabilitySlotsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UnavailabilitySlotServiceImpl.class);
    private final UnavailabilitySlotsDao unavailabilitySlotsDao;

    @Autowired
    public UnavailabilitySlotServiceImpl(final UnavailabilitySlotsDao unavailabilitySlotsDao) {
        this.unavailabilitySlotsDao = unavailabilitySlotsDao;
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isUnavailableAtDate(long doctorId, LocalDate date) {
        LOGGER.debug("Checking unavailability for doctor {} at date {}", doctorId, date);
        return unavailabilitySlotsDao.isUnavailableAtDate(doctorId, date);
    }


    @Transactional
    @Override
    public UnavailabilitySlot create(UnavailabilitySlot slot) {
        LOGGER.debug("Creating unavailability slot for doctor : {}", slot);
        UnavailabilitySlot toReturn = unavailabilitySlotsDao.create(slot);
        LOGGER.info("UnavailabilitySlot created: {}", toReturn);
        return toReturn;
    }

    @Transactional
    @Override
    public List<UnavailabilitySlot> create(List<UnavailabilitySlot> slots) {
        List<UnavailabilitySlot> returnSlots = new ArrayList<>();
        for (UnavailabilitySlot slot : slots) {
            returnSlots.add(create(slot));
        }
        LOGGER.debug("Creating {} unavailability slots for doctor ", slots.size());
        return returnSlots;
    }

    @Transactional
    @Override
    public void updateDoctorUnavailability(Doctor doctor, List<UnavailabilitySlotForm> unavailabilitySlots) {
        if (unavailabilitySlots == null) {
            unavailabilitySlots = Collections.emptyList();
        }
        LOGGER.debug("Updating unavailability for doctor {}: {} slots", doctor.getId(), unavailabilitySlots.size());
        List<UnavailabilitySlotForm> filteredSlots = unavailabilitySlots.stream()
                .filter(slot -> slot.getStartDate() != null && slot.getEndDate() != null)
                .toList();

        unavailabilitySlotsDao.updateDoctorUnavailability(doctor.getId(), transformToUnavailabilitySlots(doctor, filteredSlots));
        LOGGER.debug("Updated unavailability for doctor {}: {} slots", doctor.getId(), filteredSlots.size());
    }

    @Transactional(readOnly = true)
    @Override
    public List<UnavailabilitySlot> getUnavailabilityByDoctorId(long doctorId) {
        LOGGER.debug("Getting unavailability slots for doctor {}", doctorId);
        return unavailabilitySlotsDao.getUnavailabilityByDoctorId(doctorId);
    }
    @Transactional
    @Override
    public List<UnavailabilitySlotForm> getDoctorUnavailabilitySlots(Doctor doctor) {
        List<UnavailabilitySlot> slots = getUnavailabilityByDoctorId(doctor.getId());
        List<UnavailabilitySlotForm> unavailabilitySlots = new ArrayList<>();       //TODO CORREGIR
        for (UnavailabilitySlot slot : slots) {
            UnavailabilitySlotForm form = new UnavailabilitySlotForm(slot.getStartDate(), slot.getEndDate());
            unavailabilitySlots.add(form);
        }
        return unavailabilitySlots.stream()
                .sorted(Comparator.comparing(UnavailabilitySlotForm::getStartDate)
                        .thenComparing(UnavailabilitySlotForm::getEndDate))
                .toList();

    }
    @Transactional(readOnly = true)
    @Override
    public List<UnavailabilitySlot> getUnavailabilityByDoctorIdCurrentAndNextMonth(long doctorId) {
        LOGGER.debug("Getting unavailability slots for doctor {} in current and next month", doctorId);
        return unavailabilitySlotsDao.getUnavailabilityByDoctorIdCurrentAndNextMonth(doctorId);
    }

    @Transactional
    @Override
    public List<UnavailabilitySlot> transformToUnavailabilitySlots(Doctor doctor, List<UnavailabilitySlotForm> unavailabilitySlots) {
        List<UnavailabilitySlot> slots = new ArrayList<>();
        for (UnavailabilitySlotForm slot : unavailabilitySlots) {
            UnavailabilitySlot unavailabilitySlot = slot.toEntity(doctor);
            slots.add(unavailabilitySlot);
        }
        return slots;
    }
    @Transactional
    @Override
    public List<UnavailabilitySlotForm> transformToUnavailabilitySlotForms(List<UnavailabilitySlot> unavailabilitySlots) {
        List<UnavailabilitySlotForm> slots = new ArrayList<>();
        for (UnavailabilitySlot slot : unavailabilitySlots) {
            UnavailabilitySlotForm unavailabilitySlot = UnavailabilitySlotForm.fromEntity(slot);
            slots.add(unavailabilitySlot);
        }
        return slots;
    }
    @Transactional(readOnly = true)
    @Override
    public String getUnavailabilityByDoctorIdAndMonthAndYear(long doctorId, int month, int year) {
        if (month < 1 || month > 12) {
            month = LocalDate.now().getMonthValue();
        }
        if (year < 1) {
            year = LocalDate.now().getYear();
        }
        LOGGER.debug("Getting unavailability slots for doctor {} in month {} and year {}", doctorId, month, year);
        List<UnavailabilitySlotForm> unavailabilitySlotForms = transformToUnavailabilitySlotForms(unavailabilitySlotsDao.getUnavailabilityByDoctorIdAndMonthAndYear(doctorId, month, year));
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("unavailabilitySlots", unavailabilitySlotForms);
        response.put("month", month);
        response.put("year", year);
        return JsonUtils.toJson(response);
    }
}
