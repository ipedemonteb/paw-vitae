package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorOfficeAvailabilityDao;
import ar.edu.itba.paw.interfaceServices.DoctorOfficeService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.ResourceOwnershipException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalTime;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DoctorOfficeAvailabilityServiceImplTest {

    private static final String expectedJson = "{\"1\":[{\"endTime\":9,\"startTime\":12,\"dayOfWeek\":1},{\"endTime\":13,\"startTime\":17,\"dayOfWeek\":1}]}";

    @Mock
    private DoctorOfficeService doctorOfficeService;
    @Mock
    private DoctorOfficeAvailabilityDao doctorOfficeAvailabilityDao;
    @InjectMocks
    private DoctorOfficeAvailabilityServiceImpl doctorOfficeAvailabilityService;

    @Test
    public void testGetJsonByDoctorIdEmptySlot() {
        //Preconditions

        //Exercise
        String result = doctorOfficeAvailabilityService.getJsonByDoctorId(1000L);

        //Postconditions
        assertEquals("[]", result);
    }

    @Test
    public void testGetJsonByDoctorId() {
        //Preconditions
        long doctorId = 2L;
        DoctorOffice office = mock(DoctorOffice.class);
        when(office.getId()).thenReturn(1L);
        when(doctorOfficeAvailabilityDao.getActiveByDoctorId(anyLong())).thenReturn(
                List.of(
                        new DoctorOfficeAvailability(office, LocalTime.of(9, 0), LocalTime.of(12, 0), 1),
                        new DoctorOfficeAvailability(office, LocalTime.of(13, 0), LocalTime.of(17, 0), 1)
                )
        );

        //Exercise
        String result = doctorOfficeAvailabilityService.getJsonByDoctorId(doctorId);

        //Postconditions
        assertNotNull(result);
        assertEquals(expectedJson, result);
    }

    @Test
    public void testUpdateNoResourceOwnership() {
        //Preconditions
        long doctorId = 2L;

        DoctorOffice office1 = mock(DoctorOffice.class);
        when(office1.getId()).thenReturn(1L);

        DoctorOffice office2 = mock(DoctorOffice.class);
        when(office2.getId()).thenReturn(2L);

        when(doctorOfficeService.getAllByDoctorIdWithAvailability(doctorId))
                .thenReturn(List.of(office1, office2));

        DoctorOfficeAvailabilityForm badForm = mock(DoctorOfficeAvailabilityForm.class);
        when(badForm.getOfficeId()).thenReturn(999L);

        List<DoctorOfficeAvailabilityForm> forms = List.of(badForm);

        //Exercise & Postconditions
        assertThrows(ResourceOwnershipException.class, () ->
                doctorOfficeAvailabilityService.update(forms, doctorId)
        );
    }
}
