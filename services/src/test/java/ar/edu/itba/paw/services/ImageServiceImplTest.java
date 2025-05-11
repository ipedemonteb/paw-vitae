package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.ImageDao;
import ar.edu.itba.paw.models.Images;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImageServiceImplTest {

    @Mock
    private ImageDao imageDao;

    @InjectMocks
    private ImageServiceImpl imageService;

    @Test
    public void testCreateEmptyImage() {
        //Preconditions
        MultipartFile imageFile = mock(MultipartFile.class);
        when(imageFile.isEmpty()).thenReturn(true);
        Images image = null;

        //Exercise
        try {
            image = imageService.create(imageFile);
        } catch (Exception e) {
            fail("Unexpected error during operation create image: " + e.getMessage());
        }

        //Postconditions
        assertNull(image);
    }

    //@TODO: CHECK IF ITS OK
    @Test
    public void testCreate() {
        //Preconditions
        MultipartFile imageFile = mock(MultipartFile.class);
        byte[] imageBytes = new byte[]{1, 2, 3};
        when(imageFile.isEmpty()).thenReturn(false);
        try {
            when(imageFile.getBytes()).thenReturn(imageBytes);
        } catch (IOException e) {
            fail("Unexpected error during mocking getBytes: " + e.getMessage());
        }
        when(imageDao.create(imageBytes)).thenReturn(new Images(1, imageBytes));
        Images image = null;

        //Exercise
        try {
            image = imageService.create(imageFile);
        } catch (Exception e) {
            fail("Unexpected error during operation create image: " + e.getMessage());
        }

        //Postconditions
        assertNotNull(image);
        assertEquals(1L, image.getId());
        assertArrayEquals(imageBytes, image.getImage());
    }
}
