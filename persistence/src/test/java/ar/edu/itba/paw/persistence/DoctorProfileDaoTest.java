package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.DoctorProfileDao;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorProfile;
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
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class DoctorProfileDaoTest {

    private static final String DOCTOR_PROFILE_TABLE = "doctor_profile";
    private static final String DOC_BIO = "Cardiologo con mas de 10 años de experiencia";
    private static final String DOC_DESC = "Especializado en cardiología y enfermedades del corazón";

    @Autowired
    private DoctorProfileDao doctorProfileDao;
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
        long doctorId = 5L;
        Doctor doctor = em.find(Doctor.class, doctorId);
        String bio = "Doctor con mucha experiencia en el campo";
        String description = "Especializado en cardiología y medicina interna";

        //Exercise
        DoctorProfile newDoctor = doctorProfileDao.create(doctor, bio, description);
        em.flush();

        //Postconditions
        assertNotNull(newDoctor);
        assertEquals(doctorId, newDoctor.getDoctor().getId());
        assertEquals(bio, newDoctor.getBio());
        assertEquals(description, newDoctor.getDescription());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCTOR_PROFILE_TABLE, "doctor_id = " + doctorId));
    }

    @Test
    public void testGetByDoctorIdDoesNotExist() {
        //Preconditions

        //Exercise
        Optional<DoctorProfile> doctorProfile = doctorProfileDao.getByDoctorId(1000L);

        //Postconditions
        assertTrue(doctorProfile.isEmpty());
    }

    @Test
    public void testGetByDoctorIdExists() {
        //Preconditions
        long doctorId = 2L;

        //Exercise
        Optional<DoctorProfile> doctorProfile = doctorProfileDao.getByDoctorId(doctorId);

        //Postconditions
        assertFalse(doctorProfile.isEmpty());
        assertEquals(doctorId, doctorProfile.get().getDoctor().getId());
        assertEquals(DOC_BIO, doctorProfile.get().getBio());
        assertEquals(DOC_DESC, doctorProfile.get().getDescription());
    }

}
