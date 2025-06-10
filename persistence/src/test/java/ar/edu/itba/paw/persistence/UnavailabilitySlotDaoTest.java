package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.UnavailabilitySlot;
import ar.edu.itba.paw.persistence.hibernate.UnavailabilitySlotDaoHibeImpl;
import ar.edu.itba.paw.persistence.hibernate.UserDaoHibeImpl;
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
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class UnavailabilitySlotDaoTest {

    private static final String UNAVAILABILITY_TABLE = "doctor_unavailability";
    private static final long DOCTOR_ID = 2L;

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

    @Rollback
    @Test
    public void testUpdateDoctorUnavailability() {
        //Preconditions
        Doctor doctor = em.getReference(Doctor.class, DOCTOR_ID);
        LocalDate start_first = LocalDate.of(2026, 10, 1);
        LocalDate end_first = LocalDate.of(2026, 10, 5);
        LocalDate start_second = LocalDate.of(2026, 11, 1);
        LocalDate end_second = LocalDate.of(2026, 11, 5);
        List<UnavailabilitySlot> newUnavailabilitySlots = List.of(
                new UnavailabilitySlot(doctor, start_first, end_first),
                new UnavailabilitySlot(doctor, start_second, end_second)
        );

        //Exercise
        unavailabilitySlotDao.updateDoctorUnavailability(DOCTOR_ID, newUnavailabilitySlots);
        em.flush();

        //Postconditions
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, UNAVAILABILITY_TABLE, "doctor_id = " + DOCTOR_ID +
                " AND start_date = '" + start_first + "' AND end_date = '" + end_first + "'"));
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, UNAVAILABILITY_TABLE, "doctor_id = " + DOCTOR_ID +
                " AND start_date = '" + start_second + "' AND end_date = '" + end_second + "'"));
    }

    @Test
    public void testGetUnavailabilityByDoctorIdDoesNotExist() {
        //Preconditions

        //Exercise
        List<UnavailabilitySlot> maybeSlots = unavailabilitySlotDao.getUnavailabilityByDoctorId(1000L);

        //Postconditions
        assertTrue(maybeSlots.isEmpty());
    }

    @Test
    public void testGetUnavailabilityByDoctorIdExists() {
        //Preconditions

        //Exercise
        List<UnavailabilitySlot> maybeSlots = unavailabilitySlotDao.getUnavailabilityByDoctorId(DOCTOR_ID);

        //Postconditions
        assertFalse(maybeSlots.isEmpty());
        assertEquals(1, maybeSlots.size());
        assertEquals(DOCTOR_ID, maybeSlots.getFirst().getDoctor().getId());
        assertEquals(LocalDate.now().plusDays(60), maybeSlots.getFirst().getStartDate());
        assertEquals(LocalDate.now().plusDays(65), maybeSlots.getFirst().getEndDate());
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
    public void testGetUnavailabilityByDoctorIdCurrentAndNextMonthDoesNotExist() {
        //Preconditions

        //Exercise
        List<UnavailabilitySlot> slots = unavailabilitySlotDao.getUnavailabilityByDoctorIdCurrentAndNextMonth(DOCTOR_ID);

        //Postconditions
        assertTrue(slots.isEmpty());
    }

    @Test
    public void testGetUnavailabilityByDoctorIdCurrentAndNextMonthExist() {
        //Preconditions
        long doctorId = 4L;

        //Exercise
        List<UnavailabilitySlot> slots = unavailabilitySlotDao.getUnavailabilityByDoctorIdCurrentAndNextMonth(doctorId);

        //Postconditions
        assertFalse(slots.isEmpty());
        assertEquals(1, slots.size());
        assertEquals(doctorId, slots.getFirst().getDoctor().getId());
        assertEquals(LocalDate.now().plusDays(10), slots.getFirst().getStartDate());
        assertEquals(LocalDate.now().plusDays(15), slots.getFirst().getEndDate());
    }

    @Test
    public void testGetUnavailabilityByDoctorIdAndMonthAndYearDoesNotExist() {
        //Preconditions

        //Exercise
        List<UnavailabilitySlot> slots = unavailabilitySlotDao.getUnavailabilityByDoctorIdAndMonthAndYear(1000L, 10, 2023);

        //Postconditions
        assertTrue(slots.isEmpty());
    }

    @Test
    public void testGetUnavailabilityByDoctorIdAndMonthAndYearExist() {
        //Preconditions
        long doctorId = 4L;

        //Exercise
        List<UnavailabilitySlot> slots = unavailabilitySlotDao.getUnavailabilityByDoctorIdAndMonthAndYear(doctorId, 1, 2026);

        //Postconditions
        assertFalse(slots.isEmpty());
        assertEquals(1, slots.size());
        assertEquals(doctorId, slots.getFirst().getDoctor().getId());
        assertEquals(LocalDate.of(2026, 1, 1), slots.getFirst().getStartDate());
        assertEquals(LocalDate.of(2026, 1, 5), slots.getFirst().getEndDate());
    }
}
