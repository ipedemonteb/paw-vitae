package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.DoctorDao;
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

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class AppointmentDaoTest {

    private static final String USERS_TABLE = "users";
    private static final String CLIENTS_TABLE = "clients";
    private static final String DOCTORS_TABLE = "doctors";
    private static final String COVERAGES_TABLE = "coverages";
    private static final String SPECIALTIES_TABLE = "specialties";

    private static final String APPOINTREASON = "Check";
    private static final String APPOINTSTATUS = "pendiente";

    private AppointmentDaoImpl appointmentDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertUser;
    private SimpleJdbcInsert jdbcInsertClient;
    private SimpleJdbcInsert jdbcInsertDoctor;
    private SimpleJdbcInsert jdbcInsertCoverage;
    private SimpleJdbcInsert jdbcInsertSpecialty;

    private long specId;
    private long docId;
    private long cliId;
    private long covId;

    @Autowired
    private DataSource ds;

    private DoctorDaoImpl mockDoctorDao;

    @Before
    public void setUp() {

        mockDoctorDao = mock(DoctorDaoImpl.class);

        jdbcTemplate = new JdbcTemplate(ds);
        appointmentDao = new AppointmentDaoImpl(ds, mockDoctorDao);

        //Generate tables
        jdbcInsertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(USERS_TABLE)
                .usingGeneratedKeyColumns("id");
        jdbcInsertClient = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(CLIENTS_TABLE);
        jdbcInsertDoctor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(DOCTORS_TABLE);
        jdbcInsertCoverage = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(COVERAGES_TABLE)
                .usingGeneratedKeyColumns("id");
        jdbcInsertSpecialty = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName(SPECIALTIES_TABLE)
                .usingGeneratedKeyColumns("id");

        JdbcTestUtils.deleteFromTables(jdbcTemplate, USERS_TABLE, CLIENTS_TABLE, DOCTORS_TABLE, COVERAGES_TABLE, SPECIALTIES_TABLE);

        //Create specialties
        specId = jdbcInsertSpecialty.executeAndReturnKey(Map.of("key", "Cardiology")).longValue();

        //Create coverages
        covId = jdbcInsertCoverage.executeAndReturnKey(Map.of("coverage_name", "TestCoverage")).longValue();

        //Create users for doctor and client
        docId = jdbcInsertUser.executeAndReturnKey(
                Map.of(
                        "name", "Carlos Salvador",
                        "last_name", "Bilardo",
                        "email", "csb@gmail.com",
                        "password", "password",
                        "phone", "1177777777"
                )
        ).longValue();
        cliId = jdbcInsertUser.executeAndReturnKey(
                Map.of(
                        "name", "John",
                        "last_name", "Doe",
                        "email", "johndoe@gmail.com",
                        "password", "password",
                        "phone", "1177777777"
                )
        ).longValue();

        //Create doctor
        jdbcInsertDoctor.execute(Map.of("doctor_id", docId));

        //Create client
        jdbcInsertClient.execute(Map.of("client_id", cliId, "coverage_id", covId));

    }

    @Test
    public void testCreateAppointment() {
        //Preconditions
        LocalDateTime date = LocalDateTime.now().plusDays(1);

        //Exercise
        //CHEQUEAR el new Specialty
        Appointment appointment = appointmentDao.create(cliId, docId, date, APPOINTREASON, new Specialty(specId, "Cardiology"));

        //Postconditions
        assertNotNull(appointment);
        assertEquals(docId, appointment.getDoctorId());
        assertEquals(cliId, appointment.getClientId());
        assertEquals(date, appointment.getDate());
        assertEquals(APPOINTREASON, appointment.getReason());
        assertEquals(APPOINTSTATUS, appointment.getStatus());
        assertEquals(specId, appointment.getSpecialty().getId());
    }

    @Test
    public void testGetByClientIdDoesDoesNotExist() {
        //Preconditions

        //Exercise
        Optional<List<Appointment>> maybeAppointments = appointmentDao.getByClientId(9999L);

        //Postconditions
        assertNotNull(maybeAppointments);
        assertTrue(maybeAppointments.isEmpty());
    }

    @Test
    public void testGetByClientExists() {
        //Preconditions
        LocalDateTime date = LocalDateTime.now().plusDays(1);
        Appointment appointment = appointmentDao.create(cliId, docId, date, APPOINTREASON, new Specialty(specId, "Cardiology"));

        //Exercise
        Optional<List<Appointment>> maybeAppointments = appointmentDao.getByClientId(cliId);

        //Postconditions
        assertTrue(maybeAppointments.isPresent());
        assertEquals(1, maybeAppointments.get().size());
        List<Long> appointments = maybeAppointments.get().stream().map(Appointment::getClientId).collect(Collectors.toList());
        assertTrue(appointments.contains(cliId));
    }

    @Test
    public void testGetByDoctorIdDoesNotExist() {
        //Preconditions

        //Exercise
        Optional<List<Appointment>> maybeAppointments = appointmentDao.getByDoctorId(9999L);

        //Postconditions
        assertNotNull(maybeAppointments);
        assertTrue(maybeAppointments.isEmpty());
    }

    @Test
    public void testGetByDoctorExists() {
        //Preconditions
        LocalDateTime date = LocalDateTime.now().plusDays(1);
        Appointment appointment = appointmentDao.create(cliId, docId, date, APPOINTREASON, new Specialty(specId, "Cardiology"));

        //Exercise
        Optional<List<Appointment>> maybeAppointments = appointmentDao.getByDoctorId(docId);

        //Postconditions
        assertTrue(maybeAppointments.isPresent());
        assertEquals(1, maybeAppointments.get().size());
        List<Long> appointments = maybeAppointments.get().stream().map(Appointment::getDoctorId).collect(Collectors.toList());
        assertTrue(appointments.contains(docId));
    }

    @Test
    public void testGetAllFutureAppointments() {
        //Preconditions
        LocalDateTime old_date = LocalDateTime.now().minusDays(1);;
        LocalDateTime new_date_1 = LocalDateTime.now().plusDays(1);
        LocalDateTime new_date_2 = LocalDateTime.now().plusDays(2);
        Appointment appointment_1 = appointmentDao.create(cliId, docId, old_date, APPOINTREASON, new Specialty(specId, "Cardiology"));
        Appointment appointment_2 = appointmentDao.create(cliId, docId, new_date_1, APPOINTREASON, new Specialty(specId, "Cardiology"));
        Appointment appointment_3 = appointmentDao.create(cliId, docId, new_date_2, APPOINTREASON, new Specialty(specId, "Cardiology"));

        //Exercise
        Optional<List<Appointment>> maybeAppointments = appointmentDao.getAllFutureAppointments(docId);

        //Postconditions
        assertTrue(maybeAppointments.isPresent());
        assertEquals(2, maybeAppointments.get().size());
        List<LocalDateTime> appointments = maybeAppointments.get().stream().map(Appointment::getDate).collect(Collectors.toList());
        assertTrue(appointments.containsAll(List.of(new_date_1, new_date_2)));
        assertFalse(appointments.contains(old_date));
    }

    //@TODO cancel && accept appointment
}
