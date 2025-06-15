package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentFile;
import ar.edu.itba.paw.persistence.hibernate.AppointmentFileDaoHibeImpl;
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

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class AppointmentFileDeoTest {

    private static final String APPOINTMENTFILE_TABLE = "appointment_files";

    private static final long FILE_ID = 1L;
    private static final long APPOINTMENT_ID = 1L;

    @Autowired
    private AppointmentFileDaoHibeImpl fileDao;

    private JdbcTemplate jdbcTemplate;

    @PersistenceContext
    private EntityManager em;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Rollback
    @Test
    public void testCreate() {
        //Preconditions
        String fileName = "test.pdf";
        byte[] fileData = new byte[]{1, 2, 3, 4, 5};
        String uploaderRole = "doctor";
        Appointment managedAppointment = em.getReference(Appointment.class, APPOINTMENT_ID);

        //Exercise
        AppointmentFile createdFile = fileDao.create(fileName, fileData, uploaderRole, managedAppointment);
        em.flush();

        //Postconditions
        assertNotNull(createdFile);
        assertEquals(fileName, createdFile.getFileName());
        assertArrayEquals(fileData, createdFile.getFileData());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, APPOINTMENTFILE_TABLE, "id = " + createdFile.getId()));
    }

    @Test
    public void testGetByAppointmentIdForDoctorDoesNotExist() {
        //Preconditions

        //Exercise
        List<AppointmentFile> maybeFiles = fileDao.getByAppointmentIdForDoctor(1000L);

        //Postconditions
        assertTrue(maybeFiles.isEmpty());
    }

    @Test
    public void testGetBtyAppointmentIdForDoctorExists() {
        //Preconditions

        //Exercise
        List<AppointmentFile> maybeFiles = fileDao.getByAppointmentIdForDoctor(APPOINTMENT_ID);

        //Postconditions
        assertFalse(maybeFiles.isEmpty());
        assertEquals(FILE_ID, maybeFiles.getFirst().getId());
        assertEquals("informe.pdf", maybeFiles.getLast().getFileName());
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, maybeFiles.getFirst().getFileData());
    }

    @Test
    public void testGetByIdDoesNotExist() {
        //Preconditions

        //Exercise
        Optional<AppointmentFile> maybeFile = fileDao.getById(1000L);

        //Postconditions
        assertFalse(maybeFile.isPresent());
    }

    @Test
    public void testGetByIdExists() {
        //Preconditions

        //Exercise
        Optional<AppointmentFile> maybeFile = fileDao.getById(1L);

        //Postconditions
        assertTrue(maybeFile.isPresent());
        assertEquals(FILE_ID, maybeFile.get().getId());
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, maybeFile.get().getFileData());
    }

    @Test
    public void testGetByAppointmentIdDoesNotExist() {
        //Preconditions

        //Exercise
        List<AppointmentFile> maybeImage = fileDao.getByAppointmentId(1000L);

        //Postconditions
        assertTrue(maybeImage.isEmpty());
    }

    @Test
    public void testGetByAppointmentIdExists() {
        //Preconditions

        //Exercise
        List<AppointmentFile> maybeImage = fileDao.getByAppointmentId(APPOINTMENT_ID);

        //Postconditions
        assertFalse(maybeImage.isEmpty());
        assertEquals(FILE_ID, maybeImage.getFirst().getId());
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, maybeImage.getFirst().getFileData());
    }

    @Test
    public void testGetAllFilesForPatientDoesNotExist() {
        //Preconditions
        int pageNumber = 1;
        int pageSize = 10;

        //Exercise
        List<AppointmentFile> files = fileDao.getAllFilesForPatient(1000L, pageNumber, pageSize);

        //Postconditions
        assertTrue(files.isEmpty());
    }

    @Test
    public void testGetAllFilesForPatientExists() {
        //Preconditions
        long patientId = 1L;
        int pageNumber = 1;
        int pageSize = 10;

        //Exercise
        List<AppointmentFile> files = fileDao.getAllFilesForPatient(patientId, pageNumber, pageSize);

        //Postconditions
        assertFalse(files.isEmpty());
        assertEquals(2, files.size());
    }

    @Test
    public void testGetAllAppointmentsForPatientCountDoesNotExist() {
        //Preconditions

        //Exercise
        int count = fileDao.getAllFilesForPatientCount(1000L);

        //Postconditions
        assertEquals(0, count);
    }

    @Test
    public void testGetAllFilesForPatientCountExists() {
        //Preconditions
        long patientId = 1L;

        //Exercise
        int count = fileDao.getAllFilesForPatientCount(patientId);

        //Postconditions
        assertEquals(2, count);
    }

    @Test
    public void testGetFilesByAppointmentIdsDoNotExist() {
        //Preconditions
        List<Long> appointmentIds = List.of(1000L, 2000L);

        //Exercise
        List<AppointmentFile> files = fileDao.getFilesByAppointmentIds(appointmentIds);

        //Postconditions
        assertTrue(files.isEmpty());
    }

    @Test
    public void testGetFilesByAppointmentIdsExist() {
        //Preconditions
        List<Long> appointmentIds = List.of(APPOINTMENT_ID, 2L);

        //Exercise
        List<AppointmentFile> files = fileDao.getFilesByAppointmentIds(appointmentIds);

        //Postconditions
        assertFalse(files.isEmpty());
        assertEquals(2, files.size());
    }
}
