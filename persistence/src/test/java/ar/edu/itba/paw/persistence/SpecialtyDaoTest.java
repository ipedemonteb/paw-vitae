package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.persistence.hibernate.SpecialtyDaoHibeImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class SpecialtyDaoTest {

    @Autowired
    private SpecialtyDaoHibeImpl specialtyDao;

    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testGetByIdDoesNotExist() {
        //Preconditions

        //Exercise
        Optional<Specialty> specialty = specialtyDao.getById(1000L);

        //Postconditions
        assertFalse(specialty.isPresent());
    }

    @Test
    public void testGetByIdExists() {
        //Preconditions
        long cardiologyId = 1;

        //Exercise
        Optional<Specialty> maybeSpeciality = specialtyDao.getById(cardiologyId);

        //Postconditions
        assertTrue(maybeSpeciality.isPresent());
        assertEquals(cardiologyId, maybeSpeciality.get().getId());
        assertEquals("Cardiology", maybeSpeciality.get().getKey());
    }


    @Test
    public void testGetAll() {
        //Preconditions

        //Exercise
        List<Specialty> specialties = specialtyDao.getAll();

        //Postconditions
        List<String> keys = specialties.stream().map(Specialty::getKey).toList();
        assertEquals(4, specialties.size());
        assertTrue(keys.containsAll(List.of("Cardiology", "Neurology", "Orthopedics", "Pediatrics")));
    }

    @Test
    public void testGetByIds() {
        //Preconditions
        List<Long> ids = List.of(1L, 2L);

        //Exercise
        List<Specialty> specialties = specialtyDao.getByIds(ids);

        //Postconditions
        assertEquals(2, specialties.size());
        assertTrue(specialties.stream().allMatch(s -> ids.contains(s.getId())));
    }

    @Test
    public void testGetByIdsEmpty() {
        //Preconditions
        List<Long> ids = List.of(1000L);

        //Exercise
        List<Specialty> specialties = specialtyDao.getByIds(ids);

        //Postconditions
        assertTrue(specialties.isEmpty());
    }

    @Test
    public void testGetByDoctorIdDoesNotExist() {
        //Preconditions
        long doctorId = 1000L;

        //Exercise
        List<Specialty> specialties = specialtyDao.getByDoctorId(doctorId);

        //Postconditions
        assertTrue(specialties.isEmpty());
    }

    @Test
    public void testGetByDoctorIdExists() {
        //Preconditions
        long doctorId = 2L;

        //Exercise
        List<Specialty> specialties = specialtyDao.getByDoctorId(doctorId);

        //Postconditions
        assertFalse(specialties.isEmpty());
        assertEquals(1L, specialties.getFirst().getId());
    }
}