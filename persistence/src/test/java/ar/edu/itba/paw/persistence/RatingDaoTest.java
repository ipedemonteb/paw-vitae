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
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class RatingDaoTest {

    private RatingDao ratingDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

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
        jdbcInsert = new SimpleJdbcInsert(ds);
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
        assertEquals(doctorId, rating.getDoctorId());
        assertEquals(patientId, rating.getPatientId());
        assertEquals(appointmentId, rating.getAppointmentId());
        assertEquals(score, rating.getRating());
        assertEquals(comment, rating.getComment());
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
    public void getRatingByAppointmentIdDoesNotExist() {
        //Preconditions

        //Exercise
        Optional<Rating> maybeRating = ratingDao.getRatingByAppointmentId(1000L);

        //Postconditions
        assertFalse(maybeRating.isPresent());
    }

    @Test
    public void getRatingByAppointmentIdExists() {
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
    public void getRatingByDoctorIdDoesNotExist() {
        //Preconditions

        //Exercise
        List<Rating> maybeRating = ratingDao.getRatingsByDoctorId(1000L);

        //Postconditions
        assertTrue(maybeRating.isEmpty());
    }

    //@TODO:CHECK
    @Test
    public void getRatingByDoctorIdExists() {
        //Preconditions
//        long score = 4;
        long doctorId = 2L;
//        long patientId = 1L;
//        long appointmentId = 2L;
//        String comment = "Great doctor!";
//        Number number = jdbcInsert.executeAndReturnKey(Map.of(
//                "id", 2L,
//                "doctor_id", doctorId,
//                "client_id", patientId,
//                "appointment_id", appointmentId,
//                "rating", score,
//                "comment", comment
//        ));

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
    public void getRatingByPatientIdDoesNotExist() {
        //Preconditions

        //Exercise
        List<Rating> maybeRating = ratingDao.getRatingsByPatientId(1000L);

        //Postconditions
        assertTrue(maybeRating.isEmpty());
    }

    @Test
    public void getRatingByPatientIdExists() {
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
}
