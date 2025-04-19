//package ar.edu.itba.paw.persistence;
//
//import ar.edu.itba.paw.models.AvailabilitySlot;
//import ar.edu.itba.paw.models.Coverage;
//import ar.edu.itba.paw.models.Specialty;
//import ar.edu.itba.paw.models.Doctor;
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
//import java.sql.Time;
//import java.time.LocalDateTime;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//import static org.junit.Assert.*;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestConfig.class)
//public class DoctorDaoTest {
//
//    private static final String USERS_TABLE = "users";
//    private static final String DOCTORS_TABLE = "doctors";
//    private static final String COVERAGES_TABLE = "coverages";
//    private static final String SPECIALTIES_TABLE = "specialties";
//    private static final String DOCTOR_COVERAGES_TABLE = "doctor_coverages";
//    private static final String DOCTOR_SPECIALTIES_TABLE = "doctor_specialties";
//
//    private static final String NAME = "Carlos Salvador";
//    private static final String LASTNAME = "Bilardo";
//    private static final String EMAIL = "carlos.s.bilardo@test.com";
//    private static final String PASSWORD = "password";
//    private static final String PHONE = "1119681969";
//
//    private DoctorDaoImpl doctorDao;
//    private JdbcTemplate jdbcTemplate;
//    private SimpleJdbcInsert jdbcInsertCoverage;
//    private SimpleJdbcInsert jdbcInsertSpecialty;
//
//    @Autowired
//    private DataSource ds;
//
//    @Before
//    public void setUp() {
//        jdbcTemplate = new JdbcTemplate(ds);
//        doctorDao = new DoctorDaoImpl(ds);
//
//        jdbcInsertCoverage = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName(COVERAGES_TABLE)
//                .usingGeneratedKeyColumns("id");
//
//        jdbcInsertSpecialty = new SimpleJdbcInsert(jdbcTemplate)
//                .withTableName(SPECIALTIES_TABLE)
//                .usingGeneratedKeyColumns("id");
//
//        JdbcTestUtils.deleteFromTables(jdbcTemplate, DOCTOR_SPECIALTIES_TABLE, DOCTOR_COVERAGES_TABLE,
//                DOCTORS_TABLE, USERS_TABLE, COVERAGES_TABLE, SPECIALTIES_TABLE);
//    }
//
//    @Test
//    public void testCreateDoctor() {
//        //Preconditions
//        long coverageId = jdbcInsertCoverage.executeAndReturnKey(Map.of("coverage_name", "OSDE")).longValue();
//        long specialtyId = jdbcInsertSpecialty.executeAndReturnKey(Map.of("key", "Cardiology")).longValue();
//        Coverage coverage = new Coverage(coverageId, "OSDE");
//        Specialty specialty = new Specialty(specialtyId, "Cardiology");
//        List<Coverage> coverages = List.of(coverage);
//        List<Specialty> specialties = List.of(specialty);
//        List<AvailabilitySlot> availability = List.of(new AvailabilitySlot(1,
//                LocalTime.of(8, 0),
//                LocalTime.of(16, 0)));
//
//        //Exercise
//        Doctor doctor = doctorDao.create(NAME, LASTNAME, EMAIL, PASSWORD, PHONE, "es", specialties, coverages, availability);
//
//        //Postconditions
//        assertNotNull(doctor);
//        assertEquals(EMAIL, doctor.getEmail());
//        assertEquals(NAME, doctor.getName());
//        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, USERS_TABLE));
//        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, DOCTORS_TABLE));
//        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, DOCTOR_COVERAGES_TABLE));
//        assertEquals(1, JdbcTestUtils.countRowsInTable(jdbcTemplate, DOCTOR_SPECIALTIES_TABLE));
//    }
//
//    @Test
//    public void testGetByIdDoesNotExist() {
//        //Preconditions
//
//        //Exercise
//        Optional<Doctor> maybeDoctor = doctorDao.getById(1000L);
//
//        //Postconditions
//        assertFalse(maybeDoctor.isPresent());
//    }
//
//    @Test
//    public void testGetByIdExists() {
//        //Preconditions
//        long coverageId = jdbcInsertCoverage.executeAndReturnKey(Map.of("coverage_name", "OSDE")).longValue();
//        long specialtyId = jdbcInsertSpecialty.executeAndReturnKey(Map.of("key", "Cardiology")).longValue();
//        Coverage coverage = new Coverage(coverageId, "OSDE");
//        Specialty specialty = new Specialty(specialtyId, "Cardiology");
//        List<AvailabilitySlot> availability = List.of(new AvailabilitySlot(1,
//                LocalTime.of(8, 0),
//                LocalTime.of(16, 0))
//        );
//        Doctor created = doctorDao.create(NAME, LASTNAME, EMAIL, PASSWORD, PHONE, "es", List.of(specialty), List.of(coverage), availability);
//
//        //Exercise
//        Optional<Doctor> maybeDoctor = doctorDao.getById(created.getId());
//
//        //Postconditions
//        assertTrue(maybeDoctor.isPresent());
//        Doctor doctor = maybeDoctor.get();
//        assertEquals(created.getId(), doctor.getId());
//        assertEquals(NAME, doctor.getName());
//        assertEquals(1, doctor.getCoverageList().size());
//        assertEquals(1, doctor.getSpecialtyList().size());
//    }
//
//    @Test
//    public void testGetByEmailDoesNotExist() {
//        //Preconditions
//
//        //Excercise
//        Optional<Doctor> maybeDoctor = doctorDao.getByEmail("notexists@gmail.com");
//
//        //Postconditions
//        assertFalse(maybeDoctor.isPresent());
//    }
//
//    @Test
//    public void testGetByEmailExists() {
//        //Preconditions
//        long coverageId = jdbcInsertCoverage.executeAndReturnKey(Map.of("coverage_name", "OSDE")).longValue();
//        long specialtyId = jdbcInsertSpecialty.executeAndReturnKey(Map.of("key", "Cardiology")).longValue();
//        Coverage coverage = new Coverage(coverageId, "OSDE");
//        Specialty specialty = new Specialty(specialtyId, "Cardiology");
//        List<AvailabilitySlot> availability = List.of(new AvailabilitySlot(1,
//                LocalTime.of(8, 0),
//                LocalTime.of(16, 0)));
//        Doctor created = doctorDao.create(NAME, LASTNAME, EMAIL, PASSWORD, PHONE, "es", List.of(specialty), List.of(coverage), availability);
//
//        //Excercise
//        Optional<Doctor> maybeDoctor = doctorDao.getByEmail(EMAIL);
//
//        //Postconditions
//        assertTrue(maybeDoctor.isPresent());
//        Doctor doc = maybeDoctor.get();
//        assertEquals(created.getId(), doc.getId());
//        assertEquals(EMAIL, doc.getEmail());
//        assertEquals(1, doc.getCoverageList().size());
//        assertEquals(1, doc.getSpecialtyList().size());
//    }
//
//    @Test
//    public void testGetAll() {
//        //Preconditions
//        long coverageId = jdbcInsertCoverage.executeAndReturnKey(Map.of("coverage_name", "OSDE")).longValue();
//        long specialtyId = jdbcInsertSpecialty.executeAndReturnKey(Map.of("key", "Cardiology")).longValue();
//        Coverage coverage = new Coverage(coverageId, "OSDE");
//        Specialty specialty = new Specialty(specialtyId, "Cardiology");
//        List<AvailabilitySlot> availability = List.of(new AvailabilitySlot(1,
//                LocalTime.of(8, 0),
//                LocalTime.of(16, 0)));
//        Doctor doctor = doctorDao.create(NAME, LASTNAME, EMAIL, PASSWORD, PHONE, "es", List.of(specialty), List.of(coverage), availability);
//
//        //Exercise
//        List<Doctor> doctors = doctorDao.getAll();
//
//        //Postconditions
//        assertNotNull(doctors);
//        assertEquals(1, doctors.size());
//        Doctor foundDoctor = doctors.get(0);
//        assertEquals(doctor.getId(), foundDoctor.getId());
//        assertEquals(NAME, foundDoctor.getName());
//        assertEquals(1, foundDoctor.getCoverageList().size());
//        assertEquals(1, foundDoctor.getSpecialtyList().size());
//        assertEquals("OSDE", foundDoctor.getCoverageList().get(0).getName());
//        assertEquals("Cardiology", foundDoctor.getSpecialtyList().get(0).getKey());
//    }
//
//    @Test
//    public void testGetAllEmpty() {
//        //Preconditions
//
//        //Exercise
//        List<Doctor> doctors = doctorDao.getAll();
//
//        //Postconditions
//        assertNotNull(doctors);
//        assertTrue(doctors.isEmpty());
//    }
//
//    @Test
//    public void testGetBySpecialty() {
//        //Preconditions
//        long coverageId = jdbcInsertCoverage.executeAndReturnKey(Map.of("coverage_name", "OSDE")).longValue();
//        long specialtyId = jdbcInsertSpecialty.executeAndReturnKey(Map.of("key", "Cardiology")).longValue();
//        Coverage coverage = new Coverage(coverageId, "OSDE");
//        Specialty specialty = new Specialty(specialtyId, "Cardiology");
//        List<AvailabilitySlot> availability = List.of(new AvailabilitySlot(1,
//                LocalTime.of(8, 0),
//                LocalTime.of(16, 0)));
//        Doctor doctor = doctorDao.create(NAME, LASTNAME, EMAIL, PASSWORD, PHONE, "es", List.of(specialty), List.of(coverage), availability);
//
//        //Exercise
//        List<Doctor> doctors = doctorDao.getBySpecialty("Cardiology");
//
//        //Postconditions
//        assertNotNull(doctors);
//        assertEquals(1, doctors.size());
//        Doctor foundDoctor = doctors.get(0);
//        assertEquals(doctor.getId(), foundDoctor.getId());
//        assertEquals("OSDE", foundDoctor.getCoverageList().get(0).getName());
//        assertEquals("Cardiology", foundDoctor.getSpecialtyList().get(0).getKey());
//    }
//
//    @Test
//    public void testGetBySpecialtyEmpty() {
//        //Preconditions
//
//        //Exercise
//        List<Doctor> doctors = doctorDao.getBySpecialty("Pediatrics");
//
//        //Postconditions
//        assertNotNull(doctors);
//        assertTrue(doctors.isEmpty());
//    }
//
//    //@TODO: Implement update test
//}
//
