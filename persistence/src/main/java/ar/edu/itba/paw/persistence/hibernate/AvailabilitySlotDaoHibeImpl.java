package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.AvailabilitySlotsDao;
import ar.edu.itba.paw.models.AvailabilitySlot;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AvailabilitySlotDaoHibeImpl implements AvailabilitySlotsDao {
    @Override
    public AvailabilitySlot create(AvailabilitySlot slot) {
        return null;
    }

    @Override
    public void updateDoctorAvailability(long id, List<AvailabilitySlot> availabilitySlots) {

    }

    @Override
    public List<AvailabilitySlot> getAvailabilityByDoctorId(long doctorId) {
        return List.of();
    }
}
