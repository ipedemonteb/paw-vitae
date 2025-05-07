package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AvailabilitySlotsDao;
import ar.edu.itba.paw.models.AvailabilitySlot;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AvailabilitySlotServiceImplTest {

    @Mock
    private AvailabilitySlotsDao availabilitySlotsDao;

    @InjectMocks
    private AvailabilitySlotServiceImpl availabilitySlotService;

    //@TODO: SHOULD I CHECK MORE PARAMS
    @Test
    public void testCreateAvailabilitySlot() {
        //Preconditions
        long doctorId = 1L;
        AvailabilitySlot slot1 = mock(AvailabilitySlot.class);
        AvailabilitySlot slot2 = mock(AvailabilitySlot.class);
        List<AvailabilitySlot> slots = List.of(slot1, slot2);
        when(availabilitySlotsDao.create(doctorId, slot1)).thenReturn(slot1);
        when(availabilitySlotsDao.create(doctorId, slot2)).thenReturn(slot2);

        //Exercise
        List<AvailabilitySlot> created = availabilitySlotService.create(doctorId, slots);

        //Postconditions
        assertEquals(2, created.size());
    }
}
