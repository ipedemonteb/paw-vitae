//package ar.edu.itba.paw.persistence;
//
//import ar.edu.itba.paw.interfacePersistence.AvailabilitySlotsDao;
//import ar.edu.itba.paw.models.AvailabilitySlot;
//import ar.edu.itba.paw.models.Doctor;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//import org.springframework.test.jdbc.JdbcTestUtils;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.persistence.EntityManager;
//import javax.persistence.PersistenceContext;
//import javax.sql.DataSource;
//import java.sql.Time;
//import java.time.LocalTime;
//import java.util.List;
//import java.util.Map;
//
//import static org.junit.Assert.*;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(classes = TestConfig.class)
//@Transactional
//public class AvailabilitySlotDaoTest {
//
//    private static final String DOCAVAILABILITY_TABLE = "doctor_availability";
//
//    private static final long DOC_ID = 2L;
//
//    @Autowired
//    private AvailabilitySlotsDao availabilitySlotsDao;
//    private JdbcTemplate jdbcTemplate;
//
//    @PersistenceContext
//    private EntityManager em;
//
//    @Autowired
//    private DataSource ds;
//
//    @Before
//    public void setUp() {
//        this.jdbcTemplate = new JdbcTemplate(ds);
//    }
//
//    @Rollback
//    @Test
//    public void testCreate() {
//        //Preconditions
//        int dayOfWeek = 1,
//                startTimeHour = 8,
//                startTimeMinute = 0,
//                endTimeHour = 16;
//        Doctor mangedDoctor = em.find(Doctor.class, DOC_ID);
//        AvailabilitySlot slot = new AvailabilitySlot(mangedDoctor,
//                dayOfWeek,
//                LocalTime.of(startTimeHour, startTimeMinute),
//                LocalTime.of(endTimeHour, startTimeMinute)
//        );
//
//        //Exercise
//        AvailabilitySlot createdSlot = availabilitySlotsDao.create(slot);
//        em.flush();
//
//        //Postconditions
//        assertNotNull(createdSlot);
//        assertEquals(dayOfWeek, createdSlot.getDayOfWeek());
//        assertEquals(LocalTime.of(startTimeHour, startTimeMinute), createdSlot.getStartTime());
//        assertEquals(LocalTime.of(endTimeHour, startTimeMinute), createdSlot.getEndTime());
//        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCAVAILABILITY_TABLE, "doctor_id = " + DOC_ID +
//                " AND day_of_week = " + dayOfWeek +
//                " AND start_time = '" + Time.valueOf(LocalTime.of(startTimeHour, startTimeMinute)) + "'" +
//                " AND end_time = '" + Time.valueOf(LocalTime.of(endTimeHour, startTimeMinute)) + "'"));
//    }
//
//    @Rollback
//    @Test
//    public void testUpdateDoctorAvailability() {
//        //Preconditions
//        int dayOfWeek1 = 3;
//        int dayOfWeek2 = 4;
//        int startTimeHour = 8;
//        int startTimeMinute = 0;
//        int endTimeHour = 16;
//        Doctor mangedDoctor = em.find(Doctor.class, DOC_ID);
//        List<AvailabilitySlot> availability = List.of(
//                new AvailabilitySlot(mangedDoctor, dayOfWeek1,
//                        LocalTime.of(startTimeHour, startTimeMinute),
//                        LocalTime.of(endTimeHour, startTimeMinute)),
//                new AvailabilitySlot(mangedDoctor, dayOfWeek2,
//                        LocalTime.of(startTimeHour, startTimeMinute),
//                        LocalTime.of(endTimeHour, startTimeMinute))
//        );
//
//        //Exercise
//        availabilitySlotsDao.updateDoctorAvailability(DOC_ID, availability);
//        em.flush();
//
//        //Postconditions
//        assertEquals(2, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCAVAILABILITY_TABLE, "doctor_id = " + DOC_ID +
//                " AND start_time = '" + Time.valueOf(LocalTime.of(startTimeHour, startTimeMinute)) + "'" +
//                " AND end_time = '" + Time.valueOf(LocalTime.of(endTimeHour, startTimeMinute)) + "'"));
//    }
//
//    @Test
//    public void testGetAvailabilityByDoctorIdDoesNotExist() {
//        //Preconditions
//        long docId = 1000L;
//
//        //Exercise
//        List<AvailabilitySlot> finalAvailability = availabilitySlotsDao.getAvailabilityByDoctorId(docId);
//
//        //Postconditions
//        assertTrue(finalAvailability.isEmpty());
//    }
//
//    @Test
//    public void testGetAvailabilityByDoctorIdExists() {
//        //Preconditions
//        long docId = 4L;
//
//        //Exercise
//        List<AvailabilitySlot> finalAvailability = availabilitySlotsDao.getAvailabilityByDoctorId(docId);
//
//        //Postconditions
//        assertFalse(finalAvailability.isEmpty());
//        assertEquals(7, finalAvailability.size());
//    }
//}
