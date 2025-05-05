package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.AppointmentFile;
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

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class AppointmentFileDeoTest {

    private static final long FILE_ID = 1L;
    private static final long APPOINTMENT_ID = 1L;

    private AppointmentFileDaoImpl fileDao;
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        this.fileDao = new AppointmentFileDaoImpl(ds);
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testCreate() {
        //Preconditions
        String fileName = "test.pdf";
        byte[] fileData = new byte[]{1, 2, 3, 4, 5};
        String uploaderRole = "doctor";
        long appointmentId = 2L;

        //Exercise
        AppointmentFile createdFile = fileDao.create(fileName, fileData, uploaderRole, appointmentId);

        //Postconditions
        assertNotNull(createdFile);
        assertEquals(fileName, createdFile.getFileName());
        assertArrayEquals(fileData, createdFile.getFileData());
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
        List<AppointmentFile> maybeImage = fileDao.getByAppointmentId(APPOINTMENT_ID);

        //Postconditions
        assertFalse(maybeImage.isEmpty());
        assertEquals(FILE_ID, maybeImage.getFirst().getId());
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, maybeImage.getFirst().getFileData());
    }
}
