package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorOfficeAvailabilityDao;
import ar.edu.itba.paw.interfaceServices.DoctorOfficeAvailabilityService;
import ar.edu.itba.paw.interfaceServices.DoctorOfficeService;
import ar.edu.itba.paw.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.*;

@Service
public class DoctorOfficeAvailabilitySlotServiceImpl implements DoctorOfficeAvailabilityService {

    private final DoctorOfficeAvailabilityDao doctorOfficeAvailabilityDao;
    private final DoctorOfficeService doctorOfficeService;

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorOfficeAvailabilitySlotServiceImpl.class);

    @Autowired
    public DoctorOfficeAvailabilitySlotServiceImpl(DoctorOfficeAvailabilityDao doctorOfficeAvailabilityDao,
                                                   @Lazy DoctorOfficeService doctorOfficeService) {
        this.doctorOfficeAvailabilityDao = doctorOfficeAvailabilityDao;
        this.doctorOfficeService = doctorOfficeService;
    }

    @Transactional
    @Override
    public DoctorOfficeAvailabilitySlot create(DoctorOfficeAvailabilitySlotForm slot, DoctorOffice doctorOffice) {
        LOGGER.debug("Creating availability slot for office : {}",  slot);
        DoctorOfficeAvailabilitySlot toReturn = doctorOfficeAvailabilityDao.create(slot.toEntity(doctorOffice));
        LOGGER.info("AvailabilitySlot for office created: {}", toReturn);
        return toReturn;
    }

    @Transactional(readOnly = true)
    @Override
    public List<DoctorOfficeAvailabilitySlot> getByOfficeId(long officeId) {
        LOGGER.debug("Retrieving availability slots for office with id: {}", officeId);
        return doctorOfficeAvailabilityDao.getByOfficeId(officeId);
    }

    @Transactional
    @Override
    public List<DoctorOfficeAvailabilitySlot> create(List<DoctorOfficeAvailabilitySlotForm> slots, DoctorOffice doctorOffice) {
        List<DoctorOfficeAvailabilitySlot> toReturn = new ArrayList<>();
        slots.forEach(slot -> toReturn.add(create(slot, doctorOffice)));
        return toReturn;
    }

    @Transactional
    @Override
    public void update(
            List<DoctorOfficeAvailabilitySlotForm> forms,
            Long doctorId
    ) {
        // 1) Load all existing slots for this doctor
        List<DoctorOfficeAvailabilitySlot> allSlots =
                doctorOfficeAvailabilityDao.getByDoctorId(doctorId);

        // 2) Build lookup maps
        Map<Long,DoctorOfficeAvailabilitySlot> existingById = allSlots.stream()
                .collect(Collectors.toMap(DoctorOfficeAvailabilitySlot::getId, slot -> slot));

        List<DoctorOffice> offices = doctorOfficeService.getAllByDoctorId(doctorId);
        Map<Long,DoctorOffice> officeById = offices.stream()
                .collect(Collectors.toMap(DoctorOffice::getId, office -> office));

        Set<Long> processedIds = new HashSet<>();

        // 3) Iterate incoming forms
        for (DoctorOfficeAvailabilitySlotForm form : forms) {
            Long formId = form.getId();
            DoctorOffice targetOffice = officeById.get(form.getOfficeId());
            if (targetOffice == null) {
                throw new IllegalArgumentException(
                        "Unknown officeId: " + form.getOfficeId());
            }

            if (formId != null && existingById.containsKey(formId)) {
                // — UPDATE (and possibly MOVE) an existing slot
                DoctorOfficeAvailabilitySlot slot = existingById.get(formId);

                // update times
                slot.setStartTime(form.getStartTime());
                slot.setEndTime(  form.getEndTime()  );
                slot.setDayOfWeek(form.getDayOfWeek());

                // allow office‐move
                if (!(slot.getOffice().getId() == targetOffice.getId())) {
                    slot.setOffice(targetOffice);
                }

                doctorOfficeAvailabilityDao.update(slot);
                processedIds.add(formId);

            } else {
                // — CREATE a brand-new slot
                DoctorOfficeAvailabilitySlot newSlot = form.toEntity(targetOffice);
                doctorOfficeAvailabilityDao.create(newSlot);
                // no need to add to processedIds, since it's new
            }
        }

        // 4) DELETE any slots that weren’t in the incoming list
        allSlots.stream()
                .filter(slot -> !processedIds.contains(slot.getId()))
                .forEach(doctorOfficeAvailabilityDao::delete);
    }




    @Transactional(readOnly = true)
    @Override
    public String getJsonByDoctorId(long doctorId) {
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

    @Transactional(readOnly = true)
    @Override
    public List<DoctorOfficeAvailabilitySlot> getByDoctorId(long doctorId) {
        LOGGER.debug("Retrieving availability slots for doctor with id: {}", doctorId);
        return doctorOfficeAvailabilityDao.getByDoctorId(doctorId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isAvailableAtDayAndTime(long officeId, LocalDate date, Integer hour) {
        LOGGER.debug("Checking availability for office with id: {}, date: {}, startTime: {}", officeId, date, hour);
        List<DoctorOfficeAvailabilitySlot> slots = doctorOfficeAvailabilityDao.getActiveByOfficeId(officeId);
        return slots.stream().anyMatch(slot -> slot.getDayOfWeek() == (date.getDayOfWeek().getValue() -1) &&
               slot.getStartTime().getHour() <= hour &&
               slot.getEndTime().getHour() > hour);
    }
}
