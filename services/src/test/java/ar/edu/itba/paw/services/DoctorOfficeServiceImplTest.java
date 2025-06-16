package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.interfacePersistence.NeighborhoodDao;
import ar.edu.itba.paw.interfaceServices.DoctorOfficeService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.NeighborhoodService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.DoctorOfficeForm;
import ar.edu.itba.paw.models.exception.NeighborhoodNotFoundException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DoctorOfficeServiceImplTest {

    private static final Doctor DOCTOR = new Doctor("Jane", "Smith", "jane@test.com", "hashedpassword", "987654321", "es",
            1L, 4.5, 10, true);
    private static final DoctorOfficeForm OFFICE_FORM = new DoctorOfficeForm(1L, "Office Name", List.of(1L, 2L), List.of());

    @Mock
    private NeighborhoodDao neighborhoodDao;
    @Mock
    private DoctorDao doctorDao;
    @Mock
    private NeighborhoodService neighborhoodService;
    @Mock
    private DoctorService doctorService;
    @InjectMocks
    private DoctorOfficeServiceImpl doctorOfficeService;

    //@TODO: should i check for more methods?

    @Test
    public void testTransformToDoctorOfficeInvalidNeighborhood() {
        //Preconditions

        //Exercise & Postconditions
        assertThrows(NeighborhoodNotFoundException.class, () -> {
            doctorOfficeService.transformToDoctorOffice(DOCTOR, List.of(OFFICE_FORM));
        });
    }

    @Test
    public void testUpdateInvalidDoctor() {
        //Preconditions

        //Exercise & Postconditions
        assertThrows(UserNotFoundException.class, () -> {
            doctorOfficeService.update(List.of(OFFICE_FORM), DOCTOR);
        });
    }
}
