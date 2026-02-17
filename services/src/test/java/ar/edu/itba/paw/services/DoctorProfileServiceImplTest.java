package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorProfileDao;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorProfile;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DoctorProfileServiceImplTest {

    private static final long DOC_ID = 1L;
    private static final Doctor DOCTOR = new Doctor("Jane", "Smith", "jane@test.com", "hashedpassword", "987654321", "es",
             4.5, 10, true);
    private static final String BIO = "biography";
    private static final String DESC = "description";
    private static final DoctorProfile PROFILE = new DoctorProfile(DOCTOR, BIO, DESC);

    @Mock
    private DoctorService doctorService;
    @Mock
    private DoctorProfileDao doctorProfileDao;

    @InjectMocks
    private DoctorProfileServiceImpl doctorProfileService;

    @Test
    public void testCreateInvalidDoctor() {
        //Preconditions

        //Exercise & Postconditions
        assertThrows(UserNotFoundException.class, () ->
                doctorProfileService.update(DOCTOR, BIO, DESC)
        );
    }

    @Test
    public void testCreate() {
        //Preconditions
        when(doctorService.getById(anyLong())).thenReturn(Optional.of(DOCTOR));
        when(doctorProfileDao.create(any(), anyString(), anyString())).thenReturn(PROFILE);

        //Exercise
        DoctorProfile doctorProfile = doctorProfileService.create(DOC_ID, BIO, DESC);

        //Postconditions
        assertEquals(PROFILE.getBio(), doctorProfile.getBio());
        assertEquals(PROFILE.getDescription(), doctorProfile.getDescription());
    }
}
