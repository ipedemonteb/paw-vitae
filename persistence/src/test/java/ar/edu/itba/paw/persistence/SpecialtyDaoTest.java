package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Specialty;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.jdbc.JdbcTestUtils;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
public class SpecialtyDaoTest {

    private SpecialtyDaoImpl specialtyDao;
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    private long cardiologyId;
    private long neurologyId;
    private long orthopedicsId;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
        specialtyDao = new SpecialtyDaoImpl(ds);
        JdbcTestUtils.deleteFromTables(jdbcTemplate, "specialties");
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("specialties")
                .usingGeneratedKeyColumns("id");
        cardiologyId = jdbcInsert.executeAndReturnKey(Map.of("key", "Cardiology")).longValue();
        neurologyId = jdbcInsert.executeAndReturnKey(Map.of("key", "Neurology")).longValue();
        orthopedicsId = jdbcInsert.executeAndReturnKey(Map.of("key", "Orthopedics")).longValue();
    }

    @Test
    public void testGetByIdDoesNotExist() {
        //Preconditions

        //Exercise
        Optional<Specialty> specialty = specialtyDao.getById(1000L);

        //Postconditions
        assertFalse(specialty.isPresent());
    }

    //@TODO: revisar
    @Test
    public void testGetByIdExists() {
        //Preconditions

        //Exercise
        Optional<Specialty> maybeSpeciality = specialtyDao.getById(cardiologyId);

        //Postconditions
        assertTrue(maybeSpeciality.isPresent());
        assertEquals(cardiologyId, maybeSpeciality.get().getId());
        assertEquals("Cardiology", maybeSpeciality.get().getKey());
    }

    @Test
    public void testGetByNameDoesNotExist() {
        //Preconditions

        //Exercise
        Optional<Specialty> specialty = specialtyDao.getByName("NoSpecialty");

        //Postconditions
        assertFalse(specialty.isPresent());
    }

    @Test
    public void testGetByNameExists() {
        //Preconditions

        //Exercise
        Optional<Specialty> fetchedSpecialty = specialtyDao.getByName("Neurology");

        //Postconditions
        assertTrue(fetchedSpecialty.isPresent());
        assertEquals("Neurology", fetchedSpecialty.get().getKey());
    }

    @Test
    public void testGetAll() {
        //Preconditions

        //Exercise
        Optional<List<Specialty>> specialties = specialtyDao.getAll();

        //Postconditions
        assertTrue(specialties.isPresent());
        List<String> keys = specialties.get().stream().map(Specialty::getKey).collect(Collectors.toList());
        assertEquals(3, specialties.get().size());
        assertTrue(keys.containsAll(List.of("Cardiology", "Neurology", "Orthopedics")));
    }
}
