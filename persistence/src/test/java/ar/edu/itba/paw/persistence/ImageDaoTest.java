package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.models.Images;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.sql.DataSource;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestConfig.class)
@Transactional
public class ImageDaoTest {

    private static final long IMAGE_ID = 1L;

    private ImageDaoImpl imageDao;
    private JdbcTemplate jdbcTemplate;
    private DoctorDao mockDoctor;

    @Autowired
    private DataSource ds;

    @Before
    public void setUp() {
        this.mockDoctor = Mockito.mock(DoctorDao.class);
        this.imageDao = new ImageDaoImpl(ds);
        this.jdbcTemplate = new JdbcTemplate(ds);
    }

    @Test
    public void testCreate() {
        //Preconditions
        byte[] image = new byte[]{1, 2, 3, 4, 5};

        //Exercise
        Images createdImage = imageDao.create(image);

        //Postconditions
        assertNotNull(createdImage);
        assertEquals(image.length, createdImage.getImage().length);
        assertArrayEquals(image, createdImage.getImage());
    }

    @Test
    public void testGetByIdDoesNotExist() {
        //Preconditions

        //Exercise
        Optional<Images> maybeImage = imageDao.findById(1000L);

        //Postconditions
        assertFalse(maybeImage.isPresent());
    }

    @Test
    public void testGetByIdExists() {

        // Exercise
        Optional<Images> maybeImage = imageDao.findById(IMAGE_ID);

        // Postconditions
        assertTrue(maybeImage.isPresent());
        assertEquals(IMAGE_ID, maybeImage.get().getId());
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, maybeImage.get().getImage());
    }
}
