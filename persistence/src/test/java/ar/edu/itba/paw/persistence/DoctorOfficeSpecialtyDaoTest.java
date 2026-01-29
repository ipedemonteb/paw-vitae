package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.DoctorOfficeSpecialtyDao;
import ar.edu.itba.paw.models.DoctorOfficeSpecialty;
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

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class DoctorOfficeSpecialtyDaoTest {

    @Autowired
    private DoctorOfficeSpecialtyDao doctorOfficeSpecialtyDao;
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
    public void testGetByOfficeIdDoesNotExist() {
        //Preconditions
        long officeId = 1000L;

        //Exercise
        List<DoctorOfficeSpecialty> specialties = doctorOfficeSpecialtyDao.getByOfficeId(officeId);

        //Postconditions
        assertTrue(specialties.isEmpty());
    }

    @Test
    public void testGetByOfficeIdExists() {
        //Preconditions
        long officeId = 2L;

        //Exercise
        List<DoctorOfficeSpecialty> specialties = doctorOfficeSpecialtyDao.getByOfficeId(officeId);

        //Postconditions
        assertFalse(specialties.isEmpty());
        assertEquals(2, specialties.size());
    }
}
