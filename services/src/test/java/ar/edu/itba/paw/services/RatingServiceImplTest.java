package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.RatingDao;
import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.MailService;
import ar.edu.itba.paw.interfaceServices.PatientService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.AppointmentNotFoundException;
import ar.edu.itba.paw.models.exception.ResourceOwnershipException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class RatingServiceImplTest {

    private static final long SCORE = 5L;
    private static final String EMAIL = "john@test.com";
    private static final long DOCTOR_ID = 2L;
    private static final Doctor DOCTOR = new Doctor("Jane", "Smith", "jane@test.com", "hashedpassword", "987654321", "es",
            1L, 4.5, 10, true);
    private static final long PATIENT_ID = 1L;
    private static final Neighborhood NEIGHBORHOOD = new Neighborhood(1L, "Neighborhood A");
    private static final Patient PATIENT = new Patient("John", "Doe", "john@test.com", "hashedpassword", "123456789", "en",
            new Coverage(1L, "Coverage A"), NEIGHBORHOOD, true);
    private static final long APPOINTMENT_ID = 1L;
    private static final Appointment APPOINTMENT = new Appointment(LocalDateTime.now(ZoneId.systemDefault()), "Confirmed", "Consultation",
            new Specialty(3L, "Cardiology"), DOCTOR, PATIENT, "Report",
            new DoctorOffice(DOCTOR, NEIGHBORHOOD, List.of(new Specialty(1L, "Specialty A")),
                    "Office A", true, null,
                    List.of(new DoctorOfficeAvailability())), true
    );
    private static final String COMMENT = "Great doctor!";
    private static final Rating RATING = new Rating(SCORE, DOCTOR, PATIENT, APPOINTMENT, COMMENT);

    @Mock
    private RatingDao ratingDao;

    @Mock
    private DoctorService doctorService;

    @Mock
    private PatientService patientService;

    @Mock
    private AppointmentService appointmentService;

    @Mock
    private MailService mailService;

    @InjectMocks
    private RatingServiceImpl ratingService;

    @Test
    public void testCreateRatingInvalidPatientId() {
        //Preconditions

        //Exercise & Postconditions
        assertThrows(UserNotFoundException.class, () -> {
            ratingService.create(SCORE, EMAIL, APPOINTMENT_ID, COMMENT);
        });
    }

    @Test
    public void testCreateRatingInvalidAppointmentId() {
        //Preconditions
        when(patientService.getByEmail(anyString())).thenReturn(Optional.of(PATIENT));

        //Exercise & Postconditions
        assertThrows(AppointmentNotFoundException.class, () -> {
            ratingService.create(SCORE, EMAIL, APPOINTMENT_ID, COMMENT);
        });
    }

    @Test
    public void testCreateRatingNoOwnership() {
        //Preconditions
        Patient patient = mock(Patient.class);
        when(patient.getId()).thenReturn(999L);
        when(patientService.getByEmail(anyString())).thenReturn(Optional.of(patient));
        when(appointmentService.getById(anyLong())).thenReturn(Optional.of(APPOINTMENT));

        //Exercise & Postconditions
        assertThrows(ResourceOwnershipException.class, () -> {
            ratingService.create(SCORE, EMAIL, APPOINTMENT_ID, COMMENT);
        });
    }

    @Test
    public void testCreateRatingInvalidDoctorId() {
        //Preconditions
        when(patientService.getByEmail(anyString())).thenReturn(Optional.of(PATIENT));
        when(appointmentService.getById(anyLong())).thenReturn(Optional.of(APPOINTMENT));

        //Exercise & Postconditions
        assertThrows(UserNotFoundException.class, () -> {
            ratingService.create(SCORE, EMAIL, APPOINTMENT_ID, COMMENT);
        });
    }

    @Test
    public void testCreateRating() {
        //Preconditions
        when(ratingDao.create(anyLong(), any(), any(), any(), anyString())).thenReturn(new Rating(SCORE, DOCTOR, PATIENT, APPOINTMENT, COMMENT));
        when(doctorService.getById(anyLong())).thenReturn(Optional.of(DOCTOR));
        when(patientService.getByEmail(anyString())).thenReturn(Optional.of(PATIENT));
        when(appointmentService.getById(anyLong())).thenReturn(Optional.of(APPOINTMENT));

        //Exercise
        Rating rating = ratingService.create(SCORE, EMAIL, APPOINTMENT_ID, COMMENT);

        //Postconditions
        assertNotNull(rating);
        assertEquals(SCORE, rating.getRating());
        assertEquals(COMMENT, rating.getComment());
    }

    @Test
    public void testGetFiveTopRatingsInvalidPatient() {
        //Preconditions
        when(ratingDao.getFiveTopRatings()).thenReturn(List.of(RATING));
        when(patientService.getById(anyLong())).thenReturn(Optional.empty());

        //Exercise & Postconditions
        assertThrows(UserNotFoundException.class, () ->
                ratingService.getFiveTopRatings()
        );
    }
}
