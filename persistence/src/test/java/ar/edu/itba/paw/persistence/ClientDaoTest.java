//package ar.edu.itba.paw.persistence;
//
//import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
//import ar.edu.itba.paw.models.Client;
//import ar.edu.itba.paw.models.Coverage;
//import org.junit.Assert;
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
//
//import static org.junit.Assert.*;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.when;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestConfig.class)
//public class ClientDaoTest {
//
//    private static final String USERS_TABLE = "users";
//    private static final String CLIENTS_TABLE = "clients";
//    private static final String COVERAGES_TABLE = "coverages";
//
//    private static final String NAME = "John";
//    private static final String LASTNAME = "Doe";
//    private static final String EMAIL = "john.doe@test.com";
//    private static final String PASSWORD = "password";
//    private static final String PHONE = "1177777777";
//    private static final String LANGUAGE = "es";
//
//    private ClientDaoImpl clientDao;
//    private JdbcTemplate jdbcTemplate;
//    private SimpleJdbcInsert jdbcInsertCoverage;
//
//    @Autowired
//    private DataSource ds;
//
//    private AppointmentDao mockAppointmentDao;
//
//    @Before
//    public void setUp() {
//        mockAppointmentDao = mock(AppointmentDao.class);
//        when(mockAppointmentDao.getByClientId(anyLong())).thenReturn(Optional.of(List.of()));
//
//        jdbcTemplate = new JdbcTemplate(ds);
//
//        clientDao = new ClientDaoImpl(ds);
//
//        jdbcInsertCoverage = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName(COVERAGES_TABLE)
//                .usingGeneratedKeyColumns("id");
//
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, CLIENTS_TABLE, USERS_TABLE, COVERAGES_TABLE);
//    }
//
//    @Test
//    public void testCreateClient() {
//        //Preconditions
//        Number coverageId = jdbcInsertCoverage.executeAndReturnKey(Map.of("coverage_name", "TestCoverage"));
//        Coverage coverage = new Coverage(coverageId.longValue(), "TestCoverage");
//
//        //Exercise
//        Client client = clientDao.create(NAME, LASTNAME, EMAIL, PASSWORD, PHONE, LANGUAGE, coverage);
//
//        //Postconditions
//        assertNotNull(client);
//        assertEquals(EMAIL, client.getEmail());
//        assertEquals(NAME, client.getName());
//        //agregar where
//        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USERS_TABLE));
//        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, CLIENTS_TABLE));
//    }
//
//    @Test
//    public void testGetByIdDoesNotExist() {
//        //Preconditions
//
//        //Exercise
//        Optional<Client> maybeClient = clientDao.getById(1000);
//
//        //Postconditions
//        Assert.assertFalse(maybeClient.isPresent());
//    }
//
//    @Test
//    public void testGetByIdExists() {
//        //Preconditions
//        Number coverageId = jdbcInsertCoverage.executeAndReturnKey(Map.of("coverage_name", "TestCoverage"));
//        Coverage coverage = new Coverage(coverageId.longValue(), "TestCoverage");
//        Client created = clientDao.create(NAME, LASTNAME, EMAIL, PASSWORD, PHONE, LANGUAGE, coverage);
//
//        //Exercise
//        Optional<Client> maybeClient = clientDao.getById(created.getId());
//
//        //Postconditions
//        assertTrue(maybeClient.isPresent());
//        assertEquals(created.getId(), maybeClient.get().getId());
//        assertEquals(NAME, maybeClient.get().getName());
//        assertEquals(LASTNAME, maybeClient.get().getLastName());
//    }
//
//    @Test
//    public void testGetByEmailDoesNotExist() {
//        //Preconditions
//
//        //Exercise
//        Optional<Client> maybeClient = clientDao.getByEmail("notexists@gmail.com");
//
//        //Postconditions
//        Assert.assertFalse(maybeClient.isPresent());
//    }
//
//    @Test
//    public void testGetByEmailExists() {
//        //Preconditions
//        Number coverageId = jdbcInsertCoverage.executeAndReturnKey(Map.of("coverage_name", "TestCoverage"));
//        Coverage coverage = new Coverage(coverageId.longValue(), "TestCoverage");
//        Client created = clientDao.create(NAME, LASTNAME, EMAIL, PASSWORD, PHONE, LANGUAGE, coverage);
//
//        //Exercise
//        Optional<Client> maybeClient = clientDao.getByEmail(created.getEmail());
//
//        //Postconditions
//        assertTrue(maybeClient.isPresent());
//        assertEquals(created.getId(), maybeClient.get().getId());
//        assertEquals(NAME, maybeClient.get().getName());
//        assertEquals(LASTNAME, maybeClient.get().getLastName());
//    }
//
//
//    @Test
//    public void testUpdateClient() {
//        //Preconditions
//        Number coverageId1 = jdbcInsertCoverage.executeAndReturnKey(Map.of("coverage_name", "TestCoverage"));
//        Coverage initialCoverage = new Coverage(coverageId1.longValue(), "TestCoverage");
//        Number coverageId2 = jdbcInsertCoverage.executeAndReturnKey(Map.of("coverage_name", "UpdatedCoverage"));
//        Coverage updatedCoverage = new Coverage(coverageId2.longValue(), "UpdatedCoverage");
//        Client client = clientDao.create(NAME, LASTNAME, EMAIL, PASSWORD, PHONE, LANGUAGE, initialCoverage);
//        String updatedName = "Jane";
//        String updatedLastName = "Smith";
//        String updatedPhone = "1188888888";
//
//        //Exercise
//        clientDao.updateClient(client.getId(), updatedName, updatedLastName, updatedPhone, updatedCoverage);
//
//        //Postconditions
//        Optional<Client> maybeClient = clientDao.getById(client.getId());
//        assertTrue(maybeClient.isPresent());
//        Client updatedClient = maybeClient.get();
//        assertEquals(updatedName, updatedClient.getName());
//        assertEquals(updatedLastName, updatedClient.getLastName());
//        assertEquals(updatedPhone, updatedClient.getPhone());
//        assertEquals(updatedCoverage.getId(), updatedClient.getCoverage().getId());
//    }
//
//    //@TODO: getbyids
//}