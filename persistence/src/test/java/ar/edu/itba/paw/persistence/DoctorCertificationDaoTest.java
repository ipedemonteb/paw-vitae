package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.DoctorCertificationDao;
import ar.edu.itba.paw.interfacePersistence.DoctorExperienceDao;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorCertification;
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
public class DoctorCertificationDaoTest {

    private static final long DOCTOR_ID = 2L;
    private static final String DOCTOR_CERTIFICATIONS_TABLE = "Doctor_Certifications";
    private static final String CERTIFICATE_NAME = "Certificación en Cardiología";
    private static final String ISSUING_ENTITY = "Sociedad Argentina de Cardiología";
    private static final LocalDate ISSUE_DATE = LocalDate.of(2016, 1, 1);

    @Autowired
    private DoctorCertificationDao doctorCertificationDao;
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
        Doctor doctor = em.find(Doctor.class, DOCTOR_ID);
        String certificateName = "Capacitación en Cirugía General";
        String issuingEntity = "UBA";
        LocalDate issueDate = LocalDate.of(2018, 1, 1);

        //Exercise
        DoctorCertification newCertification = doctorCertificationDao.create(doctor, certificateName, issuingEntity, issueDate);
        em.flush();

        //Postconditions
        assertNotNull(newCertification);
        assertEquals(DOCTOR_ID, newCertification.getDoctor().getId());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCTOR_CERTIFICATIONS_TABLE, "doctor_id = " + DOCTOR_ID +
                " AND certificate_name = '" + certificateName + "' AND issuing_entity = '" + issuingEntity +
                "' AND issue_date = '" + issueDate + "'"));
    }

    @Test
    public void testGetByDoctorIdDoesNotExist() {
        //Preconditions

        //Exercise
        List<DoctorCertification> certifications = doctorCertificationDao.getByDoctorId(1000L);

        //Postconditions
        assertTrue(certifications.isEmpty());
    }

    @Test
    public void testGetByDoctorIdExists() {
        //Preconditions

        //Exercise
        List<DoctorCertification> certifications = doctorCertificationDao.getByDoctorId(DOCTOR_ID);

        //Postconditions
        assertFalse(certifications.isEmpty());
        assertEquals(1, certifications.size());
        assertEquals(DOCTOR_ID, certifications.getFirst().getDoctor().getId());
        assertEquals(CERTIFICATE_NAME, certifications.getFirst().getCertificateName());
        assertEquals(ISSUING_ENTITY, certifications.getFirst().getIssuingEntity());
        assertEquals(ISSUE_DATE, certifications.getFirst().getIssueDate());
    }

    @Rollback
    @Test
    public void testDelete() {
        //Preconditions
        long certificationId = 1L;

        //Exercise
        doctorCertificationDao.delete(certificationId);
        em.flush();

        //Postconditions
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, DOCTOR_CERTIFICATIONS_TABLE, "doctor_id = " + DOCTOR_ID));
    }
}
