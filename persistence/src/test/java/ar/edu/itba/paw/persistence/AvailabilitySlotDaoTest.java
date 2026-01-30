package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.AvailabilitySlotsDao;
import ar.edu.itba.paw.models.AvailabilitySlots;
import ar.edu.itba.paw.models.Doctor;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.sql.DataSource;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class AvailabilitySlotDaoTest {

    private static final String DOCAVAILABILITY_TABLE = "doctor_availability_slots";

    private static final long DOC_ID = 2L;
    private static final long SLOT_ID = 1L;
    private static final LocalDate SLOT_DATE = LocalDate.of(2026, 3, 2);
    private static final LocalTime SLOT_START_TIME = LocalTime.of(9, 0);
    private static final AvailabilitySlots.SlotStatus SLOT_STATUS = AvailabilitySlots.SlotStatus.AVAILABLE;

    @Autowired
    private AvailabilitySlotsDao availabilitySlotsDao;
    private JdbcTemplate jdbcTemplate;

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
    public void testCreate() {
        //Preconditions
        int slotYear = 2026;
        int slotMonth = 3;
        int slotDay = 9;
        int startTimeHour = 10;
        int startTimeMinute = 0;
        LocalDate slotDate = LocalDate.of(slotYear, slotMonth, slotDay);
        LocalTime slotStartTime = LocalTime.of(startTimeHour, startTimeMinute);
        String status = "AVAILABLE";
        Doctor mangedDoctor = em.find(Doctor.class, DOC_ID);
        AvailabilitySlots slot = new AvailabilitySlots(
                mangedDoctor,
                slotDate,
                slotStartTime,
                AvailabilitySlots.SlotStatus.valueOf(status)
        );

        //Exercise
        AvailabilitySlots createdSlot = availabilitySlotsDao.create(slot);
        em.flush();

        //Postconditions
        assertNotNull(createdSlot);
        assertEquals(slotDate, createdSlot.getSlotDate());
        assertEquals(slotStartTime, createdSlot.getStartTime());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCAVAILABILITY_TABLE, "doctor_id = " + DOC_ID +
                " AND slot_date = '" + Date.valueOf(slotDate) + "'" +
                " AND start_time = '" + Time.valueOf(slotStartTime) + "'" +
                " AND status = '" + status + "'"));
    }

    @Test
    public void testGetByIdDoesNotExist() {
        //Preconditions
        long slotId = 1000L;

        //Exercise
        Optional<AvailabilitySlots> maybeSlot = availabilitySlotsDao.getById(slotId);

        //Postconditions
        assertFalse(maybeSlot.isPresent());
    }

    @Test
    public void testGetByIdExists() {
        //Preconditions

        //Exercise
        Optional<AvailabilitySlots> maybeSlot = availabilitySlotsDao.getById(SLOT_ID);

        //Postconditions
        assertTrue(maybeSlot.isPresent());
        assertEquals(SLOT_DATE, maybeSlot.get().getSlotDate());
        assertEquals(SLOT_START_TIME, maybeSlot.get().getStartTime());
        assertEquals(SLOT_STATUS, maybeSlot.get().getStatus());
    }

    @Test
    public void testGetByDoctorIdInDateRangeNotExists() {
        //Preconditions
        LocalDate startDate = LocalDate.of(2027, 3, 1);
        LocalDate endDate = LocalDate.of(2027, 3, 10);

        //Exercise
        List<AvailabilitySlots> slots = availabilitySlotsDao.getByDoctorIdInDateRange(DOC_ID, startDate, endDate);

        //Postconditions
        assertTrue(slots.isEmpty());
    }

    @Test
    public void testGetByDoctorIdInDateRangeExists() {
        //Preconditions
        LocalDate startDate = LocalDate.of(2026, 3, 1);
        LocalDate endDate = LocalDate.of(2026, 3, 10);

        //Exercise
        List<AvailabilitySlots> slots = availabilitySlotsDao.getByDoctorIdInDateRange(DOC_ID, startDate, endDate);

        //Postconditions
        assertFalse(slots.isEmpty());
        assertEquals(6, slots.size());
    }

    @Test
    public void testExistsFalse() {
        //Preconditions
        LocalDate slotDate = LocalDate.of(2027,1,1);

        //Exercise
        boolean exists = availabilitySlotsDao.exists(DOC_ID, slotDate, SLOT_START_TIME);

        //Postconditions
        assertFalse(exists);
    }

    @Test
    public void testExistsTrue() {
        //Preconditions

        //Exercise
        boolean exists = availabilitySlotsDao.exists(DOC_ID, SLOT_DATE, SLOT_START_TIME);

        //Postconditions
        assertTrue(exists);
    }

    @Rollback
    @Test
    public void testDeleteUnbookedSlots() {
        //Preconditions
        LocalDate fromDate = LocalDate.of(2026, 3, 3);

        //Exercise
        int modified = availabilitySlotsDao.deleteUnbookedSlots(DOC_ID, fromDate);

        //Postconditions
        assertEquals(3, modified);
        assertEquals(3, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCAVAILABILITY_TABLE, "doctor_id = " + DOC_ID));
    }
}
