package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.UnavailabilitySlot;
import ar.edu.itba.paw.persistence.hibernate.UnavailabilitySlotDaoHibeImpl;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class UnavailabilitySlotDaoTest {

    private static final String UNAVAILABILITY_TABLE = "doctor_unavailability";
    private static final long DOCTOR_ID = 2L;
    private static final long SLOT_ID = 1L;

    @Autowired
    private UnavailabilitySlotDaoHibeImpl unavailabilitySlotDao;
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Rollback
    @Test
    public void testCreate() {
        //Preconditions
        Doctor doctor = em.getReference(Doctor.class, DOCTOR_ID);
        LocalDate start = LocalDate.of(2026, 10, 1);
        LocalDate end = LocalDate.of(2026, 10, 5);
        UnavailabilitySlot slot = new UnavailabilitySlot(doctor, start, end);

        //Exercise
        UnavailabilitySlot created = unavailabilitySlotDao.create(slot);
        em.flush();

        //Postconditions
        assertNotNull(created);
        assertEquals(DOCTOR_ID, created.getDoctor().getId());
        assertEquals(start, created.getStartDate());
        assertEquals(end, created.getEndDate());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, UNAVAILABILITY_TABLE, "doctor_id = " + DOCTOR_ID +
                " AND start_date = '" + start + "' AND end_date = '" + end + "'"));
    }

    @Test
    public void testGetByIdDoesNotExist() {
        //Preconditions

        //Exercise
        Optional<UnavailabilitySlot> slot = unavailabilitySlotDao.getById(999L);

        //Postconditions
        assertFalse(slot.isPresent());
    }

    @Test
    public void testGetByIdExists() {
        //Preconditions

        //Exercise
        Optional<UnavailabilitySlot> slot = unavailabilitySlotDao.getById(SLOT_ID);

        //Postconditions
        assertTrue(slot.isPresent());
        assertEquals(DOCTOR_ID, slot.get().getDoctor().getId());
        assertEquals(LocalDate.now().plusDays(60), slot.get().getStartDate());
        assertEquals(LocalDate.now().plusDays(65), slot.get().getEndDate());
    }

    @Rollback
    @Test
    public void testDelete() {
        //Preconditions
        long slotId = 2L;
        UnavailabilitySlot slot = em.getReference(UnavailabilitySlot.class, slotId);

        //Exercise
        unavailabilitySlotDao.delete(slot);
        em.flush();

        //Postconditions
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, UNAVAILABILITY_TABLE, "id = " + slotId));
    }



    @Test
    public void testIsUnavailableAtDate() {
        //Preconditions

        //Exercise
        boolean isUnavailable = unavailabilitySlotDao.isUnavailableAtDate(DOCTOR_ID, LocalDate.now().plusDays(60));

        //Postconditions
        assertTrue(isUnavailable);
    }

    @Test
    public void testIsNotUnavailableAtDate() {
        //Preconditions

        //Exercise
        boolean isUnavailable = unavailabilitySlotDao.isUnavailableAtDate(DOCTOR_ID, LocalDate.now().plusDays(10));

        //Postconditions
        assertFalse(isUnavailable);
    }





    @Test
    public void testHasNoOverlap() {
        //Preconditions
        LocalDate startDate = LocalDate.now().plusDays(10);
        LocalDate endDate = LocalDate.now().plusDays(15);

        //Exercise
        boolean overlap = unavailabilitySlotDao.hasOverlap(DOCTOR_ID, startDate, endDate);

        //Postconditions
        assertFalse(overlap);
    }

    @Test
    public void testHasOverlap() {
        //Preconditions
        LocalDate startDate = LocalDate.now().plusDays(62);
        LocalDate endDate = LocalDate.now().plusDays(70);

        //Exercise
        boolean overlap = unavailabilitySlotDao.hasOverlap(DOCTOR_ID, startDate, endDate);

        //Postconditions
        assertTrue(overlap);
    }

    @Test
    public void testGetUnavailabilityByDoctorIdInDateRangeEmpty() {
        //Preconditions
        LocalDate from = LocalDate.now().plusDays(10);
        LocalDate to = LocalDate.now().plusDays(15);

        //Exercise
        List<UnavailabilitySlot> slots = unavailabilitySlotDao.getUnavailabilityByDoctorIdInDateRange(DOCTOR_ID, from, to);

        //Postconditions
        assertTrue(slots.isEmpty());
    }

    @Test
    public void testGetUnavailabilityByDoctorIdInDateRange() {
        //Preconditions
        LocalDate from = LocalDate.now().plusDays(50);
        LocalDate to = LocalDate.now().plusDays(70);

        //Exercise
        List<UnavailabilitySlot> slots = unavailabilitySlotDao.getUnavailabilityByDoctorIdInDateRange(DOCTOR_ID, from, to);

        //Postconditions
        assertFalse(slots.isEmpty());
        assertEquals(1, slots.size());
    }

    @Test
    public void testGetUnavailabilityByDoctorIdPaginated() {
        //Preconditions
        int page = 1;
        int size = 10;

        //Exercise
        List<UnavailabilitySlot> slots = unavailabilitySlotDao.getUnavailabilityByDoctorIdPaginated(DOCTOR_ID, page, size);

        //Postconditions
        assertFalse(slots.isEmpty());
        assertEquals(1, slots.size());
        assertEquals(LocalDate.now().plusDays(60), slots.getFirst().getStartDate());
        assertEquals(LocalDate.now().plusDays(65), slots.getFirst().getEndDate());
    }

    @Test
    public void testCountUnavailabilityByDoctorId() {
        //Preconditions

        //Exercise
        long count = unavailabilitySlotDao.countUnavailabilityByDoctorId(DOCTOR_ID);

        //Postconditions
        assertEquals(1, count);
    }
}
