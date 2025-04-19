//package ar.edu.itba.paw.persistence;
//
//import ar.edu.itba.paw.models.Coverage;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.jdbc.JdbcTestUtils;
//
//import javax.sql.DataSource;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//import java.util.stream.Collectors;
//
//import static org.junit.Assert.*;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestConfig.class)
//public class CoverageDaoTest {
//
//    private CoverageDaoImpl coverageDao;
//    private JdbcTemplate jdbcTemplate;
//    private SimpleJdbcInsert jdbcInsertCoverage;
//
//    @Autowired
//    private DataSource ds;
//
//    @Before
//    public void setUp() {
//        coverageDao = new CoverageDaoImpl(ds);
//        jdbcTemplate = new JdbcTemplate(ds);
//        jdbcInsertCoverage = new SimpleJdbcInsert(ds)
//                .withTableName("coverages")
//                .usingGeneratedKeyColumns("id");
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, "coverages");
//    }
//
//    @Test
//    public void testCreate() {
//        //Preconditions
//        String coverage = "TestCoverage";
//
//        //Exercise
//        Coverage newCoverage = coverageDao.create(coverage);
//
//        //Postconditions
//        assertNotNull(newCoverage);
//        assertEquals(coverage, newCoverage.getName());
//    }
//
//    @Test
//    public void testFindByIdDoesNotExist() {
//        //Preconditions
//
//        //Exercise
//        Optional<Coverage> coverage = coverageDao.findById(1);
//
//        //Postconditions
//        assertFalse(coverage.isPresent());
//    }
//
//    @Test
//    public void testFindByIdExists() {
//        //Preconditions
//        Number coverageId = jdbcInsertCoverage.executeAndReturnKey(Map.of("coverage_name", "TestCoverage"));
//
//        //Exercise
//        Optional<Coverage> maybeCoverage = coverageDao.findById(coverageId.longValue());
//
//        //Postconditions
//        assertTrue(maybeCoverage.isPresent());
//        assertEquals(coverageId.longValue(), maybeCoverage.get().getId());
//        assertEquals("TestCoverage", maybeCoverage.get().getName());
//    }
//
//    @Test
//    public void testFindByNameDoesNotExist() {
//        //Preconditions
//
//        //Exercise
//        Optional<Coverage> coverage = coverageDao.findByName("NoCoverage");
//
//        //Postconditions
//        assertFalse(coverage.isPresent());
//    }
//
//    @Test
//    public void testFindByNameExists() {
//        //Preconditions
//        Number coverageId = jdbcInsertCoverage.executeAndReturnKey(Map.of("coverage_name", "TestCoverage"));
//
//        //Exercise
//        Optional<Coverage> maybeCoverage = coverageDao.findByName("TestCoverage");
//
//        //Postconditions
//        assertTrue(maybeCoverage.isPresent());
//        assertEquals(coverageId.longValue(), maybeCoverage.get().getId());
//        assertEquals("TestCoverage", maybeCoverage.get().getName());
//    }
//
//    @Test
//    public void testGetAll() {
//        //Preconditions
//        Number coverageId1 = jdbcInsertCoverage.executeAndReturnKey(Map.of("coverage_name", "TestCoverage1"));
//        Number coverageId2 = jdbcInsertCoverage.executeAndReturnKey(Map.of("coverage_name", "TestCoverage2"));
//
//        //Exercise
//        Optional<List<Coverage>> coverages = coverageDao.getAll();
//
//        //Postconditions
//        assertTrue(coverages.isPresent());
//        List<String> names = coverages.get().stream().map(Coverage::getName).collect(Collectors.toList());
//        assertEquals(2, coverages.get().size());
//        assertTrue(names.containsAll(List.of("TestCoverage1", "TestCoverage2")));
//    }
//}
//
