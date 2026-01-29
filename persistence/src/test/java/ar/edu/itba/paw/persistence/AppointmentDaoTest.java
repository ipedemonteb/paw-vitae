package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.models.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class AppointmentDaoTest {

    private static final String APPOINTMENT_TABLE = "appointments";

    private static final long DOC_ID = 2L;
    private static final long PAT_ID = 1L;
    private static final long SPEC_ID = 1L;
    private static final long OFFICE_ID = 1L;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private AppointmentDao appointmentDao;
    private SimpleJdbcInsert insertAppointment;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.insertAppointment = new SimpleJdbcInsert(ds)
                .withTableName(APPOINTMENT_TABLE)
                .usingGeneratedKeyColumns("id");
    }

    @Rollback
    @Test
    public void testCreate() {
        //Preconditions
        LocalDateTime dateTime = LocalDateTime.of(2025, 8, 1, 8, 0);
        String status = "confirmado";
        String reason = "General checking";
        Specialty managedSpecialty = em.getReference(Specialty.class, SPEC_ID);
        Doctor managedDoctor = em.getReference(Doctor.class, DOC_ID);
        Patient managedPatient = em.getReference(Patient.class, PAT_ID);
        String report = "Report content";
        DoctorOffice office = em.getReference(DoctorOffice.class, OFFICE_ID);
        boolean allow = true;

        //Exercise
        Appointment appointment = appointmentDao.create(dateTime, status, reason, managedSpecialty, managedDoctor, managedPatient, report, office, allow);
        em.flush();

        //Postconditions
        assertNotNull(appointment);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, APPOINTMENT_TABLE, "id = " + appointment.getId()));
    }

    @Test
    public void testGetByIdDoesNotExist() {
        //Preconditions

        //Exercise
        Optional<Appointment> maybeAppointment = appointmentDao.getById(1000L);

        //Postconditions
        assertFalse(maybeAppointment.isPresent());
    }

    @Test
    public void testGetByIdExists() {
        //Preconditions
        long appointmentId = 1L;

        //Exercise
        Optional<Appointment> maybeAppointment = appointmentDao.getById(appointmentId);

        //Postconditions
        assertTrue(maybeAppointment.isPresent());
        assertEquals(appointmentId, maybeAppointment.get().getId());
    }

    @Rollback
    @Test
    public void testGetAppointments() {
        //Preconditions
        long userId = 1L,
            specialtyId = 1L;
        boolean isFuture = true;
        int page = 1;
        int size = 2;
        String status = "confirmado";
        String reason = "General checking";
        long officeId = 1L;
        boolean allow = true;
        insertAppointment.execute(Map.of(
                "doctor_id", DOC_ID,
                "patient_id", PAT_ID,
                "specialty_id", specialtyId,
                "date", LocalDateTime.now(ZoneId.systemDefault()).plusDays(6),
                "status", status,
                "reason", reason,
                "office_id", officeId,
                "allow_full_history", allow
        ));
        insertAppointment.execute(Map.of(
                "doctor_id", DOC_ID,
                "patient_id", PAT_ID,
                "specialty_id", specialtyId,
                "date", LocalDateTime.now(ZoneId.systemDefault()).plusDays(8),
                "status", status,
                "reason", reason,
                "office_id", officeId,
                "allow_full_history", allow
        ));

        //Exercise
        List<Appointment> appointments = appointmentDao.getAppointments(userId, isFuture, page, size, status, "");

        //Postconditions
        assertFalse(appointments.isEmpty());
        assertEquals(size, appointments.size());
        appointments.forEach(appointment -> {
            assertEquals(DOC_ID, appointment.getDoctor().getId());
            assertEquals(PAT_ID, appointment.getPatient().getId());
            assertEquals(specialtyId, appointment.getSpecialty().getId());
            assertEquals(status, appointment.getStatus());
        });
    }

    @Test
    public void testGetAppointmentsByDate() {
        //Preconditions
        LocalDate date = LocalDate.of(2025, 4, 29);

        //Exercise
        List<Appointment> appointments = appointmentDao.getAppointmentsByDate(date);

        //Postconditions
        assertFalse(appointments.isEmpty());
        assertEquals(1, appointments.size());
    }

    @Test
    public void testCountAppointments() {
        //Preconditions
        boolean isFuture = false;
        String filter = "all";

        //Exercise
        int count = appointmentDao.countAppointments(PAT_ID, isFuture, filter);

        //Postconditions
        assertEquals(2, count);
    }

    @Test
    public void testGetAppointmentsByUserAndDate() {
        //Preconditions
        long userId = 1L;
        LocalDate date = LocalDate.of(2025, 4, 29);
        Integer time = 10;

        //Exercise
        List<Appointment> appointments = appointmentDao.getAppointmentsByUserAndDate(userId, date, time);

        //Postconditions
        assertFalse(appointments.isEmpty());
        assertEquals(1, appointments.size());
        assertEquals(userId, appointments.getFirst().getPatient().getId());
    }

    @Test
    public void testGetFutureAppointmentsByUserExist() {
        //Preconditions
        long specialtyId = 1L;
        String status = "confirmado";
        String reason = "General checking";
        long officeId = 1L;
        boolean allow = true;
        insertAppointment.execute(Map.of(
                "doctor_id", DOC_ID,
                "patient_id", PAT_ID,
                "specialty_id", specialtyId,
                "date", LocalDateTime.now(ZoneId.systemDefault()).plusDays(6),
                "status", status,
                "reason", reason,
                "office_id", officeId,
                "allow_full_history", allow
        ));
        insertAppointment.execute(Map.of(
                "doctor_id", DOC_ID,
                "patient_id", PAT_ID,
                "specialty_id", specialtyId,
                "date", LocalDateTime.now(ZoneId.systemDefault()).plusDays(8),
                "status", status,
                "reason", reason,
                "office_id", officeId,
                "allow_full_history", allow
        ));

        //Exercise
        List<Appointment> appointments = appointmentDao.getFutureAppointmentsByUser(PAT_ID);

        //Postconditions
        assertFalse(appointments.isEmpty());
        assertEquals(2, appointments.size());
    }

    @Test
    public void testGetFutureAppointmentsByUserDoNotExist() {
        //Preconditions

        //Exercise
        List<Appointment> appointments = appointmentDao.getFutureAppointmentsByUser(PAT_ID);

        //Postconditions
        assertTrue(appointments.isEmpty());
    }

    @Test
    public void testGetPastConfirmedAppointments() {
        //Preconditions

        //Exercise
        List<Appointment> appointments = appointmentDao.getPastConfirmedAppointments();

        //Postconditions
        assertFalse(appointments.isEmpty());
        assertEquals(1, appointments.size());
    }

    @Test
    public void testGetAppointmentsWithHistoryAllowedBefore() {
        //Preconditions
        LocalDateTime dateTime = LocalDateTime.of(2025, 5, 1, 10, 0);

        //Exercise
        List<Appointment> appointments = appointmentDao.getAppointmentsWithHistoryAllowedBefore(dateTime);

        //Postconditions
        assertFalse(appointments.isEmpty());
        assertEquals(3, appointments.size());
    }

    @Test
    public void testGetAppointmentsByPatientDoesNotExist() {
        //Preconditions
        long patientId = 1000L;
        int page = 1;
        int size = 10;

        //Exercise
        List<Appointment> appointments = appointmentDao.getAppointmentsByPatient(patientId, page, size);

        //Postconditions
        assertTrue(appointments.isEmpty());
    }

    @Test
    public void testGetAppointmentsByPatientExists() {
        //Preconditions
        int page = 1;
        int size = 10;

        //Exercise
        List<Appointment> appointments = appointmentDao.getAppointmentsByPatient(PAT_ID, page, size);

        //Postconditions
        assertFalse(appointments.isEmpty());
        assertEquals(2, appointments.size());
    }

    @Test
    public void testGetAppointmentsByPatientWithFilesOrReport() {
        //Preconditions
        int page = 1;
        int size = 10;
        String order = "desc";

        //Exercise
        List<Appointment> appointments = appointmentDao.getAppointmentsByPatientWithFilesOrReport(PAT_ID, page, size, order);

        //Postconditions
        assertFalse(appointments.isEmpty());
        assertEquals(2, appointments.size());
        assertEquals(2L, appointments.getFirst().getId());
        assertEquals(1L, appointments.get(1).getId());
    }

    @Test
    public void testCountAppointmentsByPatientWithFilesOrReport() {
        //Preconditions

        //Exercise
        int count = appointmentDao.countAppointmentsByPatientWithFilesOrReport(PAT_ID);

        //Postconditions
        assertEquals(2, count);
    }

    @Test
    public void testHasFullMedicalHistoryEnabled() {
        //Preconditions
        long patientId = 1L;
        long doctorId = 2L;

        //Exercise
        boolean hasFullHistory = appointmentDao.hasFullMedicalHistoryEnabled(patientId, doctorId);

        //Postconditions
        assertTrue(hasFullHistory);
    }

    @Test
    public void testCountAppointmentsByPatient() {
        //Preconditions
        long patientId = 3L;

        //Exercise
        int count = appointmentDao.countAppointmentsByPatient(patientId);

        //Postconditions
        assertEquals(8, count);
    }

    @Test
    public void testOfficeHasAppointments() {
        //Preconditions

        //Exercise
        boolean hasAppointments = appointmentDao.officeHasAppointments(OFFICE_ID);

        //Postconditions
        assertTrue(hasAppointments);
    }

    @Test
    public void testHasAppointmentWithPatientDoesNotExist() {
        //Preconditions
        long patientId = 1000L;

        //Exercise
        boolean hasAppointment = appointmentDao.hasAppointmentWithPatient(patientId, DOC_ID);

        //Postconditions
        assertFalse(hasAppointment);
    }

    @Test
    public void testHasAppointmentWithPatientExists() {
        //Preconditions

        //Exercise
        boolean hasAppointment = appointmentDao.hasAppointmentWithPatient(PAT_ID, DOC_ID);

        //Postconditions
        assertTrue(hasAppointment);
    }
}