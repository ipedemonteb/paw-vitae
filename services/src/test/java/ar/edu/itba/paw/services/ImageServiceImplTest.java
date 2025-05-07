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
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImageServiceImplTest {

    @Mock
    private ImageDao imageDao;

    @InjectMocks
    private ImageServiceImpl imageService;

    //@TODO: CHECK EXCEPTION HANDLING
    @Test
    public void testCreateImage() throws IOException {
        //Preconditions
        MultipartFile file = mock(MultipartFile.class);
        byte[] data = new byte[]{1, 2, 3};
        Images expectedImage = mock(Images.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getBytes()).thenReturn(data);
        when(imageDao.create(data)).thenReturn(expectedImage);
        Images result = null;

        //Exercise
        try {
            result = imageService.create(file);
        } catch (Exception e) {
            fail("Unexpected error during creation of image: " + e.getMessage());
        }

        //Postconditions
        assertNotNull(result);
        assertEquals(expectedImage, result);
    }
}
