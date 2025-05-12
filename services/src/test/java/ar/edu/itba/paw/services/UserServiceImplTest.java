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

import java.time.LocalDateTime;
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

    @Test
    public void testGetByEmailReturnsPatient() {
        //Preconditions
        when(patientDao.getByEmail("test@example.com")).thenReturn(Optional.of(PATIENT));

        //Exercise
        Optional<? extends User> result = userService.getByEmail("test@example.com");

        //Postconditions
        assertTrue(result.isPresent());
        assertEquals(PATIENT.getClass(), result.get().getClass());
    }

    @Test
    public void testGetByEmailReturnsDoctorIfPatientNotFound() {
        //Preconditions
        when(patientDao.getByEmail("test@example.com")).thenReturn(Optional.empty());
        when(doctorDao.getByEmail("test@example.com")).thenReturn(Optional.of(DOCTOR));

        //Exercise
        Optional<? extends User> result = userService.getByEmail("test@example.com");

        //Postconditions
        assertTrue(result.isPresent());
        assertEquals(DOCTOR.getClass(), result.get().getClass());
    }

    @Test
    public void testGetByEmailNotValidId() {
        //Preconditions
        when(patientDao.getByEmail("test@example.com")).thenReturn(Optional.empty());
        when(doctorDao.getByEmail("test@example.com")).thenReturn(Optional.empty());

        //Exercise
        Optional<? extends User> result = userService.getByEmail("test@example.com");

        //Postconditions
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetByIdReturnsPatient() {
        //Preconditions
        when(patientDao.getById(1L)).thenReturn(Optional.of(PATIENT));

        //Exercise
        Optional<? extends User> result = userService.getById(1L);

        //Postconditions
        assertTrue(result.isPresent());
        assertEquals(PATIENT.getClass(), result.get().getClass());
    }

    @Test
    public void testGetByIdReturnsDoctorIfPatientNotFound() {
        //Preconditions
        when(patientDao.getById(1L)).thenReturn(Optional.empty());
        when(doctorDao.getById(1L)).thenReturn(Optional.of(DOCTOR));

        //Exercise
        Optional<? extends User> result = userService.getById(1L);

        //Postconditions
        assertTrue(result.isPresent());
        assertEquals(DOCTOR.getClass(), result.get().getClass());
    }

    @Test
    public void testGetByIdNotValidId() {
        //Preconditions
        when(patientDao.getById(1L)).thenReturn(Optional.empty());
        when(doctorDao.getById(1L)).thenReturn(Optional.empty());

        //Exercise
        Optional<? extends User> result = userService.getById(1L);

        //Postconditions
        assertFalse(result.isPresent());
    }

    @Test
    public void testGetByResetTokenReturnsPatient() {
        //Preconditions
        when(patientDao.getByResetToken("RESETTOKEN")).thenReturn(Optional.of(PATIENT));

        //Exercise
        Optional<? extends User> result = userService.getByResetToken("RESETTOKEN");

        //Postconditions
        assertTrue(result.isPresent());
        assertEquals(PATIENT.getClass(), result.get().getClass());
    }

    @Test
    public void testGetByResetTokenReturnsDoctorIfPatientNotFound() {
        //Preconditions
        when(patientDao.getByResetToken("RESETTOKEN")).thenReturn(Optional.empty());
        when(doctorDao.getByResetToken("RESETTOKEN")).thenReturn(Optional.of(DOCTOR));

        //Exercise
        Optional<? extends User> result = userService.getByResetToken("RESETTOKEN");

        //Postconditions
        assertTrue(result.isPresent());
        assertEquals(DOCTOR.getClass(), result.get().getClass());
    }

    @Test
    public void testGetByResetTokenNotValid() {
        //Preconditions
        when(patientDao.getByResetToken("RESETTOKEN")).thenReturn(Optional.empty());
        when(doctorDao.getByResetToken("RESETTOKEN")).thenReturn(Optional.empty());

        //Exercise
        Optional<? extends User> result = userService.getByResetToken("RESETTOKEN");

        //Postconditions
        assertFalse(result.isPresent());
    }

    @Test
    public void testVerifyValidationTokenReturnsPatient() {
        //Preconditions
        when(userDao.tokenExpirationDate("VALIDATIONTOKEN")).thenReturn(LocalDateTime.now().plusDays(1));
        when(patientDao.getByVerificationToken("VALIDATIONTOKEN")).thenReturn(Optional.of(PATIENT));

        //Exercise
        Optional<? extends User> result = userService.verifyValidationToken("VALIDATIONTOKEN");


        //Postconditions
        assertTrue(result.isPresent());
        assertEquals(PATIENT.getClass(), result.get().getClass());
    }

    @Test
    public void testVerifyValidationTokenReturnsPatientExpiredToken() {
        //Preconditions
        when(userDao.tokenExpirationDate("VALIDATIONTOKEN")).thenReturn(LocalDateTime.now().minusDays(1));
        when(patientDao.getByVerificationToken("VALIDATIONTOKEN")).thenReturn(Optional.of(PATIENT));
        when(patientDao.getByEmail("jane@test.com")).thenReturn(Optional.of(PATIENT));

        //Exercise
        Optional<? extends User> result = userService.verifyValidationToken("VALIDATIONTOKEN");

        //Postconditions
        assertTrue(result.isPresent());
        assertEquals(PATIENT.getClass(), result.get().getClass());
    }

    @Test
    public void testVerifyValidationTokenReturnsDoctor() {
        //Preconditions
        when(userDao.tokenExpirationDate("VALIDATIONTOKEN")).thenReturn(LocalDateTime.now().plusDays(1));
        when(patientDao.getByVerificationToken("VALIDATIONTOKEN")).thenReturn(Optional.empty());
        when(doctorDao.getByVerificationToken("VALIDATIONTOKEN")).thenReturn(Optional.of(DOCTOR));

        //Exercise
        Optional<? extends User> result = userService.verifyValidationToken("VALIDATIONTOKEN");

        //Postconditions
        assertTrue(result.isPresent());
        assertEquals(DOCTOR.getClass(), result.get().getClass());
    }

    @Test
    public void testVerifyValidationTokenNotValid() {
        //Preconditions
        when(patientDao.getByVerificationToken("VALIDATIONTOKEN")).thenReturn(Optional.empty());
        when(doctorDao.getByVerificationToken("VALIDATIONTOKEN")).thenReturn(Optional.empty());

        //Exercise
        Optional<? extends User> result = userService.verifyValidationToken("VALIDATIONTOKEN");

        //Postconditions
        assertFalse(result.isPresent());
    }

    @Test
    public void testVerifyRecoveryTokenExists() {
        //Preconditions
        when(userDao.tokenExpirationDate("RECOVERYTOKEN")).thenReturn(LocalDateTime.now().plusDays(1));
        when(patientDao.getByResetToken("RECOVERYTOKEN")).thenReturn(Optional.of(PATIENT));

        //Exercise
        boolean result = userService.verifyRecoveryToken("RECOVERYTOKEN");

        //Postconditions
        assertTrue(result);
    }

    @Test
    public void testVerifyRecoveryTokenDoesNotExist() {
        //Preconditions
        when(patientDao.getByResetToken("RECOVERYTOKEN")).thenReturn(Optional.empty());
        when(doctorDao.getByResetToken("RECOVERYTOKEN")).thenReturn(Optional.empty());

        //Exercise
        boolean result = userService.verifyRecoveryToken("RECOVERYTOKEN");

        //Postconditions
        assertFalse(result);
    }

    @Test
    public void testChangePasswordInvalidUser() {
        //Preconditions

        //Exercise
        boolean result = userService.changePassword("RECOVERYTOKEN", "newpassword");

        //Postconditions
        assertFalse(result);
    }

    @Test
    public void testChangePassword() {
        //Preconditions
        when(patientDao.getByResetToken(anyString())).thenReturn(Optional.of(PATIENT));
        when(passwordEncoder.encode(anyString())).thenReturn("ENCODEDPASSWORD");

        //Exercise
        boolean result = userService.changePassword("RECOVERYTOKEN", "NEWPASSWORD");

        //Postconditions
        assertTrue(result);
    }

    @Test
    public void testGetImageIdIsNotDoctor() {
        //Preconditions

        //Exercise
        Long result = userService.getImageId(null);

        //Postconditions
        assertEquals(-1L, result.longValue());
    }

    @Test
    public void testGetImageIdIsDoctor() {
        //Preconditions

        //Exercise
        Long result = userService.getImageId(DOCTOR);

        //Postconditions
        assertEquals(1L, result.longValue());
    }
}
