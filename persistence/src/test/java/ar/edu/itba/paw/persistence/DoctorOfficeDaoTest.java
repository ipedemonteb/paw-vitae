package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.DoctorOfficeDao;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorOffice;
import ar.edu.itba.paw.models.Neighborhood;
import ar.edu.itba.paw.models.Specialty;
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
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class DoctorOfficeDaoTest {

    private static final String DOCTOR_OFFICES_TABLE = "doctor_offices";
    private static final long OFFICE_DOCTOR_ID = 2L;
    private static final String OFFICE_NAME = "Consultorio Recoleta";
    private static final long OFFICE_NEIGHBORHOOD_ID = 1L;

    @Autowired
    private DoctorOfficeDao doctorOfficeDao;
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
        Doctor doctor = em.find(Doctor.class, 5L);
        Neighborhood neighborhood = em.find(Neighborhood.class, 1L);
        Specialty specialty = em.find(Specialty.class, 1L);
        List<Specialty> specialties = List.of(specialty);
        String officeName = "Test Office";

        //Exercise
        DoctorOffice doctorOffice = doctorOfficeDao.create(new DoctorOffice(
                doctor, neighborhood, specialties, officeName
        ));
        em.flush();

        //Postconditions
        assertNotNull(doctorOffice);
        assertEquals(doctor.getId(), doctorOffice.getDoctor().getId());
        assertEquals(neighborhood.getId(), doctorOffice.getNeighborhood().getId());
        assertEquals(specialties.getFirst().getId(), specialty.getId());
        assertEquals(officeName, doctorOffice.getOfficeName());
        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCTOR_OFFICES_TABLE, "doctor_id = " + doctor.getId()));
    }

    @Test
    public void testGetByIdDoesNotExist() {
        //Preconditions

        //Exercise
        Optional<DoctorOffice> doctorOffice = doctorOfficeDao.getById(999L);

        //Postconditions
        assertTrue(doctorOffice.isEmpty());
    }

    @Test
    public void testGetByIdExists() {
        //Preconditions
        long officeId = 1L;

        //Exercise
        Optional<DoctorOffice> doctorOffice = doctorOfficeDao.getById(officeId);

        //Postconditions
        assertTrue(doctorOffice.isPresent());
        assertEquals(officeId, doctorOffice.get().getId().longValue());
        assertEquals(OFFICE_NAME, doctorOffice.get().getOfficeName());
        assertEquals(OFFICE_NEIGHBORHOOD_ID, doctorOffice.get().getNeighborhood().getId());
        assertEquals(OFFICE_DOCTOR_ID, doctorOffice.get().getDoctor().getId());
    }

    @Test
    public void testGetByDoctorIdDoesNotExist() {
        //Preconditions

        //Exercise
        List<DoctorOffice> doctorOffices = doctorOfficeDao.getByDoctorId(1000L);

        //Postconditions
        assertTrue(doctorOffices.isEmpty());
    }

    @Test
    public void testGetByDoctorIdExists() {
        //Preconditions

        //Exercise
        List<DoctorOffice> doctorOffices = doctorOfficeDao.getByDoctorId(OFFICE_DOCTOR_ID);

        //Postconditions
        assertNotNull(doctorOffices);
        assertFalse(doctorOffices.isEmpty());
        for (DoctorOffice office : doctorOffices) {
            assertEquals(OFFICE_DOCTOR_ID, office.getDoctor().getId());
        }
    }

    @Test
    public void testGetByDoctorIdWithAvailabilityDoesNotExist() {
        //Preconditions

        //Exercise
        List<DoctorOffice> doctorOffices = doctorOfficeDao.getByDoctorIdWithAvailability(1000L);

        //Postconditions
        assertTrue(doctorOffices.isEmpty());
    }

    @Test
    public void testGetByDoctorIdWithAvailabilityExists() {
        //Preconditions

        //Exercise
        List<DoctorOffice> doctorOffices = doctorOfficeDao.getByDoctorIdWithAvailability(OFFICE_DOCTOR_ID);

        //Postconditions
        assertFalse(doctorOffices.isEmpty());
        assertEquals(2, doctorOffices.getFirst().getDoctorOfficeAvailability().size());
        for (DoctorOffice office : doctorOffices) {
            assertEquals(OFFICE_DOCTOR_ID, office.getDoctor().getId());
        }
    }

    @Test
    public void testGetByDoctorIdWithAvailabilityNoAvailability() {
        //Preconditions
        long doctorId = 5L;

        //Exercise
        List<DoctorOffice> doctorOffices = doctorOfficeDao.getByDoctorIdWithAvailability(doctorId);

        //Postconditions
        assertFalse(doctorOffices.isEmpty());
        assertTrue(doctorOffices.getFirst().getDoctorOfficeAvailability().isEmpty());
        for (DoctorOffice office : doctorOffices) {
            assertEquals(0, office.getDoctorOfficeAvailability().size());
            assertEquals(doctorId, office.getDoctor().getId());
        }
    }

    @Test
    public void testGetActiveByDoctorIdDoesNotExist() {
        //Preconditions

        //Exercise
        List<DoctorOffice> doctorOffices = doctorOfficeDao.getActiveByDoctorId(1000L);

        //Postconditions
        assertTrue(doctorOffices.isEmpty());
    }

    @Test
    public void testGetActiveByDoctorIdExists() {
        //Preconditions

        //Exercise
        List<DoctorOffice> doctorOffices = doctorOfficeDao.getActiveByDoctorId(OFFICE_DOCTOR_ID);

        //Postconditions
        assertNotNull(doctorOffices);
        assertFalse(doctorOffices.isEmpty());
        assertEquals(1, doctorOffices.size());
        for (DoctorOffice office : doctorOffices) {
            assertEquals(OFFICE_DOCTOR_ID, office.getDoctor().getId());
            assertTrue(office.isActive());
        }
    }

    @Test
    public void testGetInactiveByDoctorIdDoesNotExist() {
        //Preconditions

        //Exercise
        List<DoctorOffice> doctorOffices = doctorOfficeDao.getInactiveByDoctorId(1000L);

        //Postconditions
        assertTrue(doctorOffices.isEmpty());
    }

    @Test
    public void testGetInactiveByDoctorIdExists() {
        //Preconditions

        //Exercise
        List<DoctorOffice> doctorOffices = doctorOfficeDao.getInactiveByDoctorId(OFFICE_DOCTOR_ID);

        //Postconditions
        assertNotNull(doctorOffices);
        assertFalse(doctorOffices.isEmpty());
        assertEquals(1, doctorOffices.size());
        for (DoctorOffice office : doctorOffices) {
            assertEquals(OFFICE_DOCTOR_ID, office.getDoctor().getId());
            assertFalse(office.isActive());
        }
    }

    @Rollback
    @Test
    public void testUpdate() {
        //Preconditions
        DoctorOffice doctorOffice = em.find(DoctorOffice.class, 1L);
        Specialty first_specialty = em.find(Specialty.class, 1L);
        Specialty second_specialty = em.find(Specialty.class, 2L);
        doctorOffice.setSpecialties(List.of(first_specialty, second_specialty));

        //Exercise
        DoctorOffice updated = doctorOfficeDao.update(doctorOffice);
        em.flush();

        //Postconditions
        assertNotNull(updated);
        assertEquals(OFFICE_DOCTOR_ID, updated.getDoctor().getId());
        assertEquals(OFFICE_NEIGHBORHOOD_ID, updated.getNeighborhood().getId());
        assertEquals(2, updated.getSpecialties().size());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCTOR_OFFICES_TABLE, "doctor_id = " + OFFICE_DOCTOR_ID +
                " AND office_name = '" + OFFICE_NAME + "' AND active = true" + " AND neighborhood_id = " + OFFICE_NEIGHBORHOOD_ID));
    }

    @Test
    public void testGetByNameAndNeighborhoodIdNameDoesNotExist() {
        //Preconditions

        //Exercise
        List<DoctorOffice> results = doctorOfficeDao.getByNameAndNeighborhoodId("NonExistentOffice", OFFICE_NEIGHBORHOOD_ID, OFFICE_DOCTOR_ID);

        //Postconditions
        assertTrue(results.isEmpty());
    }

    @Test
    public void testGetByNameAndNeighborhoodIdNeighborhoodDoesNotExist() {
        //Preconditions

        //Exercise
        List<DoctorOffice> results = doctorOfficeDao.getByNameAndNeighborhoodId("Consultorio Recoleta", 1000L, OFFICE_DOCTOR_ID);

        //Postconditions
        assertTrue(results.isEmpty());
    }

    @Test
    public void testGetByNameAndNeighborhoodIdDoctorDoesNotExist() {
        //Preconditions

        //Exercise
        List<DoctorOffice> results = doctorOfficeDao.getByNameAndNeighborhoodId("Consultorio Recoleta", OFFICE_NEIGHBORHOOD_ID, 1000L);

        //Postconditions
        assertTrue(results.isEmpty());
    }

    @Test
    public void testGetByNameAndNeighborhoodIdExists() {
        //Preconditions

        //Postconditions
        List<DoctorOffice> results = doctorOfficeDao.getByNameAndNeighborhoodId(OFFICE_NAME, OFFICE_NEIGHBORHOOD_ID, OFFICE_DOCTOR_ID);

        assertFalse(results.isEmpty());
        assertEquals(1, results.size());
        assertEquals(OFFICE_NAME, results.getFirst().getOfficeName());
        assertEquals(OFFICE_NEIGHBORHOOD_ID, results.getFirst().getNeighborhood().getId());
        assertEquals(OFFICE_DOCTOR_ID, results.getFirst().getDoctor().getId());
    }

    @Rollback
    @Test
    public void testRemove() {
        //Preconditions
        long officeId = 1L;

        //Exercise
        doctorOfficeDao.remove(officeId);
        em.flush();

        //Postconditions
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCTOR_OFFICES_TABLE, "id = " + officeId));
    }
}
