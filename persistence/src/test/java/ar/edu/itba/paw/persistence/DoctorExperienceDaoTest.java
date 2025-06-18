package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.DoctorExperienceDao;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorExperience;
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
import java.time.LocalDate;
import java.util.List;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class DoctorExperienceDaoTest {

    private static final long DOCTOR_ID = 2L;
    private static final String DOCTOR_EXPERIENCE_TABLE = "doctor_experience";
    private static final String POSITION = "Residente";
    private static final String ORGANIZATION = "Hospital Aleman";
    private static final LocalDate START = LocalDate.of(2010, 1, 1);
    private static final LocalDate END = LocalDate.of(2016, 1, 1);

    @Autowired
    private DoctorExperienceDao doctorExperienceDao;
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
        Doctor doctor = em.getReference(Doctor.class, DOCTOR_ID);
        String title = "Cirujano";
        String orgName = "Hospital Italiano";
        LocalDate startDate = LocalDate.of(2018, 1, 1);
        LocalDate endDate = LocalDate.of(2020, 1, 1);

        //Exercise
        DoctorExperience newExperience = doctorExperienceDao.create(doctor, title, orgName, startDate, endDate);

        //Postconditions
        assertNotNull(newExperience);
        assertEquals(DOCTOR_ID, newExperience.getDoctor().getId());
        assertEquals(title, newExperience.getPositionTitle());
        assertEquals(orgName, newExperience.getOrganizationName());
        assertEquals(startDate, newExperience.getStartDate());
        assertEquals(endDate, newExperience.getEndDate());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCTOR_EXPERIENCE_TABLE, "doctor_id = " + DOCTOR_ID +
                " AND position_title = '" + title + "' AND organization_name = '" + orgName +
                "' AND start_date = '" + startDate + "' AND end_date = '" + endDate + "'"));
    }

    @Test
    public void testGetByDoctorIdDoesNotExist() {
        //Preconditions

        //Exercise
        List<DoctorExperience> experiences = doctorExperienceDao.getByDoctorId(1000L);

        //Postconditions
        assertTrue(experiences.isEmpty());
    }

    @Test
    public void testGetByDoctorIdExists() {
        //Preconditions

        //Exercise
        List<DoctorExperience> experiences = doctorExperienceDao.getByDoctorId(DOCTOR_ID);

        //Postconditions
        assertFalse(experiences.isEmpty());
        assertEquals(1, experiences.size());
        assertEquals(DOCTOR_ID, experiences.getFirst().getDoctor().getId());
        assertEquals(POSITION, experiences.getFirst().getPositionTitle());
        assertEquals(ORGANIZATION, experiences.getFirst().getOrganizationName());
        assertEquals(START, experiences.getFirst().getStartDate());
        assertEquals(END, experiences.getFirst().getEndDate());
    }

    @Rollback
    @Test
    public void testDelete() {
        //Preconditions
        long experienceId = 1L;

        //Exercise
        doctorExperienceDao.delete(experienceId);
        em.flush();

        //Postconditions
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCTOR_EXPERIENCE_TABLE, "doctor_id = " + DOCTOR_ID));
    }
}
