//package ar.edu.itba.paw.services;
//
//import ar.edu.itba.paw.interfacePersistence.RatingDao;
//import ar.edu.itba.paw.interfaceServices.AppointmentService;
//import ar.edu.itba.paw.interfaceServices.DoctorService;
//import ar.edu.itba.paw.interfaceServices.MailService;
//import ar.edu.itba.paw.interfaceServices.PatientService;
//import ar.edu.itba.paw.models.Appointment;
//import ar.edu.itba.paw.models.Doctor;
//import ar.edu.itba.paw.models.Patient;
//import ar.edu.itba.paw.models.Rating;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.Optional;
//
//import static org.junit.Assert.*;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class RatingServiceImplTest {
//
//    private static final long SCORE = 5L;
//    private static final long DOCTOR_ID = 1L;
//    private static final long PATIENT_ID = 1L;
//    private static final long APPOINTMENT_ID = 1L;
//    private static final String COMMENT = "Great doctor!";
//
//    @Mock
//    private RatingDao ratingDao;
//
//    @Mock
//    private DoctorService doctorService;
//
//    @Mock
//    private PatientService patientService;
//
//    @Mock
//    private AppointmentService appointmentService;
//
//    @Mock
//    private MailService mailService;
//
//    @InjectMocks
//    private RatingServiceImpl ratingService;
//
//    @Test
//    public void testCreateRating() {
//        //Preconditions
//        when(ratingDao.create(anyLong(), anyLong(), anyLong(), anyLong(), anyString())).thenReturn(new Rating(1L, SCORE, DOCTOR_ID, PATIENT_ID, APPOINTMENT_ID, COMMENT));
//        when(doctorService.getById(anyLong())).thenReturn(Optional.of(mock(Doctor.class)));
//        when(patientService.getById(anyLong())).thenReturn(Optional.of(mock(Patient.class)));
//        when(appointmentService.getById(anyLong())).thenReturn(Optional.of(mock(Appointment.class)));
//
//        //Exercise
//        Rating rating = ratingService.create(SCORE, DOCTOR_ID, PATIENT_ID, APPOINTMENT_ID, COMMENT);
//
//        //Postconditions
//        assertNotNull(rating);
//        assertEquals(SCORE, rating.getRating());
//        assertEquals(DOCTOR_ID, rating.getDoctorId());
//        assertEquals(PATIENT_ID, rating.getPatientId());
//        assertEquals(APPOINTMENT_ID, rating.getAppointmentId());
//        assertEquals(COMMENT, rating.getComment());
//    }
//
//    @Test
//    public void testCreateRatingInvalidDoctorId() {
//        //Preconditions
//        when(ratingDao.create(anyLong(), anyLong(), anyLong(), anyLong(), anyString())).thenReturn(new Rating(1L, SCORE, DOCTOR_ID, PATIENT_ID, APPOINTMENT_ID, COMMENT));
//        when(doctorService.getById(anyLong())).thenReturn(Optional.empty());
//
//        //Exercise & Postconditions
//        assertThrows(IllegalArgumentException.class, () -> {
//            ratingService.create(SCORE, DOCTOR_ID, PATIENT_ID, APPOINTMENT_ID, COMMENT);
//        });
//    }
//
//    @Test
//    public void testCreateRatingInvalidPatientId() {
//        //Preconditions
//        when(ratingDao.create(anyLong(), anyLong(), anyLong(), anyLong(), anyString())).thenReturn(new Rating(1L, SCORE, DOCTOR_ID, PATIENT_ID, APPOINTMENT_ID, COMMENT));
//        when(doctorService.getById(anyLong())).thenReturn(Optional.of(mock(Doctor.class)));
//        when(patientService.getById(anyLong())).thenReturn(Optional.empty());
//
//        //Exercise & Postconditions
//        assertThrows(IllegalArgumentException.class, () -> {
//            ratingService.create(SCORE, DOCTOR_ID, PATIENT_ID, APPOINTMENT_ID, COMMENT);
//        });
//    }
//
//    @Test
//    public void testCreateRatingInvalidAppointmentId() {
//        //Preconditions
//        when(ratingDao.create(anyLong(), anyLong(), anyLong(), anyLong(), anyString())).thenReturn(new Rating(1L, SCORE, DOCTOR_ID, PATIENT_ID, APPOINTMENT_ID, COMMENT));
//        when(doctorService.getById(anyLong())).thenReturn(Optional.of(mock(Doctor.class)));
//        when(patientService.getById(anyLong())).thenReturn(Optional.of(mock(Patient.class)));
//        when(appointmentService.getById(anyLong())).thenReturn(Optional.empty());
//
//        //Exercise & Postconditions
//        assertThrows(IllegalArgumentException.class, () -> {
//            ratingService.create(SCORE, DOCTOR_ID, PATIENT_ID, APPOINTMENT_ID, COMMENT);
//        });
//    }
//}
