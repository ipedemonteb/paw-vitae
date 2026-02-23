package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.NoSuchElementException;
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
    private static final long OFFICE_ID = 1L;
    private static final String REASON = "Checkup";
    private static final String STATUS = "Confirmado";
    private static final String REPORT = "Report";
    private static final LocalDateTime DATE = LocalDateTime.now(ZoneId.systemDefault()).plusDays(1);
    private static final Integer HOUR = 10;
    private static final boolean ALLOW = true;
    private static final int SLOT_ID = 1;

    private static final Specialty SPECIALTY = new Specialty(1L, "Cardiology");
    private static final Neighborhood NEIGHBORHOOD = new Neighborhood(1L, "Neighborhood A");
    private static final Doctor DOCTOR = new Doctor("Jane", "Smith", "jane@test.com", "hashedpassword", "987654321", "es",
             4.5, 10, true);
    private static final Patient PATIENT = new Patient("John", "Doe", "john@test.com", "hashedpassword", "123456789", "en",
            new Coverage(1L, "Coverage A"), NEIGHBORHOOD, true);
    private static final DoctorOffice DOCTOR_OFFICE = new DoctorOffice(DOCTOR, NEIGHBORHOOD, List.of(SPECIALTY), "Office A");
    private static final OccupiedSlots SLOT = new OccupiedSlots(DOCTOR, DATE.toLocalDate(), LocalTime.of(10, 0));
    private static final Appointment APPOINTMENT = new Appointment(
            DATE,
            STATUS,
            REASON,
            SPECIALTY,
            DOCTOR,
            PATIENT,
            REPORT,
            DOCTOR_OFFICE,
            ALLOW
    );
    private static final Appointment APPOINTMENT_CANC = new Appointment(
            LocalDateTime.now(ZoneId.systemDefault()),
            STATUS,
            REASON,
            SPECIALTY,
            DOCTOR,
            PATIENT,
            REPORT,
            DOCTOR_OFFICE,
            ALLOW
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
    private DoctorOfficeService doctorOfficeService;
    @Mock
    private MailService mailService;
    @Mock
    private OccupiedSlotsService occupiedSlotsService;

    @InjectMocks
    private AppointmentServiceImpl appointmentService;



    @Test
    public void testCreateNonExistentSpecialty() {
        //Preconditions

        //Exercise & Postconditions
        assertThrows(SpecialtyNotFoundException.class, () -> {
            appointmentService.create(PATIENT_ID, DOCTOR_ID, DATE.toLocalDate(),HOUR, REASON, SPECIALTY_ID, OFFICE_ID, ALLOW);
        });
    }

    @Test
    public void testCreateOfficeNotFound() {
        //Preconditions
        when(specialtyService.getById(SPECIALTY_ID)).thenReturn(Optional.of(new Specialty(SPECIALTY_ID, "Cardiology")));

        when(doctorOfficeService.getById(OFFICE_ID)).thenReturn(Optional.empty());

        //Exercise & Postconditions
        assertThrows(DoctorOfficeNotFoundException.class, () -> {
            appointmentService.create(PATIENT_ID, DOCTOR_ID, DATE.toLocalDate(), HOUR, REASON, SPECIALTY_ID, OFFICE_ID, ALLOW);
        });
    }

    @Test
    public void testCreateUserNotFound() {
        //Preconditions
        when(specialtyService.getById(anyLong())).thenReturn(Optional.of(mock(Specialty.class)));
        when(doctorOfficeService.getById(anyLong())).thenReturn(Optional.of(new DoctorOffice()));

        //Exercise & Postconditions
        assertThrows(UserNotFoundException.class, () -> {
            appointmentService.create(PATIENT_ID, DOCTOR_ID, DATE.toLocalDate(), HOUR, REASON, SPECIALTY_ID, OFFICE_ID, ALLOW);
        });
    }

    @Test
    public void testCreate() {
        //Precondition
        when(specialtyService.getById(SPECIALTY_ID)).thenReturn(Optional.of(SPECIALTY));
        when(appointmentDao.create(any(LocalDateTime.class), anyString(), anyString(), any(), any(), any(), anyString(), any(), anyBoolean())).thenReturn(APPOINTMENT);
        when(doctorService.getById(anyLong())).thenReturn(Optional.of(DOCTOR));
        when(patientService.getById(anyLong())).thenReturn(Optional.of(PATIENT));
        when(doctorOfficeService.getById(anyLong())).thenReturn(Optional.of(DOCTOR_OFFICE));

        //Exercise
        Appointment appointment = appointmentService.create(0L, 0L, DATE.toLocalDate(),HOUR, REASON, SPECIALTY_ID, OFFICE_ID, ALLOW);

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

        //Exercise & Postconditions
        assertThrows(AppointmentNotFoundException.class, () ->
                appointmentService.cancelAppointment(appointmentId)
        );
    }

    @Test
    public void testCancelAppointmentNotCancellable() {
        //Preconditions
        long userId = 1000L;
        when(appointmentDao.getById(APPOINTMENT_ID)).thenReturn(Optional.of(APPOINTMENT_CANC));

        //Exercise & Postconditions
        assertThrows(CancellableException.class, () ->
                appointmentService.cancelAppointment(APPOINTMENT_ID)
        );
    }

    @Test
    public void testCancelAppointment() {
        //Preconditions
        long appointmentId = 1L;
        long userId = 1L;
        when(appointmentDao.getById(appointmentId)).thenReturn(Optional.of(APPOINTMENT));

        //Exercise
        boolean result = appointmentService.cancelAppointment(appointmentId);

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
        String sort = "asc";
        when(appointmentDao.getAppointments(eq(userId), eq(isFuture), anyInt(), anyInt(), anyString(), anyString())).thenReturn(List.of(APPOINTMENT));
        when(appointmentDao.countAppointments(userId, isFuture, filter)).thenReturn(1);

        //Exercise
        Page<Appointment> appointments = appointmentService.getAppointments(userId, isFuture, page, size, filter, sort);

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
        String sort = "asc";
        when(appointmentDao.getAppointments(eq(userId), eq(isFuture), anyInt(), anyInt(), anyString(), anyString())).thenReturn(List.of(APPOINTMENT));
        when(appointmentDao.countAppointments(userId, isFuture, filter)).thenReturn(1);

        //Exercise
        Page<Appointment> appointments = appointmentService.getAppointments(userId, isFuture, page, size, filter, sort);

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



    @Test
    public void testRevokeHistoryPermissionForOldAppointments() {
        //Preconditions
        List<Appointment> appointments = List.of(APPOINTMENT);
        when(appointmentDao.getAppointmentsWithHistoryAllowedBefore(any())).thenReturn(appointments);

        //Exercise
        appointmentService.revokeHistoryPermissionForOldAppointments();

        //Postconditions
        assertFalse(appointments.getFirst().isAllowFullHistory());
    }

    @Test
    public void testUpdateAppointmentReportNoReport() {
        //Preconditions

        //Exercise
        Optional<Long> appointmentId = appointmentService.updateAppointmentReport(APPOINTMENT_ID, null);

        //Postconditions
        assertFalse(appointmentId.isPresent());
    }

    @Test
    public void testUpdateAppointmentReportNoAppointment() {
        //Preconditions
        when(appointmentService.getById(APPOINTMENT_ID)).thenReturn(Optional.empty());

        //Exercise
        Optional<Long> appointmentId = appointmentService.updateAppointmentReport(APPOINTMENT_ID, null);

        //Postconditions
        assertFalse(appointmentId.isPresent());
    }

    @Test
    public void testUpdateAppointmentReport() {
        //Preconditions
        String newReport = "New report";
        when(appointmentService.getById(APPOINTMENT_ID)).thenReturn(Optional.of(APPOINTMENT));

        //Exercise
        Optional<Long> appointmentId = appointmentService.updateAppointmentReport(APPOINTMENT_ID, newReport);

        //Postconditions
        assertTrue(appointmentId.isPresent());
        assertEquals(newReport, APPOINTMENT.getReport());
    }
}
