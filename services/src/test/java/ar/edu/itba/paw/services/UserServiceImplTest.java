package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.interfacePersistence.PatientDao;
import ar.edu.itba.paw.interfacePersistence.UserDao;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.MailService;
import ar.edu.itba.paw.interfaceServices.PatientService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class UserServiceImplTest {

    private static final String EMAIL = "jane@test.com";
    private static final Doctor DOCTOR = new Doctor("Jane", "Smith", "jane@test.com", "hashedpassword", "987654321", "es",
            1L, 4.5, 10, true);
    private static final Patient PATIENT = new Patient("John", "Doe", "john@test.com", "hashedpassword", "123456789", "en",
            new Coverage(1L, "Coverage A"), new Neighborhood(1L, "Neighborhood A"), true);

    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserDao userDao;
    @Mock
    private MailService mailService;
    @Mock
    private PatientDao patientDao;
    @Mock
    private DoctorDao doctorDao;

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
    public void testChangeLanguageInvalidUser() {
        //Preconditions

        //Exercise & Postconditions
        assertThrows(UserNotFoundException.class, () ->
                userService.changeLanguage(1L, "es")
        );
    }

    @Test
    public void testChangeLanguage() {
        //Preconditions
        Doctor doctor = new Doctor("Jane", "Smith", "jane@test.com", "hashedpassword", "987654321", "es",
                1L, 4.5, 10, true);
        String newLanguage = "en";
        when(doctorDao.getById(anyLong())).thenReturn(Optional.of(doctor));

        //Exercise
        userService.changeLanguage(1L, newLanguage);

        //Postconditions
        assertEquals(newLanguage, doctor.getLanguage());
    }

    @Test
    public void testGetByVerificationTokenReturnsPatient() {
        //Preconditions
        String token = "VERIFTOKEN";
        when(patientDao.getByVerificationToken(token)).thenReturn(Optional.of(PATIENT));

        //Exercise
        Optional<? extends User> result = userService.getByVerificationToken(token);

        //Postconditions
        assertTrue(result.isPresent());
        assertEquals(PATIENT.getClass(), result.get().getClass());
    }

    @Test
    public void testGetByVerificationTokenReturnsDoctorIfPatientNotFound() {
        //Preconditions
        String token = "VERIFTOKEN";
        when(patientDao.getByVerificationToken(token)).thenReturn(Optional.empty());
        when(doctorDao.getByVerificationToken(token)).thenReturn(Optional.of(DOCTOR));

        //Exercise
        Optional<? extends User> result = userService.getByVerificationToken(token);

        //Postconditions
        assertTrue(result.isPresent());
        assertEquals(DOCTOR.getClass(), result.get().getClass());
    }

    @Test
    public void testGetByVerificationTokenNotValid() {
        //Preconditions
        String token = "VERIFTOKEN";
        when(patientDao.getByVerificationToken(token)).thenReturn(Optional.empty());
        when(doctorDao.getByVerificationToken(token)).thenReturn(Optional.empty());

        //Exercise
        Optional<? extends User> result = userService.getByVerificationToken(token);

        //Postconditions
        assertFalse(result.isPresent());
    }

    @Test
    public void testSetVerificationTokenInvalidUser() {
        //Preconditions

        //Exercise & Postconditions
        assertThrows(UserNotFoundException.class, () ->
                userService.setVerificationToken(EMAIL)
        );
    }

    @Test
    public void testSetVerificationToken() {
        //Preconditions
        Doctor doctor = new Doctor("Jane", "Smith", "jane@test.com", "hashedpassword", "987654321", "es",
                1L, 4.5, 10, true);
        when(doctorDao.getByEmail(EMAIL)).thenReturn(Optional.of(doctor));

        //Exercise
        userService.setVerificationToken(EMAIL);

        //Postconditions
        assertNotNull(doctor.getVerificationToken());
    }

    @Test
    public void testSetResetPasswordTokenInvalidUser() {
        //Preconditions
        Doctor doctor = new Doctor("Jane", "Smith", "jane@test.com", "hashedpassword", "987654321", "es",
                1L, 4.5, 10, true);
        String token = "TOKEN";
        doctor.setResetPasswordToken(token);
        when(doctorDao.getByEmail(anyString())).thenReturn(Optional.empty());

        //Exercise
        userService.setResetPasswordToken(EMAIL);

        //Postconditions
        assertEquals(token, doctor.getResetPasswordToken());
    }

    @Test
    public void testSetResetPasswordToken() {
        //Preconditions
        Doctor doctor = new Doctor("Jane", "Smith", "jane@test.com", "hashedpassword", "987654321", "es",
                1L, 4.5, 10, true);
        String token = "TOKEN";
        doctor.setResetPasswordToken(token);
        when(doctorDao.getByEmail(anyString())).thenReturn(Optional.of(doctor));

        //Exercise
        userService.setResetPasswordToken(EMAIL);

        //Postconditions
        assertNotEquals(token, doctor.getResetPasswordToken());
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
    public void testChangePasswordInvalidUser() {
        //Preconditions

        //Exercise & Postconditions
        assertThrows(UserNotFoundException.class, () ->
                userService.changePassword(1L, "NEWPASSWORD")
        );
    }

    @Test
    public void testChangePassword() {
        //Preconditions
        long userId = 1L;
        Doctor doctor = new Doctor("Jane", "Smith", "jane@test.com", "hashedpassword", "987654321", "es",
                1L, 4.5, 10, true);
        when(doctorDao.getById(userId)).thenReturn(Optional.of(doctor));
        String newPassword = "NEWPASSWORD";
        when(passwordEncoder.encode(newPassword)).thenReturn(newPassword);

        //Exercise
        userService.changePassword(1L, newPassword);

        //Postconditions
        assertEquals(newPassword, doctor.getPassword());
    }

    @Test
    public void testCheckTokenInvalidUser() {
        //Preconditions
        String token = "TOKEN";
        boolean isVerification = true;
        when(patientDao.getByVerificationToken(token)).thenReturn(Optional.empty());
        when(doctorDao.getByVerificationToken(token)).thenReturn(Optional.empty());
        //Exercise
        Optional<? extends User> result = userService.checkToken(token, isVerification);

        //Postconditions
        assertFalse(result.isPresent());
    }

    @Test
    public void testCheckTokenValidPatientVerificationExpiredToken() {
        //Preconditions
        String token = "TOKEN";
        boolean isVerification = true;
        when(patientDao.getByVerificationToken(anyString())).thenReturn(Optional.of(PATIENT));
        when(userDao.tokenExpirationDate(anyString())).thenReturn(LocalDateTime.now(ZoneId.systemDefault()).minusDays(1));
        when(patientDao.getByEmail(anyString())).thenReturn(Optional.of(PATIENT));

        //Exercise
        Optional<? extends User> result = userService.checkToken(token, isVerification);

        //Postconditions
        assertFalse(result.isPresent());
    }

    @Test
    public void testCheckTokenValidPatientResetExpiredToken() {
        //Preconditions
        String token = "TOKEN";
        boolean isVerification = false;
        when(patientDao.getByResetToken(token)).thenReturn(Optional.of(PATIENT));
        when(userDao.tokenExpirationDate(token)).thenReturn(LocalDateTime.now(ZoneId.systemDefault()).minusDays(1));

        //Exercise
        Optional<? extends User> result = userService.checkToken(token, isVerification);

        //Postconditions
        assertFalse(result.isPresent());
    }

    @Test
    public void testCheckTokenValidPatientVerification() {
        //Preconditions
        String token = "TOKEN";
        boolean isVerification = true;
        when(patientDao.getByVerificationToken(token)).thenReturn(Optional.of(PATIENT));
        when(userDao.tokenExpirationDate(token)).thenReturn(LocalDateTime.now(ZoneId.systemDefault()).plusDays(2));

        //Exercise
        Optional<? extends User> result = userService.checkToken(token, isVerification);

        //Postconditions
        assertTrue(result.isPresent());
        assertEquals(PATIENT.getId(), result.get().getId());
    }

    @Test
    public void testCheckTokenValidDoctorVerificationExpiredToken() {
        //Preconditions
        String token = "TOKEN";
        boolean isVerification = true;
        when(doctorDao.getByVerificationToken(anyString())).thenReturn(Optional.of(DOCTOR));
        when(userDao.tokenExpirationDate(anyString())).thenReturn(LocalDateTime.now(ZoneId.systemDefault()).minusDays(1));
        when(doctorDao.getByEmail(anyString())).thenReturn(Optional.of(DOCTOR));

        //Exercise
        Optional<? extends User> result = userService.checkToken(token, isVerification);

        //Postconditions
        assertFalse(result.isPresent());
    }

    @Test
    public void testCheckTokenValidDoctorResetExpiredToken() {
        //Preconditions
        String token = "TOKEN";
        boolean isVerification = false;
        when(doctorDao.getByResetToken(token)).thenReturn(Optional.of(DOCTOR));
        when(userDao.tokenExpirationDate(token)).thenReturn(LocalDateTime.now(ZoneId.systemDefault()).minusDays(1));

        //Exercise
        Optional<? extends User> result = userService.checkToken(token, isVerification);

        //Postconditions
        assertFalse(result.isPresent());
    }

    @Test
    public void testCheckTokenValidDoctorVerification() {
        //Preconditions
        String token = "TOKEN";
        boolean isVerification = true;
        when(doctorDao.getByVerificationToken(token)).thenReturn(Optional.of(DOCTOR));
        when(userDao.tokenExpirationDate(token)).thenReturn(LocalDateTime.now(ZoneId.systemDefault()).plusDays(2));
        //Exercise
        Optional<? extends User> result = userService.checkToken(token, isVerification);

        //Postconditions
        assertTrue(result.isPresent());
        assertEquals(DOCTOR.getId(), result.get().getId());
    }
}
