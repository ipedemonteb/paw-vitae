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

//    @Transactional
//    @Override
//    public void update(List<DoctorOfficeForm> slots, Long doctorId) {
//        List<DoctorOfficeAvailabilitySlotForm> flatSlots = new ArrayList<>();
//        slots.forEach(slot -> flatSlots.addAll(slot.getOfficeAvailabilitySlotForms()));
//
//        List<DoctorOfficeAvailabilitySlot> allSlots = doctorOfficeAvailabilityDao.getByDoctorId(doctorId);
//        List<DoctorOffice> offices = doctorOfficeService.getByDoctorId(doctorId);
//
//        Map<Long, DoctorOfficeAvailabilitySlot> slotMap = allSlots.stream()
//                .collect(Collectors.toMap(DoctorOfficeAvailabilitySlot::getId, slot -> slot));
//
//        Set<Long> keepIds = new HashSet<>();
//
//        for (DoctorOfficeAvailabilitySlotForm slotForm : flatSlots) {
//            DoctorOffice doctorOffice = offices.stream()
//                    .filter(o -> o.getId() == slotForm.getOfficeId())
//                    .findFirst()
//                    .orElseThrow(() -> new IllegalArgumentException("Office with id " + slotForm.getOfficeId() + " not found"));
//
//            if (slotForm.getId() != null) {
//                DoctorOfficeAvailabilitySlot existingSlot = slotMap.get(slotForm.getId());
//                if (existingSlot == null) {
//                    throw new IllegalArgumentException("Slot with id " + slotForm.getId() + " not found");
//                }
//                existingSlot.setStartTime(slotForm.getStartTime());
//                existingSlot.setEndTime(slotForm.getEndTime());
//                existingSlot.setDayOfWeek(slotForm.getDayOfWeek());
//                existingSlot.setOffice(doctorOffice);
//                doctorOfficeAvailabilityDao.update(existingSlot);
//                keepIds.add(existingSlot.getId());
//            } else {
//                DoctorOfficeAvailabilitySlot newSlot = slotForm.toEntity(doctorOffice);
//                doctorOfficeAvailabilityDao.create(newSlot);
//            }
//        }
//        // Delete slots not in keepIds
//        allSlots.stream()
//                .filter(slot -> !keepIds.contains(slot.getId()))
//                .forEach(doctorOfficeAvailabilityDao::delete);
//    }

    @Transactional
    @Override
    public void update(
            List<DoctorOfficeAvailabilitySlotForm> slotForms,
            Long doctorId
    ) {
        // 1) load offices + slots
        List<DoctorOffice> offices =
                doctorOfficeService.getByDoctorId(doctorId);

        // 3) group incoming forms by officeId
        Map<Long,List<DoctorOfficeAvailabilitySlotForm>> formsByOffice =
                slotForms.stream()
                        .collect(groupingBy(DoctorOfficeAvailabilitySlotForm::getOfficeId));

        // 4) for each office, build its new slot list
        for (DoctorOffice office : offices) {
            List<DoctorOfficeAvailabilitySlotForm> incoming =
                    formsByOffice.getOrDefault(office.getId(), Collections.emptyList());

            // map existing slots by their ID
            Map<Long,DoctorOfficeAvailabilitySlot> existingById =
                    office.getDoctorOfficeAvailabilitySlots().stream()
                            .collect(toMap(DoctorOfficeAvailabilitySlot::getId, s->s));

            // build the “new” list of entities
            List<DoctorOfficeAvailabilitySlot> newSlots = incoming.stream()
                    .map(form -> {
                        if (form.getId() != null) {
                            // update existing
                            var slot = existingById.get(form.getId());
                            if (slot == null) throw new IllegalArgumentException(
                                    "Slot id=" + form.getId() + " not found");
                            slot.setStartTime(form.getStartTime());
                            slot.setEndTime(  form.getEndTime());
                            slot.setDayOfWeek(form.getDayOfWeek());
                            slot.setOffice(office);
                            return slot;
                        } else {
                            // brand-new slot
                            return form.toEntity(office);
                        }
                    })
                    .collect(toList());

            // 5) replace the collection
            office.getDoctorOfficeAvailabilitySlots().clear();
            office.getDoctorOfficeAvailabilitySlots().addAll(newSlots);
        }

        // 6) flush – because of CascadeType.ALL + orphanRemoval=true,
        //    Hibernate will INSERT newSlots without IDs,
        //    UPDATE the ones you modified,
        //    and DELETE any orphans you removed.
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
