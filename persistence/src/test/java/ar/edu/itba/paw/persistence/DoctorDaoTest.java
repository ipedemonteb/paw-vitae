package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.AvailabilitySlotsDao;
import ar.edu.itba.paw.interfacePersistence.UserDao;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.persistence.hibernate.DoctorDaoHibeImpl;
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

    private static final long SPEC_ID = 1L;
    private static final long COV_ID = 1L;

    private static final long TEST_ID = 2L;
    private static final String TEST_NAME = "Jane";
    private static final String TEST_LASTNAME = "Smith";
    private static final String TEST_EMAIL = "jane@example.com";
    private static final String TEST_PASSWORD = "hashedpassword";
    private static final String TEST_PHONE = "987654321";
    private static final String TEST_LANGUAGE = "es";
    private static final double TEST_RATING = 4.5;
    private static final long TEST_RATING_COUNT = 10L;

    @Autowired
    private DoctorDaoHibeImpl doctorDao;
    private JdbcTemplate jdbcTemplate;
    private UserDao userDao;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Rollback
    @Test
    public void testCreateDoctor() {
        //Preconditions
        String name = "Robert";
        String lastname = "Johnson";
        String email = "robert@test.com";
        String password = "hashedpassword";
        String phone = "123456789";
        String language = "es";
        long imageId = 1L;
        List<Specialty> specialties = List.of(
                em.getReference(Specialty.class, 1L),
                em.getReference(Specialty.class, 2L)
        );
        List<Coverage> coverages = List.of(
                em.getReference(Coverage.class, 1L),
                em.getReference(Coverage.class, 2L)
        );

        //Exercise
        Doctor doctor = doctorDao.create(name, lastname, email, password, phone, language, imageId, specialties, coverages);
        em.flush();

        //Postconditions
        assertNotNull(doctor);
        assertEquals(name, doctor.getName());
        assertEquals(lastname, doctor.getLastName());
        assertEquals(email, doctor.getEmail());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCTOR_TABLE, "doctor_id = " + doctor.getId()));
        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCSPEC_TABLE, "doctor_id = " + doctor.getId()));
        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCCOV_TABLE, "doctor_id = " + doctor.getId()));
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
    public void testCountAll() {
        //Preconditions

        //Exercise
        int count = doctorDao.countAll();

        //Postconditions
        assertEquals(3, count);
    }

//    DEPRECATED METHODS
//    @Rollback
//    @Test
//    public void testUpdateDoctorRating() {
//        //Preconditions
//        long newRating = 5L;
//        double finalRating = (TEST_RATING * TEST_RATING_COUNT + newRating) / (TEST_RATING_COUNT + 1);
//
//        //Exercise
//        doctorDao.UpdateDoctorRating(TEST_ID, newRating);
//        em.flush();
//
//        //Postconditions
//        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCTOR_TABLE, "doctor_id = " + TEST_ID + " AND rating = " + finalRating));
//    }
//
//    @Test
//    public void testUpdateDoctor() {
//        //Preconditions
//        List<Long> newSpecialties = List.of(1L, 2L);
//        List<Long> newCoverages = List.of(1L, 2L);
//
//        //Exercise
//        doctorDao.updateDoctor(TEST_ID, newSpecialties, newCoverages);
//
//        //Postconditions
//        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCSPEC_TABLE, "doctor_id = " + TEST_ID));
//        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCCOV_TABLE, "doctor_id = " + TEST_ID));
//    }
//
//    @Test
//    public void testUpdateImage() {
//        //Preconditions
//        long newImageId = 1L;
//
//        //Exercise
//        doctorDao.updateImage(TEST_ID, newImageId);
//
//        //Postconditions
//        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCTOR_TABLE, "doctor_id = " + TEST_ID + " AND image_id = " + newImageId));
//    }
}

