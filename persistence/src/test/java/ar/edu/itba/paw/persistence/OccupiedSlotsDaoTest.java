package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.OccupiedSlotsDao;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.OccupiedSlots;
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
public class OccupiedSlotsDaoTest {

    private static final String OCCUPIED_TABLE = "Occupied_Slots";
    private static final long DOC_ID = 2L;
    private static final long SLOT_ID = 1L;

    @Autowired
    private OccupiedSlotsDao occupiedSlotsDao;
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
        Doctor mangedDoctor = em.find(Doctor.class, DOC_ID);

        //Exercise
        OccupiedSlots createdSlot = occupiedSlotsDao.create(mangedDoctor, slotDate, slotStartTime);
        em.flush();

        //Postconditions
        assertNotNull(createdSlot);
        assertEquals(slotDate, createdSlot.getSlotDate());
        assertEquals(slotStartTime, createdSlot.getStartTime());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, OCCUPIED_TABLE, "doctor_id = " + DOC_ID +
                " AND slot_date = '" + Date.valueOf(slotDate) + "'" +
                " AND start_time = '" + Time.valueOf(slotStartTime) + "'"));
    }

    @Test
    public void testGetByIdDoesNotExist() {
        //Preconditions

        //Exercise
        Optional<OccupiedSlots> slot = occupiedSlotsDao.getById(999L);

        //Postconditions
        assertTrue(slot.isEmpty());
    }

    @Test
    public void testGetByIdExists() {
        //Preconditions

        //Exercise
        Optional<OccupiedSlots> slot = occupiedSlotsDao.getById(SLOT_ID);

        //Postconditions
        assertTrue(slot.isPresent());
        assertEquals(DOC_ID, slot.get().getDoctor().getId());
        assertEquals(slot.get().getSlotDate(), LocalDate.of(2026, 4, 1));
        assertEquals(slot.get().getStartTime(), LocalTime.of(10, 0));
    }

    @Test
    public void testGetByDoctorIdInDateRangeEmpty() {
        //Preconditions
        LocalDate startDate = LocalDate.of(2027, 3, 1);
        LocalDate endDate = LocalDate.of(2027, 3, 10);

        //Exercise
        List<OccupiedSlots> slots = occupiedSlotsDao.getByDoctorIdInDateRange(DOC_ID, startDate, endDate);

        //Postconditions
        assertTrue(slots.isEmpty());
    }

    @Test
    public void testGetByDoctorIdInDateRange() {
        //Preconditions
        LocalDate startDate = LocalDate.of(2026, 3, 31);
        LocalDate endDate = LocalDate.of(2026, 4, 2);

        //Exercise
        List<OccupiedSlots> slots = occupiedSlotsDao.getByDoctorIdInDateRange(DOC_ID, startDate, endDate);

        //Postconditions
        assertFalse(slots.isEmpty());
        assertEquals(2, slots.size());
    }

    @Rollback
    @Test
    public void testDelete() {
        //Preconditions
        long slotId = 2L;

        //Exercise
        occupiedSlotsDao.delete(slotId);
        em.flush();

        //Postconditions
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, OCCUPIED_TABLE, "id = " + slotId));
    }
}
