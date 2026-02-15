package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.UnavailabilitySlotsDao;
import ar.edu.itba.paw.interfaceServices.UnavailabilitySlotsService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.BussinesRuleException;
import ar.edu.itba.paw.models.exception.ResourceOwnershipException;
import ar.edu.itba.paw.models.exception.UnavailabilitySlotNotFoundException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
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
    public UnavailabilitySlot create(Doctor doctor, UnavailabilitySlotForm form) {
        LOGGER.debug("Creating unavailability slot for doctor {}", doctor.getId());

        if ( form.getEndDate() == null || form.getStartDate() == null || form.getStartDate().isAfter(form.getEndDate()) || form.getStartDate().isBefore(LocalDate.now())) {
            throw new BussinesRuleException("exception.unavailable.invalidRange");
        }
        boolean overlaps = unavailabilitySlotsDao.hasOverlap(doctor.getId(), form.getStartDate(), form.getEndDate());
        if (overlaps) {
            throw new BussinesRuleException("exception.unavailable.overlap");
        }

        UnavailabilitySlot slot = form.toEntity(doctor);
        return unavailabilitySlotsDao.create(slot);
    }


    @Transactional
    @Override
    public void deleteDoctorUnavailability(long doctorId, long unavailabilitySlotId) {
        UnavailabilitySlot slot = unavailabilitySlotsDao.getById(unavailabilitySlotId)
                .orElseThrow(UnavailabilitySlotNotFoundException::new);
        if (slot.getDoctor().getId() != doctorId) {
            throw new ResourceOwnershipException();
        }
        unavailabilitySlotsDao.delete(slot);
    }
    @Transactional(readOnly = true)
    @Override
    public Page<UnavailabilitySlot> getUnavailabilityByDoctorId(long doctorId, String from, String to, int page, int pageSize) {
        LOGGER.debug("Getting unavailability slots for doctor {}", doctorId);
        if(from != null && to != null){
            LocalDate fromDate = LocalDate.parse(from);
            LocalDate toDate = LocalDate.parse(to);
            if (fromDate.isAfter(toDate) || fromDate.datesUntil(toDate.plusDays(1)).count() > 31) {
                throw new BussinesRuleException("exception.occupiedSlots.invalidDateRange");
            }
            List<UnavailabilitySlot> list = unavailabilitySlotsDao.getUnavailabilityByDoctorIdInDateRange(doctorId, fromDate, toDate);
            return new Page<>(list, 1, list.size(), list.size());
        }
        List<UnavailabilitySlot> pagedSlots = unavailabilitySlotsDao.getUnavailabilityByDoctorIdPaginated(doctorId, page, pageSize);
        int totalSlots = unavailabilitySlotsDao.countUnavailabilityByDoctorId(doctorId);
        return new Page<>(pagedSlots, page, pageSize, totalSlots);
    }

}
