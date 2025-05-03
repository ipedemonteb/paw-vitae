package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Patient;
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

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class PatientDaoTest {

    private static final String SQL_QUERY = "SELECT u.name AS patient_name, u.id AS patient_id, u.last_name AS patient_last_name, " +
            "u.email AS patient_email, u.password AS patient_password, u.phone AS patient_phone, " +
            "u.language AS patient_language, cov.id AS coverage_id, cov.coverage_name, u.is_verified AS patient_verified " +
            "FROM Users u JOIN Clients c ON c.client_id = u.id " +
            "JOIN Coverages cov ON cov.id = c.coverage_id WHERE u.id = ?";

    private static final String NAME = "Juan Sebastián";
    private static final String LASTNAME = "Verón";
    private static final String EMAIL = "jsveron@edelp.com";
    private static final String PASSWORD = "password";
    private static final String PHONE = "1119681969";
    private static final String LANGUAGE = "es";

    private static final long TEST_ID = 1L;
    private static final String TEST_NAME = "John";
    private static final String TEST_LASTNAME = "Doe";
    private static final String TEST_EMAIL = "john@example.com";
    private static final String TEST_PASSWORD = "hashedpassword";
    private static final String TEST_PHONE = "123456789";
    private static final String TEST_LANGUAGE = "en";
    private static final long TEST_COVERAGE_ID = 1L;
    private static final String TEST_COVERAGE_NAME = "Coverage A";
    private static final long TEST_COVERAGE_ID_2 = 2L;
    private static final String TEST_COVERAGE_NAME_2 = "Coverage B";

    private PatientDaoImpl patientDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertUser;
    private SimpleJdbcInsert jdbcInsertPatient;
    private DaoUtils daoUtils;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        this.daoUtils = new DaoUtils();
        this.patientDao = new PatientDaoImpl(ds, daoUtils);
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsertUser = new SimpleJdbcInsert(ds)
                .withTableName("Users")
                .usingGeneratedKeyColumns("id");
        this.jdbcInsertPatient = new SimpleJdbcInsert(ds)
                .withTableName("Clients");
    }

    @Test
    public void testCreateClient() {
        //Preconditions
        Coverage coverage = new Coverage(TEST_COVERAGE_ID, TEST_COVERAGE_NAME);

        //Exercise
        Patient patient = patientDao.create(NAME, LASTNAME, EMAIL, PASSWORD, PHONE, LANGUAGE, coverage);

        //Postconditions
        assertNotNull(patient);
        assertEquals(EMAIL, patient.getEmail());
        assertEquals(NAME, patient.getName());
        assertEquals(PASSWORD, patient.getPassword());
        assertEquals(PHONE, patient.getPhone());
        assertEquals(LANGUAGE, patient.getLanguage());
        assertEquals(coverage.getId(), patient.getCoverage().getId());
    }

    @Test
    public void testGetByIdDoesNotExist() {
        //Preconditions

        //Exercise
        Optional<Patient> maybeClient = patientDao.getById(1000L);

        //Postconditions
        assertFalse(maybeClient.isPresent());
    }

    //@TODO: CHECK, SHOULD I MAKE A NEW INSERTION FOR THIS TEST?
    @Test
    public void testGetByIdExists() {
        //Preconditions

        //Exercise
        Optional<Patient> maybePatient = patientDao.getById(TEST_ID);

        //Postconditions
        assertTrue(maybePatient.isPresent());
        assertEquals(TEST_EMAIL, maybePatient.get().getEmail());
        assertEquals(TEST_NAME, maybePatient.get().getName());
        assertEquals(TEST_PASSWORD, maybePatient.get().getPassword());
        assertEquals(TEST_PHONE, maybePatient.get().getPhone());
        assertEquals(TEST_LANGUAGE, maybePatient.get().getLanguage());
        assertEquals(TEST_COVERAGE_ID, maybePatient.get().getCoverage().getId());
    }

    @Test
    public void testGetByEmailDoesNotExist() {
        //Preconditions
        String nonExistEmail = "nonExistEmail";

        //Exercise
        Optional<Patient> maybeClient = patientDao.getByEmail(nonExistEmail);

        //Postconditions
        assertFalse(maybeClient.isPresent());
    }

    @Test
    public void testGetByEmailExists() {
        //Preconditions

        //Exercise
        Optional<Patient> maybePatient = patientDao.getByEmail(TEST_EMAIL);

        //Postconditions
        assertTrue(maybePatient.isPresent());
        assertEquals(TEST_EMAIL, maybePatient.get().getEmail());
        assertEquals(TEST_NAME, maybePatient.get().getName());
        assertEquals(TEST_PASSWORD, maybePatient.get().getPassword());
        assertEquals(TEST_PHONE, maybePatient.get().getPhone());
        assertEquals(TEST_LANGUAGE, maybePatient.get().getLanguage());
        assertEquals(TEST_COVERAGE_ID, maybePatient.get().getCoverage().getId());
    }

    //@TODO: CHECK IF ITS OK
    @Test
    public void testUpdateClient() {
        //Preconditions
        String updatedName = "Jane";
        String updatedLastName = "Smith";
        String updatedPhone = "1188888888";
        Coverage updatedCoverage = new Coverage(TEST_COVERAGE_ID_2, TEST_COVERAGE_NAME_2);

        //Exercise
        patientDao.updatePatient(1L, updatedName, updatedLastName, updatedPhone, updatedCoverage);

        //Postconditions
        Optional<Patient> updatedClient = jdbcTemplate.query(SQL_QUERY,
                new Object[]{1L},
                daoUtils.getPatientRowMapper()
        ).stream().findFirst();
        assertTrue(updatedClient.isPresent());
        assertEquals(updatedName, updatedClient.get().getName());
        assertEquals(updatedLastName, updatedClient.get().getLastName());
        assertEquals(updatedPhone, updatedClient.get().getPhone());
        assertEquals(updatedCoverage.getId(), updatedClient.get().getCoverage().getId());
    }

    @Test
    public void testGetByIdsDoNotExist() {
        //Preconditions
        long nonExistId = 1000L;

        //Exercise
        List<Patient> patients = patientDao.getByIds(Set.of(nonExistId));

        //Postconditions
        assertTrue(patients.isEmpty());
    }

    //@TODO: CHECK, SHOULD I ASK FOR MORE PATIENTS?
    @Test
    public void testGetByIdsExists() {
                            //Preconditions
                            Set<Long> ids = Set.of(TEST_ID);

                            //Exercise
                            List<Patient> patients = patientDao.getByIds(ids);

                            //Postconditions
                            assertFalse(patients.isEmpty());
                            Patient patient = patients.getFirst();
                            assertEquals(1, patients.size());
                            assertEquals(TEST_ID, patient.getId());
    }

//    @Test
//    public void testUpdatePassword() {
//        //Preconditions
//        String newPassword = "newpassword";
//
//        //Exercise
//        patientDao.changePassword(TEST_ID, newPassword);
//
//        //Postconditions
//        Optional<Patient> updatedPatient = jdbcTemplate.query(SQL_QUERY,
//                new Object[]{TEST_ID},
//                daoUtils.getPatientRowMapper()
//        ).stream().findFirst();
//        assertTrue(updatedPatient.isPresent());
//        assertEquals(newPassword, updatedPatient.get().getPassword());
//    }

    @Test
    public void testGetLanguage() {
        //Preconditions
        String expectedLanguage = "en";

        //Exercise
        String language = patientDao.getLanguage(TEST_ID);

        //Postconditions
        assertEquals(expectedLanguage, language);
    }

    @Test
    public void testGetByVerificationTokenDoesNotExist() {
        //Preconditions
        String nonExistToken = "NOTOKEN";

        //Exercise
        Optional<Patient> maybePatient = patientDao.getByVerificationToken(nonExistToken);

        //Postconditions
        assertFalse(maybePatient.isPresent());
    }

    @Test
    public void testGetByVerificationTokenExists() {
        //Preconditions
        String verification_token = "VERIFICATIONTOKEN";
        long id = jdbcInsertUser.executeAndReturnKey(Map.of(
                "name", NAME,
                "last_name", LASTNAME,
                "email", EMAIL,
                "password", PASSWORD,
                "phone", PHONE,
                "language", LANGUAGE,
                "verification_token", verification_token
        )).longValue();
        jdbcInsertPatient.execute(Map.of(
                "client_id", id,
                "coverage_id", TEST_COVERAGE_ID
        ));

        //Exercise
        Optional<Patient> maybePatient = patientDao.getByVerificationToken(verification_token);

        //Postconditions
        assertTrue(maybePatient.isPresent());
        assertEquals(id, maybePatient.get().getId());
        assertEquals(NAME, maybePatient.get().getName());
        assertEquals(LASTNAME, maybePatient.get().getLastName());
        assertEquals(EMAIL, maybePatient.get().getEmail());
        assertEquals(PASSWORD, maybePatient.get().getPassword());
        assertEquals(PHONE, maybePatient.get().getPhone());
        assertEquals(LANGUAGE, maybePatient.get().getLanguage());
        assertEquals(TEST_COVERAGE_ID, maybePatient.get().getCoverage().getId());
    }

    @Test
    public void testChangeLanguage() {
        //Preconditions
        String newLanguage = "es";

        //Exercise
        patientDao.changeLanguage(TEST_ID, newLanguage);

        //Postconditions
        Optional<Patient> updatedPatient = jdbcTemplate.query(SQL_QUERY,
                new Object[]{TEST_ID},
                daoUtils.getPatientRowMapper()
        ).stream().findFirst();
        assertTrue(updatedPatient.isPresent());
        assertEquals(newLanguage, updatedPatient.get().getLanguage());
    }
}