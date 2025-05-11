package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.CoverageDao;
import ar.edu.itba.paw.interfacePersistence.UserDao;
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
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class PatientDaoTest {

    private static final String PATIENT_TABLE = "clients";
    private static final String USER_TABLE = "users";

    private static final long TEST_ID = 1L;
    private static final long TEST_ID2 = 3L;
    private static final String TEST_NAME = "John";
    private static final String TEST_LASTNAME = "Doe";
    private static final String TEST_EMAIL = "john@example.com";
    private static final String TEST_PASSWORD = "hashedpassword";
    private static final String TEST_PHONE = "123456789";
    private static final String TEST_LANGUAGE = "en";
    private static final long TEST_COVERAGE_ID = 1L;
    private static final String TEST_COVERAGE_NAME = "Coverage A";

    private PatientDaoImpl patientDao;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        this.patientDao = new PatientDaoImpl(ds);
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testCreateClient() {
        //Preconditions
        long userId = 6L;
        String name = "John";
        String lastname = "Morgan";
        String email = "johnmorgan@test.com";
        String password = "hashedpassword";
        String phone = "123456789";
        String language = "es";
        Coverage coverage = new Coverage(TEST_COVERAGE_ID, TEST_COVERAGE_NAME);

        //Exercise
        Patient patient = patientDao.create(userId, name, lastname, email, password, phone, language, coverage);

        //Postconditions
        assertNotNull(patient);
        assertEquals(email, patient.getEmail());
        assertEquals(name, patient.getName());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, PATIENT_TABLE, "client_id = " + patient.getId()));
    }

    @Test
    public void testGetByIdDoesNotExist() {
        //Preconditions

        //Exercise
        Optional<Patient> maybeClient = patientDao.getById(1000L);

        //Postconditions
        assertFalse(maybeClient.isPresent());
    }

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

    @Test
    public void testUpdatePatient() {
        //Preconditions
        long patientId = 1L;
        long newCoverageId = 2L;

        //Exercise
        patientDao.updatePatient(patientId, newCoverageId);

        //Postconditions
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, PATIENT_TABLE, "client_id = " + patientId + " AND coverage_id = " + newCoverageId));
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

    @Test
    public void testGetByIdsExists() {
        //Preconditions
        Set<Long> ids = Set.of(TEST_ID, TEST_ID2);

        //Exercise
        List<Patient> patients = patientDao.getByIds(ids);

        //Postconditions
        assertFalse(patients.isEmpty());
        assertEquals(2, patients.size());
        assertEquals(TEST_ID, patients.getFirst().getId());
        assertEquals(TEST_ID2, patients.getLast().getId());
    }

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
        String verificationToken = "VERIFTOKEN";

        //Exercise
        Optional<Patient> maybePatient = patientDao.getByVerificationToken(verificationToken);

        //Postconditions
        assertTrue(maybePatient.isPresent());
        assertEquals(TEST_ID, maybePatient.get().getId());
        assertEquals(TEST_NAME, maybePatient.get().getName());
        assertEquals(TEST_LASTNAME, maybePatient.get().getLastName());
        assertEquals(TEST_EMAIL, maybePatient.get().getEmail());
        assertEquals(TEST_PASSWORD, maybePatient.get().getPassword());
        assertEquals(TEST_PHONE, maybePatient.get().getPhone());
        assertEquals(TEST_LANGUAGE, maybePatient.get().getLanguage());
        assertEquals(TEST_COVERAGE_ID, maybePatient.get().getCoverage().getId());
    }

    @Test
    public void testChangeLanguage() {
        //Preconditions
        String newLanguage = "es";

        //Exercise
        patientDao.changeLanguage(TEST_ID, newLanguage);

        //Postconditions
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "id = " + TEST_ID + "AND language = '" + newLanguage + "'"));
    }

    @Test
    public void testGetByResetTokenDoesNotExist() {
        //Preconditions
        String nonExistToken = "NOTOKEN";

        //Exercise
        Optional<Patient> maybePatient = patientDao.getByResetToken(nonExistToken);

        //Postconditions
        assertFalse(maybePatient.isPresent());
    }

    @Test
    public void testGetByResetTokenExists() {
        //Preconditions
        String resetToken = "RESTOKEN";

        //Exercise
        Optional<Patient> maybePatient = patientDao.getByResetToken(resetToken);

        //Postconditions
        assertTrue(maybePatient.isPresent());
        assertEquals(TEST_ID, maybePatient.get().getId());
        assertEquals(TEST_NAME, maybePatient.get().getName());
        assertEquals(TEST_LASTNAME, maybePatient.get().getLastName());
        assertEquals(TEST_EMAIL, maybePatient.get().getEmail());
        assertEquals(TEST_PASSWORD, maybePatient.get().getPassword());
        assertEquals(TEST_PHONE, maybePatient.get().getPhone());
        assertEquals(TEST_LANGUAGE, maybePatient.get().getLanguage());
        assertEquals(TEST_COVERAGE_ID, maybePatient.get().getCoverage().getId());
    }
}