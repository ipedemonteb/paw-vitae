package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AvailabilitySlotsDao;
import ar.edu.itba.paw.interfaceServices.DoctorOfficeAvailabilityService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AvailabilitySlotsServiceImplTest {

    @Mock
    private AvailabilitySlotsDao availabilitySlotsDao;
    @Mock
    private DoctorOfficeAvailabilityService doctorOfficeAvailabilityService;
    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private AvailabilitySlotsServiceImpl availabilitySlotsService;

    @Test
    public void testReloadAvailabilityNoDoctor() {
        //Preconditions
        when(doctorService.getById(anyLong())).thenReturn(Optional.empty());

        //Exercise & Postconditions
        assertThrows(IllegalArgumentException.class, () ->
                availabilitySlotsService.reloadAvailability(1L)
        );
    }

}
