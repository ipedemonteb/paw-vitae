package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.AvailabilitySlotsDao;
import ar.edu.itba.paw.interfacePersistence.UserDao;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class DoctorDaoTest {

    private static final String DOCSPEC_TABLE = "doctor_specialties";
    private static final String DOCCOV_TABLE = "doctor_coverages";
    private static final String DOCTOR_TABLE = "doctors";
    private static final String USER_TABLE = "users";

    private static final String NAME = "Carlos Salvador";
    private static final String LASTNAME = "Bilardo";
    private static final String EMAIL = "csbilardo@edelp.com";
    private static final String PASSWORD = "password";
    private static final String PHONE = "1177777777";
    private static final String LANGUAGE = "es";
    private static final long SPEC_ID = 1L;
    private static final String SPEC_NAME = "Cardiology";
    private static final long COV_ID = 1L;
    private static final String COV_NAME = "OSDE";
    private static final long IMAGE_ID = 1L;

    private static final long TEST_ID = 2L;
    private static final String TEST_NAME = "Jane";
    private static final String TEST_LASTNAME = "Smith";
    private static final String TEST_EMAIL = "jane@example.com";
    private static final String TEST_PASSWORD = "hashedpassword";
    private static final String TEST_PHONE = "987654321";
    private static final String TEST_LANGUAGE = "es";
    private static final double TEST_RATING = 4.5;
    private static final long TEST_RATING_COUNT = 10L;

    private DoctorDaoImpl doctorDao;
    private JdbcTemplate jdbcTemplate;
    private UserDao userDao;


    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.userDao = mock(UserDao.class);
        when(userDao.create(anyString(), anyString(), anyString(), anyString(), anyString(), anyString()))
                .thenReturn(6L);
        this.doctorDao = new DoctorDaoImpl(ds, userDao);
    }

    @Test
    public void testCreateDoctor() {
        //Preconditions
        List<Specialty> specialties = List.of(new Specialty(SPEC_ID, SPEC_NAME));
        List<Coverage> coverages = List.of(new Coverage(COV_ID, COV_NAME));

        //Exercise
        Doctor doctor = doctorDao.create(NAME, LASTNAME, EMAIL, PASSWORD, PHONE, LANGUAGE, IMAGE_ID, specialties, coverages);

        //Postconditions
        assertNotNull(doctor);
        assertEquals(NAME, doctor.getName());
        assertEquals(LASTNAME, doctor.getLastName());
        assertEquals(EMAIL, doctor.getEmail());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCTOR_TABLE, "doctor_id = " + doctor.getId()));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCSPEC_TABLE, "doctor_id = " + doctor.getId() + " AND specialty_id = " + SPEC_ID));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCCOV_TABLE, "doctor_id = " + doctor.getId() + " AND coverage_id = " + COV_ID));
    }

    @Test
    public void testGetByIdDoesNotExist() {
        //Preconditions

        //Exercise
        Optional<Doctor> maybeDoctor = doctorDao.getById(1000L);

        //Postconditions
        assertFalse(maybeDoctor.isPresent());
    }

    @Test
    public void testGetByIdExists() {
        //Preconditions

        //Exercise
        Optional<Doctor> maybeDoctor = doctorDao.getById(TEST_ID);

        //Postconditions
        assertTrue(maybeDoctor.isPresent());
        Doctor doctor = maybeDoctor.get();
        assertEquals(TEST_ID, doctor.getId());
    }

    @Test
    public void testGetByEmailDoesNotExist() {
        //Preconditions

        //Excercise
        Optional<Doctor> maybeDoctor = doctorDao.getByEmail("notexists@gmail.com");

        //Postconditions
        assertFalse(maybeDoctor.isPresent());
    }

    @Test
    public void testGetByEmailExists() {
        //Preconditions

        //Exercise
        Optional<Doctor> maybeDoctor = doctorDao.getByEmail(TEST_EMAIL);

        //Postconditions
        assertTrue(maybeDoctor.isPresent());
        Doctor doctor = maybeDoctor.get();
        assertEquals(TEST_EMAIL, doctor.getEmail());
    }

    @Test
    public void testUpdateDoctorRating() {
        //Preconditions
        long newRating = 5L;
        double finalRating = (TEST_RATING * TEST_RATING_COUNT + newRating) / (TEST_RATING_COUNT + 1);

        //Exercise
        doctorDao.UpdateDoctorRating(TEST_ID, newRating);

        //Postconditions
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCTOR_TABLE, "doctor_id = " + TEST_ID + " AND rating = " + finalRating));
    }

    @Test
    public void testGetBySpecialtyEmpty() {
        //Preconditions
        long specialtyId = 4L;

        //Exercise
        List<Doctor> doctors = doctorDao.getBySpecialty(specialtyId, 1, 1);

        //Postconditions
        assertTrue(doctors.isEmpty());
    }

    @Test
    public void testGetBySpecialtyExists() {
        //Preconditions

        //Exercise
        List<Doctor> doctors = doctorDao.getBySpecialty(SPEC_ID, 1, 1);

        //Postconditions
        assertFalse(doctors.isEmpty());
        assertEquals(1, doctors.size());
        assertEquals(TEST_ID, doctors.getFirst().getId());

    }

    @Test
    public void testUpdateDoctor() {
        //Preconditions
        String updatedName = "Osvaldo";
        String updatedLastName = "Zubeldía";
        String updatedPhone = "1122222222";
        long updatedSpecialtyId = 2L;
        String updatedSpecialtyName = "Neurology";
        long updatedCoverageId = 2L;
        String updatedCoverageName = "Coverage B";
        List<Specialty> updatedSpecialties = List.of(new Specialty(updatedSpecialtyId, updatedSpecialtyName));
        List<Coverage> updatedCoverages = List.of(new Coverage(updatedCoverageId, updatedCoverageName));

        //Exercise
        doctorDao.updateDoctor(TEST_ID, updatedName, updatedLastName, updatedPhone, updatedSpecialties, updatedCoverages);

        //Postconditions
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "id = " + TEST_ID +
                " AND name = '" + updatedName + "' AND last_name = '" + updatedLastName + "' AND phone = '" + updatedPhone + "'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCSPEC_TABLE, "doctor_id = " + TEST_ID + " AND specialty_id = " + updatedSpecialtyId));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCCOV_TABLE, "doctor_id = " + TEST_ID + " AND coverage_id = " + updatedCoverageId));
    }

    @Test
    public void testGetLanguage() {
        //Preconditions

        //Exercise
        String language = doctorDao.getLanguage(TEST_ID);

        //Postconditions
        assertEquals(LANGUAGE, language);
    }

    @Test
    public void testChangeLanguage() {
        //Preconditions
        String newLanguage = "en";

        //Exercise
        doctorDao.changeLanguage(TEST_ID, newLanguage);

        //Postconditions
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, USER_TABLE, "id = " + TEST_ID + " AND language = '" + newLanguage + "'"));
    }

    @Test
    public void testCountBySpecialty() {
        //Preconditions

        //Exercise
        int count = doctorDao.countBySpecialty(SPEC_ID);

        //Postconditions
        assertEquals(2, count);
    }

    @Test
    public void testGetWithFilters() {
        //Preconditions
        List<Integer> days = List.of(0, 1);
        String order = "name";
        String direction = "asc";

        //Exercise
        List<Doctor> doctors = doctorDao.getWithFilters(SPEC_ID, COV_ID, days, order, direction, 1, 4);

        //Postconditions
        assertFalse(doctors.isEmpty());
        assertEquals(2, doctors.size());
        assertEquals(TEST_ID, doctors.getFirst().getId());
        assertEquals(5L, doctors.getLast().getId());
    }

    @Test
    public void testCountWithFilters() {
        //Preconditions
        //al populator
        List<Integer> days = List.of(0, 1);
        String order = "name";
        String direction = "asc";

        //Exercise
        int count = doctorDao.countWithFilters(SPEC_ID, COV_ID, days, order, direction);

        //Postconditions
        assertEquals(2, count);
    }

    @Test
    public void testGetByVerificationToken() {
        //Preconditions
        String verification_token = "VERIFTOKEN2";

        //Exercise
        Optional<Doctor> maybeDoctor = doctorDao.getByVerificationToken(verification_token);

        //Postconditions
        assertTrue(maybeDoctor.isPresent());
        assertEquals(TEST_NAME, maybeDoctor.get().getName());
        assertEquals(TEST_LASTNAME, maybeDoctor.get().getLastName());
        assertEquals(TEST_EMAIL, maybeDoctor.get().getEmail());
        assertEquals(TEST_PASSWORD, maybeDoctor.get().getPassword());
        assertEquals(TEST_PHONE, maybeDoctor.get().getPhone());
        assertEquals(TEST_LANGUAGE, maybeDoctor.get().getLanguage());
    }

    @Test
    public void testGetByResetToken() {
        //Preconditions
        String verification_token = "RESTOKEN2";

        //Exercise
        Optional<Doctor> maybeDoctor = doctorDao.getByResetToken(verification_token);

        //Postconditions
        assertTrue(maybeDoctor.isPresent());
        assertEquals(TEST_NAME, maybeDoctor.get().getName());
        assertEquals(TEST_LASTNAME, maybeDoctor.get().getLastName());
        assertEquals(TEST_EMAIL, maybeDoctor.get().getEmail());
        assertEquals(TEST_PASSWORD, maybeDoctor.get().getPassword());
        assertEquals(TEST_PHONE, maybeDoctor.get().getPhone());
        assertEquals(TEST_LANGUAGE, maybeDoctor.get().getLanguage());
    }

    @Test
    public void testUpdateImage() {
        //Preconditions
        long newImageId = 1L;

        //Exercise
        doctorDao.updateImage(TEST_ID, newImageId);

        //Postconditions
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCTOR_TABLE, "doctor_id = " + TEST_ID + " AND image_id = " + newImageId));
    }
}

