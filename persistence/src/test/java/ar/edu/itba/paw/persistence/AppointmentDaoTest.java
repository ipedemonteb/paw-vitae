package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.interfacePersistence.PatientDao;
import ar.edu.itba.paw.models.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class AppointmentDaoTest {

    private static final String APPOINTMENT_TABLE = "appointments";

    private static final long DOC_ID = 2L;
    private static final String DOC_NAME = "Jane";
    private static final String DOC_LASTNAME = "Smith";
    private static final String DOC_EMAIL = "jane@example.com";
    private static final String DOC_PASSWORD = "hashedpassword";
    private static final String DOC_PHONE = "987654321";
    private static final String DOC_LANGUAGE = "es";
    private static final long DOC_IMG = 1L;
    private static final double DOC_RATING = 4.5;
    private static final int DOC_RATCOUNT = 10;
    private static final boolean DOC_VERIFIED = true;

    private static final long PAT_ID = 1L;
    private static final String PAT_NAME = "John";
    private static final String PAT_LASTNAME = "Doe";
    private static final String PAT_EMAIL = "john@example.com";
    private static final String PAT_PASSWORD = "hashedpassword";
    private static final String PAT_PHONE = "123456789";
    private static final String PAT_LANGUAGE = "en";
    private static final Coverage PAT_COVERAGE = new Coverage(1L, "Coverage A");
    private static final boolean PAT_VERIFIED = true;

    private JdbcTemplate jdbcTemplate;
    private AppointmentDao appointmentDao;
    private DoctorDao mockDoctor;
    private PatientDao mockPatient;
    private SimpleJdbcInsert insertAppointment;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.mockDoctor = mock(DoctorDao.class);
        this.mockPatient = mock(PatientDao.class);
        this.appointmentDao = new AppointmentDaoImpl(ds, mockDoctor, mockPatient);
        this.insertAppointment = new SimpleJdbcInsert(ds)
                .withTableName(APPOINTMENT_TABLE)
                .usingGeneratedKeyColumns("id");
    }

    @Test
    public void testCreate() {
        //Preconditions
        long patientId = 1L,
                docId = 2L;
        LocalDateTime dateTime = LocalDateTime.of(2025, 8, 1, 8, 0);
        String reason = "General checking";
        Specialty specialty = new Specialty(1L, "Cardiology");
        Doctor doctor = new Doctor(DOC_NAME, DOC_ID, DOC_LASTNAME,
                DOC_EMAIL, DOC_PASSWORD, DOC_PHONE, DOC_LANGUAGE, DOC_IMG,
                DOC_RATING, DOC_RATCOUNT, DOC_VERIFIED);
        Patient patient = new Patient(PAT_NAME, PAT_ID, PAT_LASTNAME,
                PAT_EMAIL, PAT_PASSWORD, PAT_PHONE, PAT_LANGUAGE, PAT_COVERAGE,
                PAT_VERIFIED);

        //Exercise
        Appointment appointment = appointmentDao.create(patientId, docId, dateTime, reason, specialty);

        //Postconditions
        assertNotNull(appointment);
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, APPOINTMENT_TABLE, "id = " + appointment.getId()));
    }

    @Test
    public void testCancelAppointment() {
        //Preconditions
        long appointmentId = 1L;

        //Exercise
        appointmentDao.cancelAppointment(appointmentId);

        //Postconditions
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, APPOINTMENT_TABLE, "id = " + appointmentId + " AND status = 'cancelado'"));
    }

    @Test
    public void testCompleteAppointment() {
        //Preconditions
        JdbcTestUtils.deleteFromTables(jdbcTemplate, APPOINTMENT_TABLE);
        insertAppointment.execute(Map.of(
                "doctor_id", DOC_ID,
                "patient_id", PAT_ID,
                "specialty_id", 1L,
                "date", LocalDateTime.now().minusDays(2),
                "status", "confirmado"
        ));
        insertAppointment.execute(Map.of(
                "doctor_id", DOC_ID,
                "patient_id", PAT_ID,
                "specialty_id", 1L,
                "date", LocalDateTime.now().minusDays(2),
                "status", "confirmado"
        ));

        //Exercise
        appointmentDao.completeAppointments();

        //Postconditions
        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, APPOINTMENT_TABLE, "status = 'completo'"));
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

    @Test
    public void testGetAppointments() {
        //Preconditions
        long userId = 1L,
            specialtyId = 1L;
        boolean isFuture = true;
        int page = 1;
        int size = 2;
        String status = "confirmado";
        insertAppointment.execute(Map.of(
                "doctor_id", DOC_ID,
                "patient_id", PAT_ID,
                "specialty_id", specialtyId,
                "date", LocalDateTime.now().plusDays(6),
                "status", status
        ));
        insertAppointment.execute(Map.of(
                "doctor_id", DOC_ID,
                "patient_id", PAT_ID,
                "specialty_id", specialtyId,
                "date", LocalDateTime.now().plusDays(8),
                "status", status
        ));

        //Exercise
        List<Appointment> appointments = appointmentDao.getAppointments(userId, isFuture, page, size, status);

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
    public void testUpdateReport() {
        //Preconditions
        long appointmentId = 1L;
        String newReport = "New report";

        //Exercise
        appointmentDao.updateReport(appointmentId, newReport);

        //Postconditions
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, APPOINTMENT_TABLE, "id = " + appointmentId + " AND report = '" + newReport + "'"));
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
        insertAppointment.execute(Map.of(
                "doctor_id", DOC_ID,
                "patient_id", PAT_ID,
                "specialty_id", specialtyId,
                "date", LocalDateTime.now().plusDays(6),
                "status", status
        ));
        insertAppointment.execute(Map.of(
                "doctor_id", DOC_ID,
                "patient_id", PAT_ID,
                "specialty_id", specialtyId,
                "date", LocalDateTime.now().plusDays(8),
                "status", status
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
}