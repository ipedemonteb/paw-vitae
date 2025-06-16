package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorOfficeAvailabilityDao;
import ar.edu.itba.paw.models.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalTime;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DoctorOfficeAvailabilityServiceImplTest {

    private static final String expectedJson = "{\"1\":[{\"endTime\":9,\"startTime\":12,\"dayOfWeek\":1},{\"endTime\":13,\"startTime\":17,\"dayOfWeek\":1}]}";

    @Mock
    private DoctorOfficeAvailabilityDao doctorOfficeAvailabilityDao;
    @InjectMocks
    private DoctorOfficeAvailabilityServiceImpl doctorOfficeAvailabilityService;

    //@TODO: Should i test update? how?

    @Test
    public void testGetJsonByDoctorIdEmptySlot() {
        //Preconditions
        when(doctorOfficeAvailabilityDao.getByDoctorId(anyLong())).thenReturn(List.of());

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
        when(doctorOfficeAvailabilityDao.getByDoctorId(anyLong())).thenReturn(
                List.of(
                        new DoctorOfficeAvailability(office, LocalTime.of(9, 0), LocalTime.of(12, 0), 1),
                        new DoctorOfficeAvailability(office, LocalTime.of(13, 0), LocalTime.of(17, 0), 1)
                )
        );

        //Exercise
        String result = doctorOfficeAvailabilityService.getJsonByDoctorId(doctorId);

        //Postconditions
        System.out.println(result);
        assertEquals(expectedJson, result);
    }
}
