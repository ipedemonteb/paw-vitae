package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorOfficeAvailabilityDao;
import ar.edu.itba.paw.interfaceServices.DoctorOfficeAvailabilityService;
import ar.edu.itba.paw.models.DoctorOffice;
import ar.edu.itba.paw.models.DoctorOfficeAvailabilitySlot;
import ar.edu.itba.paw.models.DoctorOfficeAvailabilitySlotForm;
import ar.edu.itba.paw.models.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DoctorOfficeAvailabilitySlotServiceImpl implements DoctorOfficeAvailabilityService {

    private final DoctorOfficeAvailabilityDao doctorOfficeAvailabilityDao;

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorOfficeAvailabilitySlotServiceImpl.class);

    @Autowired
    public DoctorOfficeAvailabilitySlotServiceImpl(DoctorOfficeAvailabilityDao doctorOfficeAvailabilityDao) {
        this.doctorOfficeAvailabilityDao = doctorOfficeAvailabilityDao;
    }

    @Override
    public DoctorOfficeAvailabilitySlot create(DoctorOfficeAvailabilitySlotForm slot, DoctorOffice doctorOffice) {
        LOGGER.debug("Creating availability slot for office : {}",  slot);
        DoctorOfficeAvailabilitySlot toReturn = doctorOfficeAvailabilityDao.create(slot.toEntity(doctorOffice));
        LOGGER.info("AvailabilitySlot for office created: {}", toReturn);
        return toReturn;
    }

    @Override
    public List<DoctorOfficeAvailabilitySlot> getByOfficeId(long officeId) {
        LOGGER.debug("Retrieving availability slots for office with id: {}", officeId);
        return doctorOfficeAvailabilityDao.getByOfficeId(officeId);
    }

    @Override
    public List<DoctorOfficeAvailabilitySlot> create(List<DoctorOfficeAvailabilitySlotForm> slots, DoctorOffice doctorOffice) {
        List<DoctorOfficeAvailabilitySlot> toReturn = new ArrayList<>();
        slots.forEach(slot -> toReturn.add(create(slot, doctorOffice)));
        return toReturn;
    }

    @Override
    public void update(List<DoctorOfficeAvailabilitySlotForm> slots, DoctorOffice doctorOffice) {

    }
     //uses Jackson to convert the list of slots to a JSON string
    @Transactional(readOnly = true)
    @Override
    public String getByDoctorId(long doctorId) {
        List<DoctorOfficeAvailabilitySlot> slots = doctorOfficeAvailabilityDao.getByDoctorId(doctorId);
        if (slots.isEmpty()) {
            LOGGER.debug("No availability slots found for doctor with id: {}", doctorId);
            return "[]"; // Return an empty JSON array if no slots are found
        }
        Map<Long, List<DoctorOfficeAvailabilitySlot>> officeSlotsMap = new HashMap<>();
        for (DoctorOfficeAvailabilitySlot slot : slots) {
            long officeId = slot.getOffice().getId();
            officeSlotsMap.computeIfAbsent(officeId, k -> new ArrayList<>()).add(slot);
        }

        Map<String, Object> responseMap = new HashMap<>();
        for (Map.Entry<Long, List<DoctorOfficeAvailabilitySlot>> entry : officeSlotsMap.entrySet()) {
            long officeId = entry.getKey();
            List<DoctorOfficeAvailabilitySlot> officeSlots = entry.getValue();
            responseMap.put(String.valueOf(officeId), officeSlots);
        }

        return JsonUtils.toJson(responseMap, DoctorOfficeAvailabilitySlot.Views.Public.class);
    }

    @Override
    public boolean isAvailableAtDayAndTime(long officeId, LocalDate date, Integer hour) {
        LOGGER.debug("Checking availability for office with id: {}, date: {}, startTime: {}", officeId, date, hour);
        List<DoctorOfficeAvailabilitySlot> slots = doctorOfficeAvailabilityDao.getByOfficeId(officeId);
        return slots.stream().anyMatch(slot -> slot.getDayOfWeek() == (date.getDayOfWeek().getValue() -1) &&
               slot.getStartTime().getHour() <= hour &&
               slot.getEndTime().getHour() > hour);
    }
}
