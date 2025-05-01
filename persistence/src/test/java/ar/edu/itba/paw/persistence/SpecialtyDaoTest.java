//package ar.edu.itba.paw.persistence;
//
//import ar.edu.itba.paw.models.Specialty;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import javax.sql.DataSource;
//import java.util.List;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import static org.junit.Assert.*;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestConfig.class)
//public class SpecialtyDaoTest {
//
//    private SpecialtyDaoImpl specialtyDao;
//    private JdbcTemplate jdbcTemplate;
//    private SimpleJdbcInsert jdbcInsert;
//
//    @Autowired
//    private DataSource ds;
//
//    @Before
//    public void setUp() {
//        jdbcTemplate = new JdbcTemplate(ds);
//        specialtyDao = new SpecialtyDaoImpl(ds);
//    }
//
//    @Test
//    public void testGetByIdDoesNotExist() {
//        //Preconditions
//
//        //Exercise
//        Optional<Specialty> specialty = specialtyDao.getById(1000L);
//
//        //Postconditions
//        assertFalse(specialty.isPresent());
//    }
//
//    @Test
//    public void testGetByIdExists() {
//        //Preconditions
//        long cardiologyId = 1;
//
//        //Exercise
//        Optional<Specialty> maybeSpeciality = specialtyDao.getById(cardiologyId);
//
//        //Postconditions
//        assertTrue(maybeSpeciality.isPresent());
//        assertEquals(cardiologyId, maybeSpeciality.get().getId());
//        assertEquals("Cardiology", maybeSpeciality.get().getKey());
//    }
//
//    @Test
//    public void testGetByNameDoesNotExist() {
//        //Preconditions
//
//        //Exercise
//        Optional<Specialty> specialty = specialtyDao.getByName("NoSpecialty");
//
//        //Postconditions
//        assertFalse(specialty.isPresent());
//    }
//
//    @Test
//    public void testGetByNameExists() {
//        //Preconditions
//
//        //Exercise
//        Optional<Specialty> fetchedSpecialty = specialtyDao.getByName("Neurology");
//
//        //Postconditions
//        assertTrue(fetchedSpecialty.isPresent());
//        assertEquals("Neurology", fetchedSpecialty.get().getKey());
//    }
//
//    @Test
//    public void testGetAll() {
//        //Preconditions
//
//        //Exercise
//        List<Specialty> specialties = specialtyDao.getAll();
//
//        //Postconditions
//        List<String> keys = specialties.stream().map(Specialty::getKey).collect(Collectors.toList());
//        assertEquals(3, specialties.size());
//        assertTrue(keys.containsAll(List.of("Cardiology", "Neurology", "Orthopedics")));
//    }
//
//    @Test
//    public void testGetByIds() {
//        //Preconditions
//        List<Long> ids = List.of(1L, 2L);
//
//        //Exercise
//        List<Specialty> specialties = specialtyDao.getByIds(ids);
//
//        //Postconditions
//        assertEquals(2, specialties.size());
//        assertTrue(specialties.stream().allMatch(s -> ids.contains(s.getId())));
//    }
//
//    @Test
//    public void testGetByIdsEmpty() {
//        //Preconditions
//        List<Long> ids = List.of(1000L);
//
//        //Exercise
//        List<Specialty> specialties = specialtyDao.getByIds(ids);
//
//        //Postconditions
//        assertTrue(specialties.isEmpty());
//    }
//}
