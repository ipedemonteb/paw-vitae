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
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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

    private static final String SQL_QUERY_STATUS = "SELECT status FROM appointments WHERE id = ?";
    private static final String SQL_QUERY_REPORT= "SELECT report FROM appointments WHERE id = ?";

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
    private SimpleJdbcInsert insertAppointment;
    private AppointmentDao appointmentDao;
    private DoctorDao mockDoctor;
    private PatientDao mockPatient;

    @Autowired
    private DataSource ds;
    private DaoUtils daoUtils;

    @Before
    public void setUp() {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.daoUtils = new DaoUtils();
        this.mockDoctor = mock(DoctorDao.class);
        this.mockPatient = mock(PatientDao.class);
        this.appointmentDao = new AppointmentDaoImpl(ds, mockDoctor, mockPatient, daoUtils);
        this.insertAppointment = new SimpleJdbcInsert(ds)
                .withTableName("Appointments")
                .usingGeneratedKeyColumns("id");
    }

    //@TODO: CHECK IF ITS OK
    @Test
    public void testCreate() {
        //Preconditions
        long patientId = 1L,
                docId = 2L;
        LocalDateTime dateTime = LocalDateTime.of(2025, 8, 1, 8, 0);
        String reason = "General checking";
        Specialty specialty = new Specialty(1L, "Cardiology");
        Doctor mockDoctorObj = new Doctor(DOC_NAME, DOC_ID, DOC_LASTNAME,
                DOC_EMAIL, DOC_PASSWORD, DOC_PHONE, DOC_LANGUAGE, DOC_IMG,
                DOC_RATING, DOC_RATCOUNT, DOC_VERIFIED);
        Patient mockPatientObj = new Patient(PAT_NAME, PAT_ID, PAT_LASTNAME,
                PAT_EMAIL, PAT_PASSWORD, PAT_PHONE, PAT_LANGUAGE, PAT_COVERAGE,
                PAT_VERIFIED);
        when(mockDoctor.getById(docId)).thenReturn(Optional.of(mockDoctorObj));
        when(mockPatient.getById(patientId)).thenReturn(Optional.of(mockPatientObj));

        //Exercise
        Appointment appointment = appointmentDao.create(patientId, docId, dateTime, reason, specialty);

        //Postconditions
        assertNotNull(appointment);
        assertEquals(patientId, appointment.getPatient().getId());
        assertEquals(docId, appointment.getDoctor().getId());
        assertEquals(dateTime, appointment.getDate());
        assertEquals(reason, appointment.getReason());
        assertEquals(specialty.getId(), appointment.getSpecialty().getId());
    }

    @Test
    public void testGetByPatientIdDoesNotExist() {
        //Preconditions
        long patientId = 1000L;

        //Exercise
        List<Appointment> maybeAppointment = appointmentDao.getByPatientId(patientId);

        //Postconditions
        assertTrue(maybeAppointment.isEmpty());
    }

    @Test
    public void testGetByPatientIdExists() {
        //Preconditions

        //Exercise
        List<Appointment> maybeAppointments = appointmentDao.getByPatientId(PAT_ID);

        //Postconditions
        assertFalse(maybeAppointments.isEmpty());
        assertEquals(2, maybeAppointments.size());
    }

    @Test
    public void testGetByDoctorIdDoesNotExist() {
        //Preconditions
        long docId = 1000L;

        //Exercise
        List<Appointment> maybeAppointments = appointmentDao.getByDoctorId(docId);

        //Postconditions
        assertTrue(maybeAppointments.isEmpty());
    }

    @Test
    public void testGetByDoctorIdExists() {
        //Preconditions
        insertAppointment.execute(Map.of(
                "doctor_id", DOC_ID,
                "client_id", PAT_ID,
                "specialty_id", 1L,
                "date", LocalDateTime.now().plusDays(6),
                "status", "confirmado"
        ));
        insertAppointment.execute(Map.of(
                "doctor_id", DOC_ID,
                "client_id", PAT_ID,
                "specialty_id", 1L,
                "date", LocalDateTime.now().plusDays(8),
                "status", "confirmado"
        ));

        //Exercise
        List<Appointment> maybeAppointments = appointmentDao.getByDoctorId(DOC_ID);

        //Postconditions
        assertFalse(maybeAppointments.isEmpty());
        assertEquals(2, maybeAppointments.size());
    }

    //@TODO: Check if its ok
    @Test
    public void testCancelAppointment() {
        //Preconditions
        long appointmentId = 1L;

        //Exercise
        appointmentDao.cancelAppointment(appointmentId);

        //Postconditions
        List<String> finalAppointment = jdbcTemplate.query(SQL_QUERY_STATUS,
                (rs, rowNum) -> rs.getString("status"),
                appointmentId
        );
        assertFalse(finalAppointment.isEmpty());
        assertEquals(1, finalAppointment.size());
        assertEquals("cancelado", finalAppointment.getFirst());
    }

    @Test
    public void testGetByIdDoesNotExist() {
        //Preconditions

        //Exercise
        Optional<Appointment> maybeAppointment = appointmentDao.getById(1000L);

        //Postconditions
        assertFalse(maybeAppointment.isPresent());
    }

    //@TODO: SHOULD I CHECK MORE PARAMS?
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
                "client_id", PAT_ID,
                "specialty_id", specialtyId,
                "date", LocalDateTime.now().plusDays(6),
                "status", status
        ));
        insertAppointment.execute(Map.of(
                "doctor_id", DOC_ID,
                "client_id", PAT_ID,
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

    //@TODO: CHECK PROBLEM WITH DATABASE
//    @Test
//    public void testGetAppointmentsByDate() {
//        //Preconditions
//        LocalDate date = LocalDate.of(2025, 4, 29);
//
//        //Exercise
//        List<Appointment> appointments = appointmentDao.getAppointmentsByDate(date);
//
//        //Postconditions
//        assertFalse(appointments.isEmpty());
//        assertEquals(1, appointments.size());
//    }

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
        List<String> finalAppointment = jdbcTemplate.query(SQL_QUERY_REPORT,
                (rs, rowNum) -> rs.getString("report"),
                appointmentId
        );
        assertFalse(finalAppointment.isEmpty());
        assertEquals(1, finalAppointment.size());
        assertEquals(newReport, finalAppointment.getFirst());
    }
}