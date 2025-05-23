package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AvailabilitySlotsDao;

import ar.edu.itba.paw.interfaceServices.AvailabilitySlotsService;
import ar.edu.itba.paw.models.AvailabilitySlot;
import ar.edu.itba.paw.models.AvailabilitySlotForm;
import ar.edu.itba.paw.models.Doctor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class AvailabilitySlotServiceImpl implements AvailabilitySlotsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AvailabilitySlotServiceImpl.class);
    private final AvailabilitySlotsDao availabilitySlotsDao;

    @Autowired
    public AvailabilitySlotServiceImpl(final AvailabilitySlotsDao availabilitySlotsDao) {
        this.availabilitySlotsDao = availabilitySlotsDao;
    }

    @Transactional
    @Override
    public AvailabilitySlot create( AvailabilitySlot slot) {
        LOGGER.debug("Creating availability slot for doctor : {}",  slot);
        AvailabilitySlot toReturn = availabilitySlotsDao.create( slot);
        LOGGER.info("AvailabilitySlot created: {}", toReturn);
        return toReturn;
    }


    @Transactional
    @Override
    public List<AvailabilitySlot> create( List<AvailabilitySlot> slots) {
        List<AvailabilitySlot> returnSlot = new ArrayList<>();
        for (AvailabilitySlot slot : slots) {
            returnSlot.add(create( slot));
        }
        LOGGER.debug("Creating {} availability slots for doctor ", slots.size());
        return returnSlot;
    }

    @Transactional
    @Override
    public void updateDoctorAvailability(Doctor doctor, List<AvailabilitySlotForm> availabilitySlots) {
        LOGGER.debug("Updating availability for doctor {}: {} slots", doctor.getId(), availabilitySlots.size());
        List<AvailabilitySlotForm> filteredSlots = availabilitySlots.stream().filter(slot -> slot.getStartTime() != null && slot.getEndTime() != null).toList();
        availabilitySlotsDao.updateDoctorAvailability(doctor.getId(), transformToAvailabilitySlots(doctor,filteredSlots));
        LOGGER.debug("Updating availability for doctor {}: {} slots", doctor.getId(), filteredSlots.size());

    }

    @Transactional(readOnly = true)
    @Override
    public boolean isAvailableAtDateAndTime(long doctorId, LocalDate date, int time) {
        LOGGER.debug("Checking availability for doctor {} at date {} and time {}", doctorId, date, time);
        List<AvailabilitySlot> slots = getAvailabilityByDoctorId(doctorId);
        return slots.stream().anyMatch(slot -> slot.getDayOfWeek() == (date.getDayOfWeek().getValue() - 1) && slot.getStartTime().getHour() <= time && slot.getEndTime().getHour() >= time);
    }

    @Transactional(readOnly = true)
    @Override
    public List<AvailabilitySlot> getAvailabilityByDoctorId(long doctorId) {
        LOGGER.debug("Getting availability slots for doctor {}", doctorId);
        return availabilitySlotsDao.getAvailabilityByDoctorId(doctorId);
    }
    @Transactional
    @Override
    public List<AvailabilitySlotForm> getDoctorAvailabilitySlots(Doctor doctor) {
        List<AvailabilitySlot> slots = doctor.getAvailabilitySlots();
        List<AvailabilitySlotForm> availabilitySlots = new ArrayList<>();
        for (AvailabilitySlot slot : slots) {
            AvailabilitySlotForm form = new AvailabilitySlotForm(slot.getDayOfWeek(), slot.getStartTime(), slot.getEndTime());
            availabilitySlots.add(form);
        }
        return availabilitySlots.stream().sorted(Comparator.comparingInt(AvailabilitySlotForm::getDayOfWeek)
                        .thenComparing(AvailabilitySlotForm::getStartTime))
                .toList();
    }
    @Transactional
    @Override
    public List<AvailabilitySlot> transformToAvailabilitySlots(Doctor doctor,List<AvailabilitySlotForm> availabilitySlots) {
        List<AvailabilitySlot> slots = new ArrayList<>();
        for (AvailabilitySlotForm slot : availabilitySlots) {
            AvailabilitySlot availabilitySlot = slot.toEntity(doctor);
            slots.add(availabilitySlot);
        }
        return slots;
    }
    @Transactional
    @Override
    public List<AvailabilitySlotForm> transformToAvailabilitySlotForms(List<AvailabilitySlot> availabilitySlots) {
        List<AvailabilitySlotForm> slots = new ArrayList<>();
        for (AvailabilitySlot slot : availabilitySlots) {
            AvailabilitySlotForm availabilitySlot = AvailabilitySlotForm.fromEntity(slot);
            slots.add(availabilitySlot);
        }
        return slots;
    }

}
