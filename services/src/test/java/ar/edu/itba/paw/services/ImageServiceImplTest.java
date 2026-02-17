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
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ImageServiceImplTest {

    @Mock
    private ImageDao imageDao;

    @InjectMocks
    private ImageServiceImpl imageService;

    @Test
    public void testCreate() throws IOException {
        //Preconditions
        MultipartFile imageFile = mock(MultipartFile.class);
        byte[] imageBytes = new byte[]{1, 2, 3};
        long doctorId = 1L;
        long expectedImageId = 100L;

        when(imageFile.getBytes()).thenReturn(imageBytes);
        when(imageFile.getOriginalFilename()).thenReturn("profile.jpg");
        when(imageFile.getSize()).thenReturn((long) imageBytes.length);

        Images mockedImage = mock(Images.class);
        when(mockedImage.getId()).thenReturn(expectedImageId);

        when(imageDao.create(imageBytes)).thenReturn(mockedImage);

        //Exercise
        long imageId = imageService.create(imageFile, doctorId);

        //Postconditions
        assertEquals(expectedImageId, imageId);
    }

    @Test
    public void testFindByIdNotExits() {
        //Preconditions
        when(imageDao.findById(1000L)).thenReturn(Optional.empty());

        //Exercise
        Optional<Images> maybeImage = imageService.findById(1000L);

        //Postconditions
        assertFalse(maybeImage.isPresent());
    }

    @Test
    public void testFindByIdExists() {
        //Preconditions
        byte[] imageBytes = new byte[]{1, 2, 3};
        when(imageDao.findById(anyLong())).thenReturn(Optional.of(
                new Images(imageBytes)
        ));

        //Exercise
        Optional<Images> maybeImage = imageService.findById(1L);

        //Postconditions
        assertTrue(maybeImage.isPresent());
        assertArrayEquals(imageBytes, maybeImage.get().getImage());
    }
}