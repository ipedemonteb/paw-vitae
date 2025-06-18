package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.DoctorOfficeAvailabilityDao;
import ar.edu.itba.paw.models.DoctorOffice;
import ar.edu.itba.paw.models.DoctorOfficeAvailability;
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

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class DoctorOfficeAvailabilityDaoTest {

    private static final String DOCTOR_OFFICE_AVAILABILITY_TABLE = "doctor_office_availability_slots";

    @Autowired
    private DoctorOfficeAvailabilityDao doctorOfficeAvailabilityDao;
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
        // Preconditions
        DoctorOffice doctorOffice = em.find(DoctorOffice.class, 1L);
        LocalTime startTime = LocalTime.of(9, 0);
        LocalTime endTime = LocalTime.of(17, 0);
        Integer dayOfWeek = 2;

        // Exercise
        DoctorOfficeAvailability createdAvailability = doctorOfficeAvailabilityDao.create(new DoctorOfficeAvailability(
                doctorOffice,
                endTime,
                startTime,
                dayOfWeek
        ));
        em.flush();

        // Postconditions
        assertNotNull(createdAvailability);
        assertEquals(doctorOffice.getId(), createdAvailability.getOffice().getId());
        assertEquals(endTime, createdAvailability.getEndTime());
        assertEquals(startTime, createdAvailability.getStartTime());
        assertEquals(dayOfWeek, createdAvailability.getDayOfWeek());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCTOR_OFFICE_AVAILABILITY_TABLE, "office_id = " + doctorOffice.getId() +
                " AND day_of_week = " + dayOfWeek)
        );
    }

    @Rollback
    @Test
    public void testUpdate() {
        // Preconditions
        DoctorOffice doctorOffice = em.find(DoctorOffice.class, 1L);
        LocalTime startTime = LocalTime.of(14, 0);
        LocalTime endTime = LocalTime.of(17, 0);
        Integer dayOfWeek = 1;

        // Exercise
        doctorOfficeAvailabilityDao.update(new DoctorOfficeAvailability(
                doctorOffice,
                endTime,
                startTime,
                dayOfWeek
        ));
        em.flush();

        // Postconditions
        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCTOR_OFFICE_AVAILABILITY_TABLE, "office_id = " + doctorOffice.getId() +
                " AND day_of_week = " + dayOfWeek)
        );
    }

    @Rollback
    @Test
    public void testDelete() {
        // Preconditions
        DoctorOfficeAvailability toDelete = em.find(DoctorOfficeAvailability.class, 2L);

        // Exercise
        doctorOfficeAvailabilityDao.delete(toDelete);
        em.flush();

        // Postconditions
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCTOR_OFFICE_AVAILABILITY_TABLE, "office_id = " + toDelete.getOffice().getId() +
                " AND day_of_week = " + 1)
        );
    }

    @Test
    public void testGetByOfficeIdDoesNotExist() {
        //Preconditions

        //Exercise
        List<DoctorOfficeAvailability> availability = doctorOfficeAvailabilityDao.getByOfficeId(1000L);

        //Postconditions
        assertTrue(availability.isEmpty());
    }

    @Test
    public void testGetByOfficeIdExists() {
        //Preconditions
        long officeId = 1L;

        //Exercise
        List<DoctorOfficeAvailability> availability = doctorOfficeAvailabilityDao.getByOfficeId(officeId);

        //Postconditions
        assertFalse(availability.isEmpty());
        assertEquals(2, availability.size());
        assertEquals(officeId, availability.getFirst().getOffice().getId().longValue());
        assertEquals(officeId, availability.get(1).getOffice().getId().longValue());
    }

    @Test
    public void testGetActiveByOfficeIdDoesNotExist() {
        // Preconditions

        // Exercise
        List<DoctorOfficeAvailability> availability = doctorOfficeAvailabilityDao.getActiveByOfficeId(1000L);

        // Postconditions
        assertTrue(availability.isEmpty());
    }

    @Test
    public void testGetActiveByOfficeIdExists() {
        // Preconditions
        long officeId = 1L;

        // Exercise
        List<DoctorOfficeAvailability> availability = doctorOfficeAvailabilityDao.getActiveByOfficeId(officeId);

        // Postconditions
        assertFalse(availability.isEmpty());
        assertEquals(2, availability.size());
        assertEquals(officeId, availability.getFirst().getOffice().getId().longValue());
        assertEquals(officeId, availability.get(1).getOffice().getId().longValue());
    }

    @Test
    public void testGetByDoctorIdDoesNotExist() {
        // Preconditions

        // Exercise
        List<DoctorOfficeAvailability> availability = doctorOfficeAvailabilityDao.getByDoctorId(1000L);

        // Postconditions
        assertTrue(availability.isEmpty());
    }

    @Test
    public void testGetByDoctorIdExists() {
        // Preconditions
        long doctorId = 2L;

        // Exercise
        List<DoctorOfficeAvailability> availability = doctorOfficeAvailabilityDao.getByDoctorId(doctorId);

        // Postconditions
        assertFalse(availability.isEmpty());
        assertEquals(2, availability.size());
        assertEquals(doctorId, availability.getFirst().getOffice().getDoctor().getId());
        assertEquals(doctorId, availability.get(1).getOffice().getDoctor().getId());
    }

    @Test
    public void testGetActiveByDoctorIdDoesNotExist() {
        // Preconditions

        // Exercise
        List<DoctorOfficeAvailability> availability = doctorOfficeAvailabilityDao.getActiveByDoctorId(1000L);

        // Postconditions
        assertTrue(availability.isEmpty());
    }

    @Test
    public void testGetActiveByDoctorIdExists() {
        // Preconditions
        long doctorId = 2L;

        // Exercise
        List<DoctorOfficeAvailability> availability = doctorOfficeAvailabilityDao.getActiveByDoctorId(doctorId);

        // Postconditions
        assertFalse(availability.isEmpty());
        assertEquals(2, availability.size());
        assertEquals(doctorId, availability.getFirst().getOffice().getDoctor().getId());
        assertEquals(doctorId, availability.get(1).getOffice().getDoctor().getId());
    }
}
