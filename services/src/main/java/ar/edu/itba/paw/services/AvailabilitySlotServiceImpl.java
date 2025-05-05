package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AvailabilitySlotsDao;

import ar.edu.itba.paw.interfaceServices.AvailabilitySlotsService;
import ar.edu.itba.paw.models.AvailabilitySlot;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
public class AvailabilitySlotServiceImpl implements AvailabilitySlotsService {

    private final AvailabilitySlotsDao availabilitySlotsDao;
    private static final Logger LOGGER = LoggerFactory.getLogger(AvailabilitySlotServiceImpl.class);

    @Autowired
    public AvailabilitySlotServiceImpl(final AvailabilitySlotsDao availabilitySlotsDao) {
        this.availabilitySlotsDao = availabilitySlotsDao;
    }
    @Transactional
    @Override
    public AvailabilitySlot create(long docId, AvailabilitySlot slot) {
        AvailabilitySlot toReturn = availabilitySlotsDao.create(docId, slot);
        LOGGER.info("AvailabilitySlot created: {}", toReturn);
      return  toReturn;
    }
    @Transactional
    @Override
    public List<AvailabilitySlot> create(long docId, List<AvailabilitySlot> slots) {
        List<AvailabilitySlot> returnSlot = new ArrayList<>();
        for (AvailabilitySlot slot : slots) {
            returnSlot.add(create(docId, slot));
        }
        LOGGER.debug("Creating {} availability slots for doctor {}", slots.size(), docId);
        return returnSlot;
    }
    @Transactional
    @Override
    public void updateDoctorAvailability(long id, List<AvailabilitySlot> availabilitySlots) {
        availabilitySlotsDao.updateDoctorAvailability(id, availabilitySlots);
        LOGGER.debug("Updating availability for doctor {}: {} slots", id, availabilitySlots.size());

    }
    @Transactional(readOnly = true)
    @Override
    public List<AvailabilitySlot> getAvailabilityByDoctorId(long doctorId) {
        return availabilitySlotsDao.getAvailabilityByDoctorId(doctorId);
    }


}
