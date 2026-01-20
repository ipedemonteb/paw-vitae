package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorOfficeAvailabilityDao;
import ar.edu.itba.paw.interfaceServices.DoctorOfficeAvailabilityService;
import ar.edu.itba.paw.interfaceServices.DoctorOfficeService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.BussinesRuleException;
import ar.edu.itba.paw.models.exception.ResourceOwnershipException;
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
        List<DoctorOffice> offices =
                doctorOfficeService.getAllByDoctorIdWithAvailability(doctorId);

        Set<Long> doctorOfficeIds = offices.stream()
                .map(DoctorOffice::getId)
                .collect(Collectors.toSet());

        Set<Long> incomingOfficeIds = forms.stream()
                .map(DoctorOfficeAvailabilityForm::getOfficeId)
                .collect(Collectors.toSet());
        for (Long incomingId : incomingOfficeIds) {
            if (!doctorOfficeIds.contains(incomingId)) {
                throw new ResourceOwnershipException();
            }
        }
        for (DoctorOffice office : offices) {
            if (office.isActive() && !incomingOfficeIds.contains(office.getId())) {
                office.setActive(false);
//                throw new BussinesRuleException("exception.business.officeAvailabilitySlot.active"); // you muist provide slots for all active offices
            }
        }
        Map<Long,DoctorOfficeAvailability> existingById =
                offices.stream()
                        .flatMap(o -> o.getDoctorOfficeAvailability().stream())
                        .collect(Collectors.toMap(DoctorOfficeAvailability::getId, s -> s));

        Map<Long,List<DoctorOfficeAvailabilityForm>> formsByOffice =
                forms.stream()
                        .collect(Collectors.groupingBy(DoctorOfficeAvailabilityForm::getOfficeId));

        for (DoctorOffice office : offices) {
            List<DoctorOfficeAvailabilityForm> incoming =
                    formsByOffice.getOrDefault(office.getId(), List.of());

            List<DoctorOfficeAvailability> newSlots = new ArrayList<>();
            for (var form : incoming) {
                if (form.getId() != null) {
                    var existing = existingById.get(form.getId());
                    if (existing != null && existing.getOffice().getId().equals(office.getId())) {
                        existing.setStartTime(form.getStartTime());
                        existing.setEndTime(  form.getEndTime());
                        existing.setDayOfWeek(form.getDayOfWeek());
                        newSlots.add(existing);
                    } else {
                        newSlots.add(form.toEntity(office));
                    }
                } else {
                    newSlots.add(form.toEntity(office));
                }
            }
            office.replaceAvailability(newSlots);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public String getJsonByDoctorId(long doctorId) {
        List<DoctorOfficeAvailability> slots = doctorOfficeAvailabilityDao.getActiveByDoctorId(doctorId);
        if (slots.isEmpty()) {
            LOGGER.debug("No availability slots found for doctor with id: {}", doctorId);
            return "[]";
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
