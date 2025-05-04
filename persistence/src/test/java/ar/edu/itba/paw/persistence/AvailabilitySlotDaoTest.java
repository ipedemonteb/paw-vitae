package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.AvailabilitySlotsDao;
import ar.edu.itba.paw.models.AvailabilitySlot;
import org.junit.Assert;
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
import java.sql.Time;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class AvailabilitySlotDaoTest {

    private final static String SQL_QUERY_AVAILABILITY = "SELECT * FROM doctor_availability WHERE doctor_id = ?";

    private static final long DOC_ID = 2L;

    private JdbcTemplate jdbcTemplate;
    private AvailabilitySlotsDao availabilitySlotsDao;
    private SimpleJdbcInsert jdbcInsertTimeAvailability;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.availabilitySlotsDao = new AvailabilitySlotDaoImpl(ds);
        this.jdbcInsertTimeAvailability = new SimpleJdbcInsert(ds)
                .withTableName("Doctor_Availability");
    }

    @Test
    public void testCreate() {
        //Preconditions
        int dayOfWeek = 1;
        int startTimeHour = 8;
        int startTimeMinute = 0;
        int endTimeHour = 16;
        AvailabilitySlot slot = new AvailabilitySlot(dayOfWeek,
                LocalTime.of(startTimeHour, startTimeMinute),
                LocalTime.of(endTimeHour, startTimeMinute)
        );

        //Exercise
        AvailabilitySlot createdSlot = availabilitySlotsDao.create(DOC_ID, slot);

        //Postconditions
        assertNotNull(createdSlot);
        assertEquals(dayOfWeek, createdSlot.getDayOfWeek());
        assertEquals(LocalTime.of(startTimeHour, startTimeMinute), createdSlot.getStartTime());
        assertEquals(LocalTime.of(endTimeHour, startTimeMinute), createdSlot.getEndTime());
    }

    @Test
    public void testUpdateDoctorAvailability() {
        //Preconditions
        List<AvailabilitySlot> availability = List.of(
                new AvailabilitySlot(3,
                        LocalTime.of(8, 0),
                        LocalTime.of(16, 0)),
                new AvailabilitySlot(4,
                        LocalTime.of(8, 0),
                        LocalTime.of(16, 0))
        );

        //Exercise
        availabilitySlotsDao.updateDoctorAvailability(DOC_ID, availability);

        //Postconditions
        List<AvailabilitySlot> finalAvailability = jdbcTemplate.query(SQL_QUERY_AVAILABILITY,
                (rs, rowNum) -> new AvailabilitySlot(
                        rs.getInt("day_of_week"),
                        rs.getTime("start_time").toLocalTime(),
                        rs.getTime("end_time").toLocalTime()
                ),
                DOC_ID
        );
        assertFalse(finalAvailability.isEmpty());
        assertEquals(2, finalAvailability.size());
    }

    @Test
    public void testGetAvailabilityByDoctorId() {
        //Preconditions
        int dayOfWeek = 2;
        jdbcInsertTimeAvailability.execute(Map.of(
                "doctor_id", DOC_ID,
                "day_of_week", dayOfWeek,
                "start_time", Time.valueOf(LocalTime.of(8, 0)),
                "end_time", Time.valueOf(LocalTime.of(16, 0))
        ));

        //Exercise
        List<AvailabilitySlot> finalAvailability = availabilitySlotsDao.getAvailabilityByDoctorId(DOC_ID);

        //Postconditions
        assertFalse(finalAvailability.isEmpty());
        assertEquals(2, finalAvailability.size());
    }
}
