package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.UserDao;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.MailService;
import ar.edu.itba.paw.interfaceServices.PatientService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserDao userDao;
    @Mock
    private MailService mailService;
    @Mock
    private PatientService patientService;
    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    public void testGetByEmailReturnsPatientIfExists() {
        //Preconditions
        Patient patient = mock(Patient.class);
        when(patientService.getByEmail("test@example.com")).thenReturn(Optional.of(patient));
        Optional<? extends User> result = Optional.empty();

        //Exercise
        try {
            result = userService.getByEmail("test@example.com");
        } catch (Exception e) {
            fail("Unexpected error during operation get user by email: " + e.getMessage());
        }

        //Postconditions
        assertTrue(result.isPresent());
        assertSame(patient, result.get());
    }

    @Test
    public void testGetByEmailReturnsDoctorIfPatientNotFound() {
        //Preconditions
        Doctor doctor = mock(Doctor.class);
        when(patientService.getByEmail("test@example.com")).thenReturn(Optional.empty());
        when(doctorService.getByEmail("test@example.com")).thenReturn(Optional.of(doctor));
        Optional<? extends User> result = Optional.empty();

        //Exercise
        try {
            result = userService.getByEmail("test@example.com");
        } catch (Exception e) {
            fail("Unexpected error during operation get user by email:" + e.getMessage());
        }

        //Postconditions
        assertTrue(result.isPresent());
        assertSame(doctor, result.get());
    }

    //@TODO: CHECK HOW TO DO
//    @Test
//    public void testSetVerificationToken() {
//        //Preconditions
//        String user_email = "test@email.com";
//        User user = mock(User.class);
//        when(user.getEmail()).thenReturn(Optional.of());
//
//        //Exercise
//        try {
//            userService.setVerificationToken(user_email);
//        } catch (Exception e) {
//            fail("Unexpected error during setVerificationToken: " + e.getMessage());
//        }
//
//        //Postconditions
//
//    }

    //@TODO: CHECK CAUSE THIS FUNCTION CALLS ANOTHER ONE TESTED INSIDE
    @Test
    public void testVerifyValidationTokenWithPatient() {
        //Preconditions
        final String TOKEN = "validToken";
        final long PATIENT_ID = 5L;
        Patient patient = mock(Patient.class);
        when(patient.getId()).thenReturn(PATIENT_ID);
        when(patientService.getByVerificationToken(TOKEN)).thenReturn(Optional.of(patient));
        Optional<? extends User> result = Optional.empty();

        //Exercise
        try {
            result = userService.verifyValidationToken(TOKEN);
        } catch (Exception e) {
            fail("Unexpected error during verifyValidationToken (patient): " + e.getMessage());
        }

        //Postconditions
        assertTrue(result.isPresent());
        assertEquals(patient.getId(), result.get().getId());
    }

    //@TODO: CHECK SAME ISSUE AS BEFORE
    @Test
    public void testVerifyValidationTokenWithDoctor() {
        final String token = "validToken";
        final long DOCTOR_ID = 8L;
        Doctor doctor = mock(Doctor.class);
        when(doctor.getId()).thenReturn(DOCTOR_ID);
        when(patientService.getByVerificationToken(token)).thenReturn(Optional.empty());
        when(doctorService.getByVerificationToken(token)).thenReturn(Optional.of(doctor));
        when(doctor.getVerificationToken()).thenReturn(token);

        Optional<? extends User> result = Optional.empty();
        try {
            result = userService.verifyValidationToken(token);
        } catch (Exception e) {
            fail("Unexpected error during verifyValidationToken (doctor): " + e.getMessage());
        }

        assertTrue(result.isPresent());
        assertEquals(token, result.get().getVerificationToken());
    }

    @Test
    public void testVerifyValidationTokenInvalid() {
        final String INVALID_TOKEN = "invalid";

        when(patientService.getByVerificationToken(INVALID_TOKEN)).thenReturn(Optional.empty());
        when(doctorService.getByVerificationToken(INVALID_TOKEN)).thenReturn(Optional.empty());

        Optional<? extends User> result = Optional.of(mock(User.class));
        try {
            result = userService.verifyValidationToken(INVALID_TOKEN);
        } catch (Exception e) {
            fail("Unexpected error during verifyValidationToken (invalid): " + e.getMessage());
        }

        assertFalse(result.isPresent());
    }

    //@TODO: CHECK IF THERE ARE MORE FUNCTION WORTH TESTING
}
