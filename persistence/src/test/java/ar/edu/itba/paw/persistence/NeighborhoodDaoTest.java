package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.NeighborhoodDao;
import ar.edu.itba.paw.interfacePersistence.RatingDao;
import ar.edu.itba.paw.models.Neighborhood;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
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
public class NeighborhoodDaoTest {

    @Autowired
    private NeighborhoodDao neighborhoodDao;
    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testGetByIdDoesNotExist() {
        //Preconditions

        //Exercise
        Optional<Neighborhood> maybeNeighborhood = neighborhoodDao.getById(1000L);

        //Postconditions
        assertTrue(maybeNeighborhood.isEmpty());
    }

    @Test
    public void testGetByIdDoesExists() {
        //Preconditions
        long neighborhoodId = 1L;

        //Exercise
        Optional<Neighborhood> maybeNeighborhood = neighborhoodDao.getById(neighborhoodId);

        //Postconditions
        assertFalse(maybeNeighborhood.isEmpty());
        assertEquals("Recoleta", maybeNeighborhood.get().getName());
    }

    @Test
    public void testGetAll() {
        //Preconditions

        //Exercise
        List<Neighborhood> neighborhoods = neighborhoodDao.getAll();

        //Postconditions
        assertFalse(neighborhoods.isEmpty());
        assertEquals(2, neighborhoods.size());
        assertEquals("Recoleta", neighborhoods.getFirst().getName());
        assertEquals("Belgrano", neighborhoods.getLast().getName());
    }
}
