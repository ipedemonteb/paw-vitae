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
import org.springframework.test.jdbc.JdbcTestUtils;
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

    private static final String DOCAVAILABILITY_TABLE = "doctor_availability";

    private static final long DOC_ID = 2L;

    private JdbcTemplate jdbcTemplate;
    private AvailabilitySlotsDao availabilitySlotsDao;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        this.jdbcTemplate = new JdbcTemplate(ds);
        this.availabilitySlotsDao = new AvailabilitySlotDaoImpl(ds);
    }

    @Test
    public void testCreate() {
        //Preconditions
        int dayOfWeek = 1,
                startTimeHour = 8,
                startTimeMinute = 0,
                endTimeHour = 16;
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
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCAVAILABILITY_TABLE, "doctor_id = " + DOC_ID +
                " AND day_of_week = " + dayOfWeek +
                " AND start_time = '" + Time.valueOf(LocalTime.of(startTimeHour, startTimeMinute)) + "'" +
                " AND end_time = '" + Time.valueOf(LocalTime.of(endTimeHour, startTimeMinute)) + "'"));
    }

    @Test
    public void testUpdateDoctorAvailability() {
        //Preconditions
        int dayOfWeek1 = 3;
        int dayOfWeek2 = 4;
        int startTimeHour = 8;
        int startTimeMinute = 0;
        int endTimeHour = 16;
        List<AvailabilitySlot> availability = List.of(
                new AvailabilitySlot(dayOfWeek1,
                        LocalTime.of(startTimeHour, startTimeMinute),
                        LocalTime.of(endTimeHour, startTimeMinute)),
                new AvailabilitySlot(dayOfWeek2,
                        LocalTime.of(startTimeHour, startTimeMinute),
                        LocalTime.of(endTimeHour, startTimeMinute))
        );

        //Exercise
        availabilitySlotsDao.updateDoctorAvailability(DOC_ID, availability);

        //Postconditions
        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCAVAILABILITY_TABLE, "doctor_id = " + DOC_ID +
                " AND start_time = '" + Time.valueOf(LocalTime.of(startTimeHour, startTimeMinute)) + "'" +
                " AND end_time = '" + Time.valueOf(LocalTime.of(endTimeHour, startTimeMinute)) + "'"));
    }

    @Test
    public void testGetAvailabilityByDoctorIdDoesNotExist() {
        //Preconditions
        long docId = 1000L;

        //Exercise
        List<AvailabilitySlot> finalAvailability = availabilitySlotsDao.getAvailabilityByDoctorId(docId);

        //Postconditions
        assertTrue(finalAvailability.isEmpty());
    }

    @Test
    public void testGetAvailabilityByDoctorIdExists() {
        //Preconditions
        long docId = 4L;

        //Exercise
        List<AvailabilitySlot> finalAvailability = availabilitySlotsDao.getAvailabilityByDoctorId(docId);

        //Postconditions
        assertFalse(finalAvailability.isEmpty());
        assertEquals(7, finalAvailability.size());
    }
}
