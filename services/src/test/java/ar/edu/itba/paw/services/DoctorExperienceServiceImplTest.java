package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorExperienceDao;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DoctorExperienceServiceImplTest {

    @Mock
    private DoctorExperienceDao doctorExperienceDao;
    @Mock
    private DoctorService doctorService;
    @InjectMocks
    private DoctorExperienceServiceImpl doctorExperienceService;

    //@TODO: Should i test update? how??

    @Test
    public void testCreateInvalidUser() {
        //Preconditions
        when(doctorService.getById(anyLong())).thenReturn(Optional.empty());

        //Exercise & Postconditions
        assertThrows(UserNotFoundException.class, () -> {
            doctorExperienceService.create(1L, "Title", "Organization", LocalDate.of(2015, 1, 1), LocalDate.of(2020, 1, 1));
        });
    }

    @Test
    public void testFindByDoctorIdUserNotFound() {
        //Preconditions
        when(doctorExperienceDao.getByDoctorId(anyLong())).thenReturn(List.of());

        //Exercise & Postconditions
        assertThrows(UserNotFoundException.class, () -> {
            doctorExperienceService.findByDoctorId(1L);
        });
    }
}
