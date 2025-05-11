package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.interfaceServices.MailService;
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
    private static final LocalDateTime DATE = LocalDateTime.of(2025, 5, 6, 10, 0);

    @Mock
    private AppointmentDao appointmentDao;

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
        Specialty specialty = mock(Specialty.class);
        Appointment expectedAppointment = mock(Appointment.class);
        Patient patient = mock(Patient.class);
        Doctor doctor = mock(Doctor.class);
        when(patient.getId()).thenReturn(PATIENT_ID);
        when(doctor.getId()).thenReturn(DOCTOR_ID);
        when(expectedAppointment.getPatient()).thenReturn(patient);
        when(expectedAppointment.getDoctor()).thenReturn(doctor);
        when(specialtyService.getById(SPECIALTY_ID)).thenReturn(Optional.of(specialty));
        when(appointmentDao.create(PATIENT_ID, DOCTOR_ID, DATE, REASON, specialty))
                .thenReturn(expectedAppointment);
        Appointment appointment = null;

        //Exercise
        try {
            appointment = appointmentService.create(PATIENT_ID, DOCTOR_ID, DATE.toLocalDate(), DATE.getHour(), REASON, SPECIALTY_ID);
        } catch (Exception e) {
            fail("Unexpected error during creation of appointment: " + e.getMessage());
        }

        //Postconditions
        assertNotNull(appointment);
        assertEquals(PATIENT_ID, appointment.getPatient().getId());
        assertEquals(DOCTOR_ID, appointment.getDoctor().getId());
    }

    //@TODO: CHECK IF OK CAUSE
    @Test
    public void testCancelAppointmentNotFound() {
        //Preconditions
        long appointmentId = 1L;
        long userId = 1000L;
        when(appointmentService.getById(appointmentId)).thenReturn(Optional.empty());
        boolean result = false;

        //Exercise
        try {
            result = appointmentService.cancelAppointment(appointmentId, userId);
        } catch (Exception e) {
            fail("Unexpected error during cancellation of appointment: " + e.getMessage());
        }

        //Postconditions
        assertFalse(result);
    }

    //@TODO: CHECK SAME AS BEFORE
    @Test
    public void testCancelAppointmentNotCancellable() {
        //Preconditions
        long appointmentId = 1L;
        long userId = 1000L;
        Appointment appointment = mock(Appointment.class);
        when(appointmentService.getById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointment.getDate()).thenReturn(LocalDateTime.now().plusHours(1));
        boolean result = false;

        //Exercise
        try {
            result = appointmentService.cancelAppointment(appointmentId, userId);
        } catch (Exception e) {
            fail("Unexpected error during cancellation of appointment: " + e.getMessage());
        }

        //Postconditions
        assertFalse(result);
    }

    //@TODO: CHECK SAME AS BEFORE
    @Test
    public void testCancelAppointment() {
        //Preconditions
        long appointmentId = 1L;
        long userId = 1L;
        Appointment appointment = mock(Appointment.class);
        when(appointmentService.getById(appointmentId)).thenReturn(Optional.of(appointment));
        when(appointment.getDate()).thenReturn(LocalDateTime.now().plusHours(3));
        when(appointment.getCancellable()).thenReturn(true);
        boolean result = false;

        //Exercise
        try {
            result = appointmentService.cancelAppointment(appointmentId, userId);
        } catch (Exception e) {
            fail("Unexpected error during cancellation of appointment: " + e.getMessage());
        }

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
        when(appointmentDao.getAppointments(eq(userId), eq(isFuture), anyInt(), anyInt(), anyString())).thenReturn(List.of(new Appointment(DATE, "confirmado", REASON, APPOINTMENT_ID,
                mock(Specialty.class),
                mock(Doctor.class),
                mock(Patient.class),
                "report")
        ));
        when(appointmentDao.countAppointments(userId, isFuture, filter)).thenReturn(1);
        Page<Appointment> appointments = null;

        //Exercise
        try{
            appointments = appointmentService.getAppointments(userId, isFuture, page, size, filter);
        } catch (Exception e) {
            fail("Unexpected error during retrieval of appointments: " + e.getMessage());
        }

        //Postconditions
        assertNotNull(appointments);
        assertEquals(1, appointments.getPageNumber());
    }

    @Test
    public void testGetAppointmentsWithPositivePage() {
        //Preconditions
        long userId = 1L;
        boolean isFuture = true;
        int page = 4;
        int size = 10;
        String filter = "filter";
        when(appointmentDao.getAppointments(eq(userId), eq(isFuture), anyInt(), anyInt(), anyString())).thenReturn(List.of(new Appointment(DATE, STATUS, REASON, APPOINTMENT_ID,
                mock(Specialty.class),
                mock(Doctor.class),
                mock(Patient.class),
                REPORT
        )));
        when(appointmentDao.countAppointments(userId, isFuture, filter)).thenReturn(1);
        Page<Appointment> appointments = null;

        //Exercise
        try {
            appointments = appointmentService.getAppointments(userId, isFuture, page, size, filter);
        } catch (Exception e) {
            fail("Unexpected error during retrieval of appointments: " + e.getMessage());
        }

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
        when(appointmentDao.getAppointmentsByUserAndDate(PATIENT_ID, date, time)).thenReturn(List.of(new Appointment(DATE, STATUS, REASON, APPOINTMENT_ID,
                mock(Specialty.class),
                mock(Doctor.class),
                mock(Patient.class),
                REPORT
        )));
        List<Appointment> appointments = Collections.emptyList();

        //Exercise
        try {
            appointments = appointmentService.getAppointmentByUserAndDate(PATIENT_ID, date, time);
        } catch (Exception e) {
            fail("Unexpected error during retrieval of appointments: " + e.getMessage());
        }

        //Postconditions
        assertFalse(appointments.isEmpty());
        assertEquals(1, appointments.size());
    }
}
