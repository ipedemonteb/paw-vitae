//package ar.edu.itba.paw.services;
//
//import ar.edu.itba.paw.interfacePersistence.DoctorDao;
//import ar.edu.itba.paw.interfacePersistence.DoctorOfficeDao;
//import ar.edu.itba.paw.interfacePersistence.NeighborhoodDao;
//import ar.edu.itba.paw.interfaceServices.*;
//import ar.edu.itba.paw.models.*;
//import ar.edu.itba.paw.models.exception.NeighborhoodNotFoundException;
//import ar.edu.itba.paw.models.exception.UserNotFoundException;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.Assert.*;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class DoctorOfficeServiceImplTest {
//
//    private static final Neighborhood NEIGHBORHOOD = new Neighborhood(1L, "Neighborhood A");
//    private static final Specialty SPECIALTY = new Specialty(1L, "Specialty A");
//    private static final Doctor DOCTOR = new Doctor("Jane", "Smith", "jane@test.com", "hashedpassword", "987654321", "es",
//            1L, 4.5, 10, true);
//    private static final DoctorOfficeForm OFFICE_FORM = new DoctorOfficeForm(1L, "Office Name", List.of(1L, 2L));
//
//    @Mock
//    private SpecialtyService specialtyService;
//    @Mock
//    private DoctorOfficeDao doctorOfficeDao;
//    @Mock
//    private DoctorOfficeAvailabilityService doctorOfficeAvailabilityService;
//    @Mock
//    private NeighborhoodDao neighborhoodDao;
//    @Mock
//    private DoctorDao doctorDao;
//    @Mock
//    private NeighborhoodService neighborhoodService;
//    @Mock
//    private DoctorService doctorService;
//    @InjectMocks
//    private DoctorOfficeServiceImpl doctorOfficeService;
//
////    @Test
////    public void testTransformToDoctorOfficeInvalidNeighborhood() {
////        //Preconditions
////
////        //Exercise & Postconditions
////        assertThrows(NeighborhoodNotFoundException.class, () -> {
////            doctorOfficeService.transformToDoctorOffice(DOCTOR, OFFICE_FORM);
////        });
////    }
////
////    @Test
////    public void testTransformToDoctorOffice() {
////        //Preconditions
////        when(neighborhoodService.getById(anyLong())).thenReturn(Optional.of(NEIGHBORHOOD));
////        DoctorOffice mockOffice = new DoctorOffice(DOCTOR, NEIGHBORHOOD, List.of(SPECIALTY), "Office A");
////        when(doctorOfficeDao.create(any(DoctorOffice.class))).thenReturn(mockOffice);
////
////        //Exercise
////        List<DoctorOffice> doctorOffices = doctorOfficeService.transformToDoctorOffice(DOCTOR, OFFICE_FORM));
////
////        //Postconditions
////        assertFalse(doctorOffices.isEmpty());
////        assertEquals(1, doctorOffices.size());
////        assertEquals(DOCTOR.getId(), doctorOffices.getFirst().getDoctor().getId());
////    }
////
////    @Test
////    public void testUpdateInvalidDoctor() {
////        //Preconditions
////
////        //Exercise & Postconditions
////        assertThrows(UserNotFoundException.class, () -> {
////            doctorOfficeService.update( List.of(OFFICE_FORM), DOCTOR);
////        });
////    }
//}
