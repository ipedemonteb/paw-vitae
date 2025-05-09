package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.RatingDao;
import ar.edu.itba.paw.models.Rating;
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
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class RatingDaoTest {

    private static final String RATINGS_TABLE = "ratings";

    private RatingDao ratingDao;
    private JdbcTemplate jdbcTemplate;

    private static final long RAT_ID = 1L;
    private static final long PAT_ID = 1L;
    private static final long DOC_ID = 2L;
    private static final long APP_ID = 1L;
    private static final int RATING = 5;
    private static final String COMMENT = "Excelente atención";

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        ratingDao = new RatingDaoImpl(ds);
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testCreate() {
        // Preconditions
        long score = 4;
        long doctorId = 2L,
             patientId = 1L,
             appointmentId = 2L;
        String comment = "Great doctor!";

        // Exercise
        Rating rating = ratingDao.create(score, doctorId, patientId, appointmentId, comment);

        //Postconditions
        assertNotNull(rating);
        assertEquals(score, rating.getRating());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, RATINGS_TABLE, "doctor_id = " + doctorId + " AND client_id = " + patientId + " AND appointment_id = " + appointmentId));
    }

    @Test
    public void testGetRatingDoesNotExist() {
        //Preconditions

        //Excercise
        Optional<Rating> maybeRating = ratingDao.getRating(1000);

        //Postconditions
        assertFalse(maybeRating.isPresent());
    }

    @Test
    public void testGetRatingExists() {
        //Preconditions
        long ratingId = 1L;

        //Exercise
        Optional<Rating> maybeRating = ratingDao.getRating(ratingId);

        //Postconditions
        assertTrue(maybeRating.isPresent());
        assertEquals(ratingId, maybeRating.get().getId());
        assertEquals(PAT_ID, maybeRating.get().getPatientId());
        assertEquals(DOC_ID, maybeRating.get().getDoctorId());
        assertEquals(APP_ID, maybeRating.get().getAppointmentId());
        assertEquals(RATING, maybeRating.get().getRating());
        assertEquals(COMMENT, maybeRating.get().getComment());
    }

    @Test
    public void testGetRatingByAppointmentIdDoesNotExist() {
        //Preconditions

        //Exercise
        Optional<Rating> maybeRating = ratingDao.getRatingByAppointmentId(1000L);

        //Postconditions
        assertFalse(maybeRating.isPresent());
    }

    @Test
    public void testGetRatingByAppointmentIdExists() {
        //Preconditions
        long appointmentId = 1L;

        //Exercise
        Optional<Rating> maybeRating = ratingDao.getRatingByAppointmentId(appointmentId);

        //Postconditions
        assertTrue(maybeRating.isPresent());
        assertEquals(appointmentId, maybeRating.get().getAppointmentId());
        assertEquals(PAT_ID, maybeRating.get().getPatientId());
        assertEquals(DOC_ID, maybeRating.get().getDoctorId());
        assertEquals(RATING, maybeRating.get().getRating());
        assertEquals(COMMENT, maybeRating.get().getComment());
    }

    @Test
    public void testGetRatingByDoctorIdDoesNotExist() {
        //Preconditions

        //Exercise
        List<Rating> maybeRating = ratingDao.getRatingsByDoctorId(1000L);

        //Postconditions
        assertTrue(maybeRating.isEmpty());
    }

    @Test
    public void testGetRatingByDoctorIdExists() {
        //Preconditions
        long doctorId = 2L;

        //Exercise
        List<Rating> maybeRatings = ratingDao.getRatingsByDoctorId(doctorId);

        //Postconditions
        assertFalse(maybeRatings.isEmpty());
        assertEquals(1, maybeRatings.size());
        Rating result = maybeRatings.getFirst();
        assertEquals(RAT_ID, result.getId());
        assertEquals(doctorId, result.getDoctorId());
        assertEquals(PAT_ID, result.getPatientId());
        assertEquals(APP_ID, result.getAppointmentId());
        assertEquals(RATING, result.getRating());
        assertEquals(COMMENT, result.getComment());
    }

    @Test
    public void testGetRatingByPatientIdDoesNotExist() {
        //Preconditions

        //Exercise
        List<Rating> maybeRating = ratingDao.getRatingsByPatientId(1000L);

        //Postconditions
        assertTrue(maybeRating.isEmpty());
    }

    @Test
    public void testGetRatingByPatientIdExists() {
        //Preconditions
        long patientId = 1L;

        //Exercise
        List<Rating> maybeRatings = ratingDao.getRatingsByPatientId(patientId);

        //Postconditions
        assertFalse(maybeRatings.isEmpty());
        assertEquals(1, maybeRatings.size());
        Rating result = maybeRatings.getFirst();
        assertEquals(RAT_ID, result.getId());
        assertEquals(DOC_ID, result.getDoctorId());
        assertEquals(patientId, result.getPatientId());
        assertEquals(APP_ID, result.getAppointmentId());
        assertEquals(RATING, result.getRating());
        assertEquals(COMMENT, result.getComment());
    }

    @Test
    public void testGetFiveTopRatings() {
        //Preconditions

        //Exercise
        List<Rating> results = ratingDao.getFiveTopRatings();

        //Postconditions
        assertFalse(results.isEmpty());
        assertEquals(5, results.size());
        assertTrue(results.getFirst().getRating() >= results.getLast().getRating());
        for (int i = 0; i < results.size() - 1; i++) {
            assertTrue(results.get(i).getRating() >= results.get(i + 1).getRating());
        }
    }
}
