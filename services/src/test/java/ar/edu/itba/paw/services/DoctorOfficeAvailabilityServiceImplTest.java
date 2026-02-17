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


    @Mock
    private DoctorOfficeService doctorOfficeService;

    @InjectMocks
    private DoctorOfficeAvailabilityServiceImpl doctorOfficeAvailabilityService;



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
