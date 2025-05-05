package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.AvailabilitySlotsDao;
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
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class DoctorDaoTest {

    private final static String SQL_QUERY = "SELECT u.name AS doctor_name, u.id AS doctor_id, u.last_name AS doctor_last_name, " +
            "u.email AS doctor_email, u.password AS doctor_password, u.phone AS doctor_phone, " +
            "u.language AS doctor_language, d.rating AS rating, d.rating_count AS rating_count,d.image_id AS image_id,u.is_verified AS doctor_verified " +
            "FROM users u JOIN doctors d ON u.id = d.doctor_id WHERE u.id = ?";
    private final static String SQL_QUERY_AVAILABILITY = "SELECT * FROM doctor_availability WHERE doctor_id = ?";
    private final static String SQL_QUERY_SPECIALTY = "SELECT s.id, s.key FROM Doctor_Specialties ds JOIN Specialties s ON ds.specialty_id = s.id WHERE ds.doctor_id = ?";
    private final static String SQL_QUERY_COVERAGE = "SELECT c.id, c.coverage_name FROM Doctor_Coverages dc JOIN Coverages c ON dc.coverage_id = c.id WHERE dc.doctor_id = ?";

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
    private static final String TEST_EMAIL = "jane@example.com";
    private static final double TEST_RATING = 4.5;
    private static final long TEST_RATING_COUNT = 10L;

    private DoctorDaoImpl doctorDao;
    private AvailabilitySlotsDao mockAvailability;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsertUser;
    private SimpleJdbcInsert jdbcInsertDoctor;

    @Autowired
    private DataSource ds;
    private DaoUtils daoUtils;

    @Before
    public void setUp() {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.daoUtils = new DaoUtils();
        this.mockAvailability = mock(AvailabilitySlotsDao.class);
        this.doctorDao = new DoctorDaoImpl(ds, daoUtils, mockAvailability);
        this.jdbcInsertUser = new SimpleJdbcInsert(ds)
                .withTableName("Users")
                .usingGeneratedKeyColumns("id");
        this.jdbcInsertDoctor = new SimpleJdbcInsert(ds)
                .withTableName("Doctors");
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
        assertEquals(PASSWORD, doctor.getPassword());
        assertEquals(PHONE, doctor.getPhone());
        assertEquals(LANGUAGE, doctor.getLanguage());
        assertEquals(IMAGE_ID, doctor.getImageId());
        List<Specialty> querySpecialties = jdbcTemplate.query(SQL_QUERY_SPECIALTY,
                (rs, rowNum) -> new Specialty(
                        rs.getLong("id"),
                        rs.getString("key")
                ),
                TEST_ID
        );
        assertFalse(querySpecialties.isEmpty());
        assertEquals(querySpecialties.size(), specialties.size());
        List<Coverage> queryCoverages = jdbcTemplate.query(SQL_QUERY_COVERAGE,
                (rs, rowNum) -> new Coverage(
                        rs.getLong("id"),
                        rs.getString("coverage_name")
                ),
                TEST_ID
        );
        assertFalse(queryCoverages.isEmpty());
        assertEquals(coverages.size(), queryCoverages.size());
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
        Optional<Doctor> updatedDoctor = jdbcTemplate.query(SQL_QUERY,
                new Object[]{TEST_ID},
                daoUtils.getDoctorRowMapper()
        ).stream().findFirst();
        assertTrue(updatedDoctor.isPresent());
        assertEquals(finalRating, updatedDoctor.get().getRating(), 0.01);
    }

    @Test
    public void testGetByIdsDoNotExist() {
        //Preconditions
        long nonExistId = 1000L;

        //Exercise
        List<Doctor> doctors = doctorDao.getByIds(Set.of(nonExistId));

        //Postconditions
        assertTrue(doctors.isEmpty());
    }

    //@TODO: CHECK, SHOULD I ASK FOR MORE DOCTORS?
    @Test
    public void testGetByIdsExists() {
        //Preconditions
        Set<Long> ids = Set.of(TEST_ID);

        //Exercise
        List<Doctor> doctors = doctorDao.getByIds(ids);

        //Postconditions
        assertFalse(doctors.isEmpty());
        Doctor doctor = doctors.getFirst();
        assertEquals(1, doctors.size());
        assertEquals(TEST_ID, doctor.getId());
    }

    @Test
    public void testGetBySpecialtyEmpty() {
        //Preconditions
        long specialtyId = 3L;

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
        Optional<Doctor> updatedDoctor = jdbcTemplate.query(SQL_QUERY,
                new Object[]{TEST_ID},
                daoUtils.getDoctorRowMapper()
        ).stream().findFirst();
        assertTrue(updatedDoctor.isPresent());
        assertEquals(TEST_ID, updatedDoctor.get().getId());
        assertEquals(updatedName, updatedDoctor.get().getName());
        assertEquals(updatedLastName, updatedDoctor.get().getLastName());
        assertEquals(updatedPhone, updatedDoctor.get().getPhone());
        List<Specialty> finalSpecialties = jdbcTemplate.query(SQL_QUERY_SPECIALTY,
                (rs, rowNum) -> new Specialty(
                        rs.getLong("id"),
                        rs.getString("key")
                ),
                TEST_ID
        );
        assertFalse(finalSpecialties.isEmpty());
        assertEquals(updatedSpecialtyId, finalSpecialties.getFirst().getId());
        assertEquals(updatedSpecialtyName, finalSpecialties.getFirst().getKey());
        List<Coverage> finalCoverages = jdbcTemplate.query(SQL_QUERY_COVERAGE,
                (rs, rowNum) -> new Coverage(
                        rs.getLong("id"),
                        rs.getString("coverage_name")
                ),
                TEST_ID
        );
        assertFalse(finalCoverages.isEmpty());
        assertEquals(updatedCoverageId, finalCoverages.getFirst().getId());
        assertEquals(updatedCoverageName, finalCoverages.getFirst().getName());
    }

    //@TODO: SHOULDN'T FUNCTION RETURN OPTIONAL?
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
        Optional<Doctor> updatedDoctor = jdbcTemplate.query(SQL_QUERY,
                new Object[]{TEST_ID},
                daoUtils.getDoctorRowMapper()
        ).stream().findFirst();
        assertTrue(updatedDoctor.isPresent());
        assertEquals(newLanguage, updatedDoctor.get().getLanguage());
    }

    //@TODO: SHOULD I ADD MORE
    @Test
    public void testCountBySpecialty() {
        //Preconditions

        //Exercise
        int count = doctorDao.countBySpecialty(SPEC_ID);

        //Postconditions
        assertEquals(1, count);
    }

    //@TODO: FINISH
    @Test
    public void testGetWithFilters() {
        //Preconditions

        //Exercise

        //Postconditions
    }

    //@TODO: FINISH
    @Test
    public void testCountWithFilters() {

    }

    @Test
    public void testGetByVerificationToken() {
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
        jdbcInsertDoctor.execute(Map.of(
                "doctor_id", id,
                "rating", TEST_RATING,
                "rating_count", TEST_RATING_COUNT,
                "image_id", IMAGE_ID
        ));

        //Exercise
        Optional<Doctor> maybeDoctor = doctorDao.getByVerificationToken(verification_token);

        //Postconditions
        assertTrue(maybeDoctor.isPresent());
        assertEquals(id, maybeDoctor.get().getId());
        assertEquals(NAME, maybeDoctor.get().getName());
        assertEquals(LASTNAME, maybeDoctor.get().getLastName());
        assertEquals(EMAIL, maybeDoctor.get().getEmail());
        assertEquals(PASSWORD, maybeDoctor.get().getPassword());
        assertEquals(PHONE, maybeDoctor.get().getPhone());
        assertEquals(LANGUAGE, maybeDoctor.get().getLanguage());
    }

    @Test
    public void testGetByResetToken() {
        //Preconditions
        String reset_token = "RESETTOKEN";
        long id = jdbcInsertUser.executeAndReturnKey(Map.of(
                "name", NAME,
                "last_name", LASTNAME,
                "email", EMAIL,
                "password", PASSWORD,
                "phone", PHONE,
                "language", LANGUAGE,
                "reset_token", reset_token
        )).longValue();
        jdbcInsertDoctor.execute(Map.of(
                "doctor_id", id,
                "rating", TEST_RATING,
                "rating_count", TEST_RATING_COUNT,
                "image_id", IMAGE_ID
        ));

        //Exercise
        Optional<Doctor> maybeDoctor = doctorDao.getByResetToken(reset_token);

        //Postconditions
        assertTrue(maybeDoctor.isPresent());
        assertEquals(id, maybeDoctor.get().getId());
        assertEquals(NAME, maybeDoctor.get().getName());
        assertEquals(LASTNAME, maybeDoctor.get().getLastName());
        assertEquals(EMAIL, maybeDoctor.get().getEmail());
        assertEquals(PASSWORD, maybeDoctor.get().getPassword());
        assertEquals(PHONE, maybeDoctor.get().getPhone());
        assertEquals(LANGUAGE, maybeDoctor.get().getLanguage());
    }
}

