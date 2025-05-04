package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AvailabilitySlotsDao;

import ar.edu.itba.paw.interfaceServices.AvailabilitySlotsService;
import ar.edu.itba.paw.models.AvailabilitySlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Service
public class AvailabilitySlotServiceImpl implements AvailabilitySlotsService {

    private final AvailabilitySlotsDao availabilitySlotsDao;

    @Autowired
    public AvailabilitySlotServiceImpl(final AvailabilitySlotsDao availabilitySlotsDao) {
        this.availabilitySlotsDao = availabilitySlotsDao;
    }
    @Transactional
    @Override
    public AvailabilitySlot create(long docId, AvailabilitySlot slot) {
      return  availabilitySlotsDao.create(docId, slot);
    }
    @Transactional
    @Override
    public List<AvailabilitySlot> create(long docId, List<AvailabilitySlot> slots) {
        List<AvailabilitySlot> returnSlot = new ArrayList<>();
        for (AvailabilitySlot slot : slots) {
            returnSlot.add(create(docId, slot));
        }
        return returnSlot;
    }
    @Transactional
    @Override
    public void updateDoctorAvailability(long id, List<AvailabilitySlot> availabilitySlots) {
        availabilitySlotsDao.updateDoctorAvailability(id, availabilitySlots);
    }
    @Transactional(readOnly = true)
    @Override
    public List<AvailabilitySlot> getAvailabilityByDoctorId(long doctorId) {
        return availabilitySlotsDao.getAvailabilityByDoctorId(doctorId);
    }


}
