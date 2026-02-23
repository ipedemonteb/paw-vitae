package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.models.Images;
import ar.edu.itba.paw.persistence.hibernate.ImageDaoHibeImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
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
public class ImageDaoTest {

    private static final String IMAGE_TABLE = "images";

    private static final long IMAGE_ID = 1L;

    @Autowired
    private ImageDaoHibeImpl imageDao;

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
        byte[] image = new byte[]{1, 2, 3, 4, 5};

        //Exercise
        Images createdImage = imageDao.create(image);
        em.flush();

        //Postconditions
        assertNotNull(createdImage);
        assertEquals(image.length, createdImage.getImage().length);
        assertArrayEquals(image, createdImage.getImage());
        assertEquals(1, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, IMAGE_TABLE, "id = " + createdImage.getId()));
    }

    @Rollback
    @Test
    public void testDeleteImage() {
        //Preconditions
        long imageId = 1L;

        //Exercise
        imageDao.deleteImage(imageId);
        em.flush();

        //Postconditions
        assertEquals(0, JdbcTestUtils.countRowsInTableWhere(jdbcTemplate, IMAGE_TABLE, "id = " + imageId));
    }

    @Test
    public void testFindByIdDoesNotExist() {
        //Preconditions

        //Exercise
        Optional<Images> maybeImage = imageDao.findById(1000L);

        //Postconditions
        assertFalse(maybeImage.isPresent());
    }

    @Test
    public void testFindByIdExists() {
        //Preconditions

        //Exercise
        Optional<Images> maybeImage = imageDao.findById(IMAGE_ID);

        //Postconditions
        assertTrue(maybeImage.isPresent());
        assertEquals(IMAGE_ID, maybeImage.get().getId());
        assertArrayEquals(new byte[]{1, 2, 3, 4, 5}, maybeImage.get().getImage());
    }
}
