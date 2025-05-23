package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.MailService;
import ar.edu.itba.paw.interfaceServices.PatientService;
import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import ar.edu.itba.paw.models.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentServiceImplTest {

    private static final long APPOINTMENT_ID = 1L;
    private static final long PATIENT_ID = 1L;
    private static final long DOCTOR_ID = 2L;
    private static final long SPECIALTY_ID = 3L;
    private static final String REASON = "Checkup";
    private static final String STATUS = "Confirmado";
    private static final String REPORT = "Report";
    private static final LocalDateTime DATE = LocalDateTime.now().plusDays(1);

    private static final Specialty SPECIALTY = new Specialty(1L, "Cardiology");
    private static final Doctor DOCTOR = new Doctor("Jane", "Smith", "jane@test.com", "hashedpassword", "987654321", "es",
            1L, 4.5, 10, true);
    private static final Patient PATIENT = new Patient("John", "Doe", "john@test.com", "hashedpassword", "123456789", "en",
            new Coverage(1L, "Coverage A"), true);

    private static final Appointment APPOINTMENT = new Appointment(
            DATE,
            STATUS,
            REASON,
            SPECIALTY,
            DOCTOR,
            PATIENT,
            REPORT
    );
    private static final Appointment APPOINTMENT_CANC = new Appointment(
            LocalDateTime.now(),
            STATUS,
            REASON,
            SPECIALTY,
            DOCTOR,
            PATIENT,
            REPORT
    );

    @Mock
    private AppointmentDao appointmentDao;

    @Mock
    private DoctorService doctorService;

    @Mock
    private PatientService patientService;

    @Mock
    private SpecialtyService specialtyService;

    @Mock
    private MailService mailService;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;

    @Test
    public void testCreateNonExistentSpecialty() {
        //Preconditions
        when(specialtyService.getById(anyLong())).thenReturn(Optional.empty());

        //Exercise & Postconditions
        assertThrows(IllegalArgumentException.class, () -> {
            appointmentService.create(PATIENT_ID, DOCTOR_ID, DATE.toLocalDate(), DATE.getHour(), REASON, SPECIALTY_ID);
        });
    }

    @Test
    public void testCreate() {
        //Preconditions
        when(specialtyService.getById(SPECIALTY_ID)).thenReturn(Optional.of(SPECIALTY));
        when(appointmentDao.create(any(LocalDateTime.class), anyString(), anyString(), any(), any(), any(), anyString())).thenReturn(APPOINTMENT);
        when(doctorService.getById(anyLong())).thenReturn(Optional.of(DOCTOR));
        when(patientService.getById(anyLong())).thenReturn(Optional.of(PATIENT));

        //Exercise
        Appointment appointment = appointmentService.create(0L, 0L, DATE.toLocalDate(), DATE.getHour(), REASON, SPECIALTY_ID);

        //Postconditions
        assertNotNull(appointment);
        assertEquals(0L, appointment.getPatient().getId());
        assertEquals(0L, appointment.getDoctor().getId());
    }

    @Test
    public void testCancelAppointmentNotFound() {
        //Preconditions
        long appointmentId = 1L;
        long userId = 1000L;
        when(appointmentDao.getById(appointmentId)).thenReturn(Optional.empty());

        //Exercise
        boolean result = appointmentService.cancelAppointment(appointmentId, userId);

        //Postconditions
        assertFalse(result);
    }

    @Test
    public void testCancelAppointmentNotCancellable() {
        //Preconditions
        long userId = 1000L;
        when(appointmentDao.getById(APPOINTMENT_ID)).thenReturn(Optional.of(APPOINTMENT_CANC));

        //Exercise
        boolean result = appointmentService.cancelAppointment(APPOINTMENT_ID, userId);

        //Postconditions
        assertFalse(result);
    }

    @Test
    public void testCancelAppointment() {
        //Preconditions
        long appointmentId = 1L;
        long userId = 1L;
        when(appointmentDao.getById(appointmentId)).thenReturn(Optional.of(APPOINTMENT));

        //Exercise
        boolean result = appointmentService.cancelAppointment(appointmentId, userId);

        //Postconditions
        assertTrue(result);
    }

    @Test
    public void testGetAppointmentsWithNegativePage() {
        //Preconditions
        long userId = 1L;
        boolean isFuture = true;
        int page = -1;
        int size = 10;
        String filter = "filter";
        when(appointmentDao.getAppointments(eq(userId), eq(isFuture), anyInt(), anyInt(), anyString())).thenReturn(List.of(APPOINTMENT));
        when(appointmentDao.countAppointments(userId, isFuture, filter)).thenReturn(1);

        //Exercise
        Page<Appointment> appointments = appointmentService.getAppointments(userId, isFuture, page, size, filter);

        //Postconditions
        assertNotNull(appointments);
        assertEquals(1, appointments.getPageNumber());
    }

    @Test
    public void testGetAppointmentsWithPositivePage() {
        //Preconditions
        long userId = 1L;
        boolean isFuture = true;
        int page =4;
        int size = 10;
        String filter = "filter";
        when(appointmentDao.getAppointments(eq(userId), eq(isFuture), anyInt(), anyInt(), anyString())).thenReturn(List.of(APPOINTMENT));
        when(appointmentDao.countAppointments(userId, isFuture, filter)).thenReturn(1);

        //Exercise
        Page<Appointment> appointments = appointmentService.getAppointments(userId, isFuture, page, size, filter);

        //Postconditions
        assertNotNull(appointments);
        assertEquals(page, appointments.getPageNumber());
    }

    @Test
    public void testGetAppointmentByUserAndDateWithNullDate() {
        //Preconditions
        Integer time = 10;

        //Exercise
        List<Appointment> appointments = appointmentService.getAppointmentByUserAndDate(PATIENT_ID, null, time);

        //Postconditions
        assertNotNull(appointments);
        assertTrue(appointments.isEmpty());
    }

    @Test
    public void testGetAppointmentByUserAndDateWithNullTime() {
        //Preconditions
        LocalDate date = LocalDate.now();

        //Exercise
        List<Appointment> appointments = appointmentService.getAppointmentByUserAndDate(PATIENT_ID, date, null);

        //Postconditions
        assertNotNull(appointments);
        assertTrue(appointments.isEmpty());
    }

    @Test
    public void testGetAppointmentByUserAndDate() {
        //Preconditions
        LocalDate date = LocalDate.now();
        Integer time = 10;
        when(appointmentDao.getAppointmentsByUserAndDate(PATIENT_ID, date, time)).thenReturn(List.of(APPOINTMENT));

        //Exercise
        List<Appointment> appointments = appointmentService.getAppointmentByUserAndDate(PATIENT_ID, date, time);

        //Postconditions
        assertFalse(appointments.isEmpty());
        assertEquals(1, appointments.size());
    }
}
