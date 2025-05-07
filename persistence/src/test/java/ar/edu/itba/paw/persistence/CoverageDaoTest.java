package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Coverage;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class CoverageDaoTest {

    private CoverageDaoImpl coverageDao;
    private JdbcTemplate jdbcTemplate;

    private static final long COVERAGE_ID_1 = 1;
    private static final String COVERAGE_NAME_1 = "Coverage A";
    private static final String COVERAGE_NAME_2 = "Coverage B";

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        coverageDao = new CoverageDaoImpl(ds);
        jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testCreate() {
        //Preconditions
        String testCoverageName = "TestCoverage";

        //Exercise
        Coverage test_Coverage = coverageDao.create(testCoverageName);

        //Postconditions
        assertNotNull(test_Coverage);
        assertEquals(testCoverageName, test_Coverage.getName());
    }

    @Test
    public void testFindByIdDoesNotExist() {
        //Preconditions

        //Exercise
        Optional<Coverage> coverage = coverageDao.findById(1000L);

        //Postconditions
        assertFalse(coverage.isPresent());
    }

    @Test
    public void testFindByIdExists() {
        //Preconditions

        //Exercise
        Optional<Coverage> maybeCoverage = coverageDao.findById(COVERAGE_ID_1);

        //Postconditions
        assertTrue(maybeCoverage.isPresent());
        assertEquals(COVERAGE_ID_1, maybeCoverage.get().getId());
        assertEquals(COVERAGE_NAME_1, maybeCoverage.get().getName());
    }

    @Test
    public void testFindByNameDoesNotExist() {
        //Preconditions

        //Exercise
        Optional<Coverage> coverage = coverageDao.findByName("NoCoverage");

        //Postconditions
        assertFalse(coverage.isPresent());
    }

    @Test
    public void testFindByNameExists() {
        //Preconditions

        //Exercise
        Optional<Coverage> maybeCoverage = coverageDao.findByName(COVERAGE_NAME_1);

        //Postconditions
        assertTrue(maybeCoverage.isPresent());
        assertEquals(COVERAGE_ID_1, maybeCoverage.get().getId());
        assertEquals(COVERAGE_NAME_1, maybeCoverage.get().getName());
    }

    @Test
    public void testGetAll() {
        //Preconditions

        //Exercise
        List<Coverage> coverages = coverageDao.getAll();

        //Postconditions
        List<String> names = coverages.stream().map(Coverage::getName).toList();
        assertEquals(2, coverages.size());
        assertTrue(names.containsAll(List.of(COVERAGE_NAME_1, COVERAGE_NAME_2)));
    }

    @Test
    public void testFindByIds(){
        //Preconditions
        List<Long> ids = List.of(1L, 2L);

        //Exercise
        List<Coverage> coverages = coverageDao.findByIds(ids);

        //Postconditions
        //Postconditions
        assertEquals(2, coverages.size());
        assertTrue(coverages.stream().allMatch(s -> ids.contains(s.getId())));
    }

    @Test
    public void testFindByIdsEmpty() {
        //Preconditions
        List<Long> ids = List.of(1000L);

        //Exercise
        List<Coverage> specialties = coverageDao.findByIds(ids);

        //Postconditions
        assertTrue(specialties.isEmpty());
    }
}