package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorOfficeAvailabilityDao;
import ar.edu.itba.paw.interfaceServices.DoctorOfficeAvailabilityService;
import ar.edu.itba.paw.models.DoctorOffice;
import ar.edu.itba.paw.models.DoctorOfficeAvailabilitySlot;
import ar.edu.itba.paw.models.DoctorOfficeAvailabilitySlotForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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
}
