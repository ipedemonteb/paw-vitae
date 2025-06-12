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
import java.util.stream.Collectors;

import static java.util.Collections.emptyList;
import static java.util.stream.Collectors.*;

@Service
public class DoctorOfficeAvailabilityServiceImpl implements DoctorOfficeAvailabilityService {

    private final DoctorOfficeAvailabilityDao doctorOfficeAvailabilityDao;
    private final DoctorOfficeService doctorOfficeService;

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorOfficeAvailabilityServiceImpl.class);

    @Autowired
    public DoctorOfficeAvailabilityServiceImpl(DoctorOfficeAvailabilityDao doctorOfficeAvailabilityDao,
                                               @Lazy DoctorOfficeService doctorOfficeService) {
        this.doctorOfficeAvailabilityDao = doctorOfficeAvailabilityDao;
        this.doctorOfficeService = doctorOfficeService;
    }

    @Transactional
    @Override
    public DoctorOfficeAvailability create(DoctorOfficeAvailabilityForm slot, DoctorOffice doctorOffice) {
        LOGGER.debug("Creating availability slot for office : {}",  slot);
        DoctorOfficeAvailability toReturn = doctorOfficeAvailabilityDao.create(slot.toEntity(doctorOffice));
        LOGGER.info("AvailabilitySlot for office created: {}", toReturn);
        return toReturn;
    }

    @Transactional(readOnly = true)
    @Override
    public List<DoctorOfficeAvailability> getByOfficeId(long officeId) {
        LOGGER.debug("Retrieving availability slots for office with id: {}", officeId);
        return doctorOfficeAvailabilityDao.getByOfficeId(officeId);
    }

    @Transactional
    @Override
    public List<DoctorOfficeAvailability> create(List<DoctorOfficeAvailabilityForm> slots, DoctorOffice doctorOffice) {
        List<DoctorOfficeAvailability> toReturn = new ArrayList<>();
        slots.forEach(slot -> toReturn.add(create(slot, doctorOffice)));
        return toReturn;
    }

    @Transactional
    @Override
    public void update(
            List<DoctorOfficeAvailabilityForm> forms,
            Long doctorId
    ) {
        // 1) Load offices + current slots
        List<DoctorOffice> offices =
                doctorOfficeService.getAllByDoctorIdWithAvailability(doctorId);

        // 2) Index existing slots by ID
        Map<Long,DoctorOfficeAvailability> existingById =
                offices.stream()
                        .flatMap(o -> o.getDoctorOfficeAvailability().stream())
                        .collect(Collectors.toMap(DoctorOfficeAvailability::getId, s -> s));

        // 3) Group incoming forms by their target office
        Map<Long,List<DoctorOfficeAvailabilityForm>> formsByOffice =
                forms.stream()
                        .collect(Collectors.groupingBy(DoctorOfficeAvailabilityForm::getOfficeId));

        // 4) For each office, build its *new* slot list
        for (DoctorOffice office : offices) {
            List<DoctorOfficeAvailabilityForm> incoming =
                    formsByOffice.getOrDefault(office.getId(), List.of());

            List<DoctorOfficeAvailability> newSlots = new ArrayList<>();
            for (var form : incoming) {
                if (form.getId() != null) {
                    var existing = existingById.get(form.getId());
                    if (existing != null && existing.getOffice().getId().equals(office.getId())) {
                        // — same office: update in place
                        existing.setStartTime(form.getStartTime());
                        existing.setEndTime(  form.getEndTime());
                        existing.setDayOfWeek(form.getDayOfWeek());
                        newSlots.add(existing);
                    } else {
                        // — either moved here or brand-new: create new entity
                        newSlots.add(form.toEntity(office));
                    }
                } else {
                    // — brand-new slot
                    newSlots.add(form.toEntity(office));
                }
            }

            // 5) Replace the collection in one go
            office.replaceAvailability(newSlots);
        }

        // 6) flush on commit; Hibernate issues:
        //   • DELETE for any slot cleared out of its old office
        //   • UPDATE for the ones you mutated in-place
        //   • INSERT for every newSlots without an ID
    }






    @Transactional(readOnly = true)
    @Override
    public String getJsonByDoctorId(long doctorId) {
        List<DoctorOfficeAvailability> slots = doctorOfficeAvailabilityDao.getByDoctorId(doctorId);
        if (slots.isEmpty()) {
            LOGGER.debug("No availability slots found for doctor with id: {}", doctorId);
            return "[]"; // Return an empty JSON array if no slots are found
        }
        Map<Long, List<DoctorOfficeAvailability>> officeSlotsMap = new HashMap<>();
        for (DoctorOfficeAvailability slot : slots) {
            long officeId = slot.getOffice().getId();
            officeSlotsMap.computeIfAbsent(officeId, k -> new ArrayList<>()).add(slot);
        }

        Map<String, Object> responseMap = new HashMap<>();
        for (Map.Entry<Long, List<DoctorOfficeAvailability>> entry : officeSlotsMap.entrySet()) {
            long officeId = entry.getKey();
            List<DoctorOfficeAvailability> officeSlots = entry.getValue();
            responseMap.put(String.valueOf(officeId), officeSlots);
        }

        return JsonUtils.toJson(responseMap, DoctorOfficeAvailability.Views.Public.class);
    }

    @Transactional(readOnly = true)
    @Override
    public List<DoctorOfficeAvailability> getByDoctorId(long doctorId) {
        LOGGER.debug("Retrieving availability slots for doctor with id: {}", doctorId);
        return doctorOfficeAvailabilityDao.getByDoctorId(doctorId);
    }

    @Transactional(readOnly = true)
    @Override
    public boolean isAvailableAtDayAndTime(long officeId, LocalDate date, Integer hour) {
        LOGGER.debug("Checking availability for office with id: {}, date: {}, startTime: {}", officeId, date, hour);
        List<DoctorOfficeAvailability> slots = doctorOfficeAvailabilityDao.getActiveByOfficeId(officeId);
        return slots.stream().anyMatch(slot -> slot.getDayOfWeek() == (date.getDayOfWeek().getValue() -1) &&
               slot.getStartTime().getHour() <= hour &&
               slot.getEndTime().getHour() > hour);
    }
}
