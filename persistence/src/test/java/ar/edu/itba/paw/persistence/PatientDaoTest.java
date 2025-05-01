//package ar.edu.itba.paw.persistence;
//
//import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
//import ar.edu.itba.paw.models.Coverage;
//import ar.edu.itba.paw.models.Patient;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.jdbc.JdbcTestUtils;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.sql.DataSource;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.Assert.*;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestConfig.class)
//@Transactional
//public class PatientDaoTest {
//
//    private static final String NAME = "Carlos Salvador";
//    private static final String LASTNAME = "Bilardo";
//    private static final String EMAIL = "carlos.bilardo@edelp.com";
//    private static final String PASSWORD = "password";
//    private static final String PHONE = "1177777777";
//    private static final String LANGUAGE = "es";
//
//    private static final long TEST_ID = 1L;
//    private static final String TEST_NAME = "John";
//    private static final String TEST_LASTNAME = "Doe";
//    private static final String TEST_EMAIL = "john@example.com";
//    private static final String TEST_PASSWORD = "hashedpassword";
//    private static final String TEST_PHONE = "123456789";
//    private static final String TEST_LANGUAGE = "en";
//    private static final long TEST_COVERAGE_ID = 1L;
//    private static final String TEST_COVERAGE_NAME = "Coverage A";
//    private static final long TEST_COVERAGE_ID_2 = 2L;
//    private static final String TEST_COVERAGE_NAME_2 = "Coverage B";
//
//    private PatientDaoImpl patientDao;
//    private JdbcTemplate jdbcTemplate;
//
//    @Autowired
//    private DataSource ds;
//
//    private AppointmentDao mockAppointmentDao;
//
//    @Before
//    public void setUp() {
//        mockAppointmentDao = mock(AppointmentDao.class);
//        when(mockAppointmentDao.getByPatientId(anyLong())).thenReturn(List.of());
//        patientDao = new PatientDaoImpl(ds);
//        jdbcTemplate = new JdbcTemplate(ds);
//    }
//
//    @Test
//    public void testCreateClient() {
//        //Preconditions
//        Coverage coverage = new Coverage(TEST_COVERAGE_ID, TEST_COVERAGE_NAME);
//
//        //Exercise
//        Patient patient = patientDao.create(NAME, LASTNAME, EMAIL, PASSWORD, PHONE, LANGUAGE, coverage);
//
//        //Postconditions
//        assertNotNull(patient);
//        assertEquals(EMAIL, patient.getEmail());
//        assertEquals(NAME, patient.getName());
//        assertEquals(PASSWORD, patient.getPassword());
//        assertEquals(PHONE, patient.getPhone());
//        assertEquals(LANGUAGE, patient.getLanguage());
//        assertEquals(coverage.getId(), patient.getCoverage().getId());
//    }
//
//    @Test
//    public void testGetByIdDoesNotExist() {
//        //Preconditions
//
//        //Exercise
//        Optional<Patient> maybeClient = patientDao.getById(1000L);
//
//        //Postconditions
//        assertFalse(maybeClient.isPresent());
//    }
//
//    @Test
//    public void testGetByIdExists() {
//        //Preconditions
//
//        //Exercise
//        Optional<Patient> maybePatient = patientDao.getById(TEST_ID);
//
//        //Postconditions
//        assertTrue(maybePatient.isPresent());
//        assertEquals(TEST_EMAIL, maybePatient.get().getEmail());
//        assertEquals(TEST_NAME, maybePatient.get().getName());
//        assertEquals(TEST_PASSWORD, maybePatient.get().getPassword());
//        assertEquals(TEST_PHONE, maybePatient.get().getPhone());
//        assertEquals(TEST_LANGUAGE, maybePatient.get().getLanguage());
//        assertEquals(TEST_COVERAGE_ID, maybePatient.get().getCoverage().getId());
//    }
//
//    @Test
//    public void testGetByEmailDoesNotExist() {
//        //Preconditions
//
//        //Exercise
//        Optional<Patient> maybeClient = patientDao.getByEmail("notexists@gmail.com");
//
//        //Postconditions
//        assertFalse(maybeClient.isPresent());
//    }
//
//    @Test
//    public void testGetByEmailExists() {
//        //Preconditions
//
//        //Exercise
//        Optional<Patient> maybePatient = patientDao.getByEmail(TEST_EMAIL);
//
//        //Postconditions
//        assertTrue(maybePatient.isPresent());
//        assertEquals(TEST_EMAIL, maybePatient.get().getEmail());
//        assertEquals(TEST_NAME, maybePatient.get().getName());
//        assertEquals(TEST_PASSWORD, maybePatient.get().getPassword());
//        assertEquals(TEST_PHONE, maybePatient.get().getPhone());
//        assertEquals(TEST_LANGUAGE, maybePatient.get().getLanguage());
//        assertEquals(TEST_COVERAGE_ID, maybePatient.get().getCoverage().getId());
//    }

//
//    @Test
//    public void testUpdateClient() {
//        //Preconditions
//        String updatedName = "Jane";
//        String updatedLastName = "Smith";
//        String updatedPhone = "1188888888";
//        Coverage updatedCoverage = new Coverage(TEST_COVERAGE_ID_2, TEST_COVERAGE_NAME_2);
//
//        //Exercise
//        patientDao.updatePatient(1L, updatedName, updatedLastName, updatedPhone, updatedCoverage);
//
//        //Postconditions
//        Optional<Patient> maybeClient = clientDao.getById(client.getId());
//        assertTrue(maybeClient.isPresent());
//        Patient updatedClient = maybeClient.get();
//        assertEquals(updatedName, updatedClient.getName());
//        assertEquals(updatedLastName, updatedClient.getLastName());
//        assertEquals(updatedPhone, updatedClient.getPhone());
//        assertEquals(updatedCoverage.getId(), updatedClient.getCoverage().getId());
//    }
//
////    //@TODO: getbyids
//}