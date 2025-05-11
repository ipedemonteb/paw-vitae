package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.interfacePersistence.PatientDao;
import ar.edu.itba.paw.interfacePersistence.UserDao;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.MailService;
import ar.edu.itba.paw.interfaceServices.PatientService;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final Doctor DOCTOR = new Doctor("John", 2L, "Doe",
            "john@test.com", "hashedpassword", "123456789",
            "es", 1L, true
    );
    private static final Patient PATIENT = new Patient("Jane", 1L, "Smith",
            "jane@test.com", "hashedpassword", "987654321", "es",
            new Coverage(1L, "OSDE"), true
    );

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserDao userDao;
    @Mock
    private MailService mailService;
    @Mock
    private PatientDao patientDao;
    @Mock
    private PatientService patientService;
    @Mock
    private DoctorDao doctorDao;
    @Mock
    private DoctorService doctorService;

    @InjectMocks
    private UserServiceImpl userService;

    //@TODO: CHECK LEGALITY OF THIS
    @Test
    public void testGetByEmailReturnsPatient() {
        //Preconditions
        when(patientDao.getByEmail("test@example.com")).thenReturn(Optional.of(PATIENT));
        Optional<? extends User> result = Optional.empty();

        //Exercise
        try {
            result = userService.getByEmail("test@example.com");
        } catch (Exception e) {
            fail("Unexpected error during operation get user by email: " + e.getMessage());
        }

        //Postconditions
        assertTrue(result.isPresent());
        assertEquals(PATIENT.getClass(), result.get().getClass());
    }

    //@TODO: CHECK LEGALITY OF THIS
    @Test
    public void testGetByEmailReturnsDoctorIfPatientNotFound() {
        //Preconditions
        when(patientDao.getByEmail("test@example.com")).thenReturn(Optional.empty());
        when(doctorDao.getByEmail("test@example.com")).thenReturn(Optional.of(DOCTOR));
        Optional<? extends User> result = Optional.empty();

        //Exercise
        try {
            result = userService.getByEmail("test@example.com");
        } catch (Exception e) {
            fail("Unexpected error during operation get user by email:" + e.getMessage());
        }

        //Postconditions
        assertTrue(result.isPresent());
        assertEquals(DOCTOR.getClass(), result.get().getClass());
    }

    @Test
    public void testGetByEmailNotValidId() {
        //Preconditions
        when(patientDao.getByEmail("test@example.com")).thenReturn(Optional.empty());
        when(doctorDao.getByEmail("test@example.com")).thenReturn(Optional.empty());
        Optional<? extends User> result = Optional.empty();

        //Exercise
        try {
            result = userService.getByEmail("test@example.com");
        } catch (Exception e) {
            fail("Unexpected error during operation get user by email:" + e.getMessage());
        }

        //Postconditions
        assertFalse(result.isPresent());
    }

    //@TODO: CHECK LEGALITY OF THIS
    @Test
    public void testGetByIdReturnsPatient() {
        //Preconditions
        when(patientDao.getById(1L)).thenReturn(Optional.of(PATIENT));
        Optional<? extends User> result = Optional.empty();

        //Exercise
        try {
            result = userService.getById(1L);
        } catch (Exception e) {
            fail("Unexpected error during operation get user by email: " + e.getMessage());
        }

        //Postconditions
        assertTrue(result.isPresent());
        assertEquals(PATIENT.getClass(), result.get().getClass());
    }

    //@TODO: CHECK LEGALITY OF THIS
    @Test
    public void testGetByIdReturnsDoctorIfPatientNotFound() {
        //Preconditions
        when(patientDao.getById(1L)).thenReturn(Optional.empty());
        when(doctorDao.getById(1L)).thenReturn(Optional.of(DOCTOR));
        Optional<? extends User> result = Optional.empty();

        //Exercise
        try {
            result = userService.getById(1L);
        } catch (Exception e) {
            fail("Unexpected error during operation get user by email:" + e.getMessage());
        }

        //Postconditions
        assertTrue(result.isPresent());
        assertEquals(DOCTOR.getClass(), result.get().getClass());
    }

    @Test
    public void testGetByIdNotValidId() {
        //Preconditions
        when(patientDao.getById(1L)).thenReturn(Optional.empty());
        when(doctorDao.getById(1L)).thenReturn(Optional.empty());
        Optional<? extends User> result = Optional.empty();

        //Exercise
        try {
            result = userService.getById(1L);
        } catch (Exception e) {
            fail("Unexpected error during operation get user by email:" + e.getMessage());
        }

        //Postconditions
        assertFalse(result.isPresent());
    }

    //@TODO: CHECK LEGALITY OF THIS
    @Test
    public void testGetByResetTokenReturnsPatient() {
        //Preconditions
        when(patientDao.getByResetToken("RESETTOKEN")).thenReturn(Optional.of(PATIENT));
        Optional<? extends User> result = Optional.empty();

        //Exercise
        try {
            result = userService.getByResetToken("RESETTOKEN");
        } catch (Exception e) {
            fail("Unexpected error during operation get user by email: " + e.getMessage());
        }

        //Postconditions
        assertTrue(result.isPresent());
        assertEquals(PATIENT.getClass(), result.get().getClass());
    }

    //@TODO: CHECK LEGALITY OF THIS
    @Test
    public void testGetByResetTokenReturnsDoctorIfPatientNotFound() {
        //Preconditions
        when(patientDao.getByResetToken("RESETTOKEN")).thenReturn(Optional.empty());
        when(doctorDao.getByResetToken("RESETTOKEN")).thenReturn(Optional.of(DOCTOR));
        Optional<? extends User> result = Optional.empty();

        //Exercise
        try {
            result = userService.getByResetToken("RESETTOKEN");
        } catch (Exception e) {
            fail("Unexpected error during operation get user by email:" + e.getMessage());
        }

        //Postconditions
        assertTrue(result.isPresent());
        assertEquals(DOCTOR.getClass(), result.get().getClass());
    }

    @Test
    public void testGetByResetTokenNotValid() {
        //Preconditions
        when(patientDao.getByResetToken("RESETTOKEN")).thenReturn(Optional.empty());
        when(doctorDao.getByResetToken("RESETTOKEN")).thenReturn(Optional.empty());
        Optional<? extends User> result = Optional.empty();

        //Exercise
        try {
            result = userService.getByResetToken("RESETTOKEN");
        } catch (Exception e) {
            fail("Unexpected error during operation get user by email:" + e.getMessage());
        }

        //Postconditions
        assertFalse(result.isPresent());
    }

    //@TODO: CHECK LEGALITY OF THIS
    @Test
    public void testVerifyValidationTokenReturnsPatient() {
        //Preconditions
        when(patientDao.getByVerificationToken("VALIDATIONTOKEN")).thenReturn(Optional.of(PATIENT));
        Optional<? extends User> result = Optional.empty();

        //Exercise
        try {
            result = userService.verifyValidationToken("VALIDATIONTOKEN");
        } catch (Exception e) {
            fail("Unexpected error during operation get user by email: " + e.getMessage());
        }

        //Postconditions
        assertTrue(result.isPresent());
        assertEquals(PATIENT.getClass(), result.get().getClass());
    }

    //@TODO: CHECK LEGALITY OF THIS
    @Test
    public void testVerifyValidationTokenReturnsDoctor() {
        //Preconditions
        when(patientDao.getByVerificationToken("VALIDATIONTOKEN")).thenReturn(Optional.empty());
        when(doctorDao.getByVerificationToken("VALIDATIONTOKEN")).thenReturn(Optional.of(DOCTOR));
        Optional<? extends User> result = Optional.empty();

        //Exercise
        try {
            result = userService.verifyValidationToken("VALIDATIONTOKEN");
        } catch (Exception e) {
            fail("Unexpected error during operation get user by email:" + e.getMessage());
        }

        //Postconditions
        assertTrue(result.isPresent());
        assertEquals(DOCTOR.getClass(), result.get().getClass());
    }

    @Test
    public void testVerifyValidationTokenNotValid() {
        //Preconditions
        when(patientDao.getByVerificationToken("VALIDATIONTOKEN")).thenReturn(Optional.empty());
        when(doctorDao.getByVerificationToken("VALIDATIONTOKEN")).thenReturn(Optional.empty());
        Optional<? extends User> result = Optional.empty();

        //Exercise
        try {
            result = userService.verifyValidationToken("VALIDATIONTOKEN");
        } catch (Exception e) {
            fail("Unexpected error during operation get user by email:" + e.getMessage());
        }

        //Postconditions
        assertFalse(result.isPresent());
    }

    @Test
    public void testVerifyRecoveryTokenExists() {
        //Preconditions
        when(patientDao.getByResetToken("RECOVERYTOKEN")).thenReturn(Optional.of(PATIENT));
        boolean result = false;

        //Exercise
        try {
            result = userService.verifyRecoveryToken("RECOVERYTOKEN");
        } catch (Exception e) {
            fail("Unexpected error during operation get user by email:" + e.getMessage());
        }

        //Postconditions
        assertTrue(result);
    }

    @Test
    public void testVerifyRecoveryTokenDoesNotExist() {
        //Preconditions
        when(patientDao.getByResetToken("RECOVERYTOKEN")).thenReturn(Optional.empty());
        when(doctorDao.getByResetToken("RECOVERYTOKEN")).thenReturn(Optional.empty());
        boolean result = false;

        //Exercise
        try {
            result = userService.verifyRecoveryToken("RECOVERYTOKEN");
        } catch (Exception e) {
            fail("Unexpected error during operation get user by email:" + e.getMessage());
        }

        //Postconditions
        assertFalse(result);
    }

    //@TODO: CHECK HOW TO TEST CAUSE IT CALLS ANOTHER FUNCTION
    @Test
    public void testChangePassword() {
        //Preconditions
    }

    @Test
    public void testGetImageIdIsNotDoctor() {
        //Preconditions
        Long result = 0L;

        //Exercise
        try {
            result = userService.getImageId(null);
        } catch (Exception e) {
            fail("Unexpected error during operation get user by email: " + e.getMessage());
        }

        //Postconditions
        assertEquals(-1L, result.longValue());
    }

    //@TODO: CHECK CAUSE IT DOESNT USE DB
    @Test
    public void testGetImageIdIsDoctor() {
        //Preconditions
        Long result = 0L;

        //Exercise
        try {
            result = userService.getImageId(DOCTOR);
        } catch (Exception e) {
            fail("Unexpected error during operation get user by email: " + e.getMessage());
        }

        //Postconditions
        assertEquals(1L, result.longValue());
    }
}
