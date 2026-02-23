package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.OccupiedSlotsDao;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.OccupiedSlots;
import ar.edu.itba.paw.models.exception.BussinesRuleException;
import ar.edu.itba.paw.models.exception.OccupiedSlotNotFoundException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OccupiedSlotsServiceImplTest {

    private static final Long DOCTOR_ID = 1L;
    private static final Long SLOT_ID = 100L;
    private static final LocalDate DATE = LocalDate.of(2026, 3, 10);
    private static final LocalTime TIME = LocalTime.of(10, 0);

    private static final Doctor DOCTOR = new Doctor("Jane", "Smith", "jane@test.com", "pass", "123", "es", 5.0, 1, true);
    private static final OccupiedSlots SLOT = new OccupiedSlots(DOCTOR, DATE, TIME);

    @Mock
    private OccupiedSlotsDao occupiedSlotsDao;
    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private OccupiedSlotsServiceImpl occupiedSlotsService;

    @Test
    public void testCreateDoctorNotFound() {
        //Preconditions
        when(doctorService.getById(anyLong())).thenReturn(Optional.empty());

        //Exercise & Postconditions
        assertThrows(UserNotFoundException.class, () ->
                occupiedSlotsService.create(DOCTOR_ID, DATE, TIME)
        );
    }

    @Test
    public void testCreate() {
        //Preconditions
        when(doctorService.getById(anyLong())).thenReturn(Optional.of(DOCTOR));
        when(occupiedSlotsDao.create(any(), any(), any())).thenReturn(SLOT);

        //Exercise
        OccupiedSlots createdSlot = occupiedSlotsService.create(DOCTOR_ID, DATE, TIME);

        //Postconditions
        assertNotNull(createdSlot);
        assertEquals(DOCTOR, createdSlot.getDoctor());
        assertEquals(DATE, createdSlot.getSlotDate());
        assertEquals(TIME, createdSlot.getStartTime());
    }

    @Test
    public void testGetById() {
        //Preconditions
        when(occupiedSlotsDao.getById(anyLong())).thenReturn(Optional.of(SLOT));

        //Exercise
        Optional<OccupiedSlots> result = occupiedSlotsService.getById(SLOT_ID);

        //Postconditions
        assertTrue(result.isPresent());
        assertEquals(SLOT, result.get());
    }

    @Test
    public void testGetByDoctorIdInDateRangeInvalidDates() {
        //Preconditions
        LocalDate start = LocalDate.of(2026, 3, 10);
        LocalDate end = LocalDate.of(2026, 3, 5);

        //Exercise & Postconditions
        assertThrows(BussinesRuleException.class, () ->
                occupiedSlotsService.getByDoctorIdInDateRange(DOCTOR_ID, start, end)
        );
    }

    @Test
    public void testGetByDoctorIdInDateRangeTooLong() {
        //Preconditions
        LocalDate start = LocalDate.of(2026, 1, 1);
        LocalDate end = LocalDate.of(2026, 3, 1);

        //Exercise & Postconditions
        assertThrows(BussinesRuleException.class, () ->
                occupiedSlotsService.getByDoctorIdInDateRange(DOCTOR_ID, start, end)
        );
    }

    @Test
    public void testGetByDoctorIdInDateRange() {
        //Preconditions
        LocalDate start = LocalDate.of(2026, 3, 1);
        LocalDate end = LocalDate.of(2026, 3, 5);
        when(occupiedSlotsDao.getByDoctorIdInDateRange(anyLong(), any(), any()))
                .thenReturn(List.of(SLOT));

        //Exercise
        List<OccupiedSlots> list = occupiedSlotsService.getByDoctorIdInDateRange(DOCTOR_ID, start, end);

        //Postconditions
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        assertEquals(SLOT, list.get(0));
    }

    @Test
    public void testDeleteNotFoundInDao() {
        //Preconditions
        when(occupiedSlotsDao.getByDoctorIdInDateRange(anyLong(), any(), any()))
                .thenReturn(Collections.emptyList());

        //Exercise & Postconditions
        assertThrows(OccupiedSlotNotFoundException.class, () ->
                occupiedSlotsService.delete(DOCTOR_ID, DATE, TIME)
        );
    }

    @Test
    public void testDeleteTimeMismatch() {
        //Preconditions
        OccupiedSlots differentTimeSlot = new OccupiedSlots(DOCTOR, DATE, TIME.plusHours(1));

        when(occupiedSlotsDao.getByDoctorIdInDateRange(anyLong(), any(), any()))
                .thenReturn(List.of(differentTimeSlot));

        //Exercise & Postconditions
        assertThrows(OccupiedSlotNotFoundException.class, () ->
                occupiedSlotsService.delete(DOCTOR_ID, DATE, TIME)
        );
    }

    @Test
    public void testDelete() {
        //Preconditions
        OccupiedSlots slotToDelete = new OccupiedSlots(DOCTOR, DATE, TIME);
        slotToDelete.setId(SLOT_ID);

        when(occupiedSlotsDao.getByDoctorIdInDateRange(anyLong(), any(), any()))
                .thenReturn(List.of(slotToDelete));

        //Exercise
        occupiedSlotsService.delete(DOCTOR_ID, DATE, TIME);

        //Postconditions
        verify(occupiedSlotsDao, times(1)).delete(SLOT_ID);
    }
}