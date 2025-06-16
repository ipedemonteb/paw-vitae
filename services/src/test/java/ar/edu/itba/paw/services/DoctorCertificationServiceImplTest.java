package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorCertificationDao;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DoctorCertificationServiceImplTest {

    @Mock
    private DoctorCertificationDao doctorCertificationDao;
    @Mock
    private DoctorService doctorService;
    @InjectMocks
    private DoctorCertificationServiceImpl doctorCertificationService;

    //@TODO: Should i test update? how??

    @Test
    public void testCreateInvalidDoctor() {
        //Preconditions
        when(doctorService.getById(anyLong())).thenReturn(Optional.empty());

        //Exercise & Postconditions
        assertThrows(UserNotFoundException.class, () -> {
            doctorCertificationService.create(1L, "Certificate", "Entity", LocalDate.of(2020, 1, 1));
        });
    }

    @Test
    public void testFindByDoctorIdNotExists() {
        //Preconditions

        //Exercise & Postconditions
        assertThrows(UserNotFoundException.class, () -> {
            doctorCertificationService.findByDoctorId(1L);
        });
    }

}
