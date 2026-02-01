//package ar.edu.itba.paw.services;
//
//import ar.edu.itba.paw.interfacePersistence.DoctorExperienceDao;
//import ar.edu.itba.paw.interfaceServices.DoctorService;
//import ar.edu.itba.paw.models.Doctor;
//import ar.edu.itba.paw.models.DoctorExperience;
//import ar.edu.itba.paw.models.exception.UserNotFoundException;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.Assert.*;
//import static org.mockito.ArgumentMatchers.*;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class DoctorExperienceServiceImplTest {
//
//    private static final Doctor DOCTOR = new Doctor("Jane", "Smith", "jane@test.com", "hashedpassword", "987654321", "es",
//            1L, 4.5, 10, true);
//
//    @Mock
//    private DoctorExperienceDao doctorExperienceDao;
//    @Mock
//    private DoctorService doctorService;
//    @InjectMocks
//    private DoctorExperienceServiceImpl doctorExperienceService;
//
//    @Test
//    public void testCreateInvalidUser() {
//        //Preconditions
//        when(doctorService.getById(anyLong())).thenReturn(Optional.empty());
//
//        //Exercise & Postconditions
//        assertThrows(UserNotFoundException.class, () -> {
//            doctorExperienceService.create(1L, "Title", "Organization", LocalDate.of(2015, 1, 1), LocalDate.of(2020, 1, 1));
//        });
//    }
//
//    @Test
//    public void testCreate() {
//        //Preconditions
//        String title = "Title";
//        String organization = "Organization";
//        LocalDate startDate = LocalDate.of(2015, 1, 1);
//        LocalDate endDate = LocalDate.of(2020, 1, 1);
//        when(doctorService.getById(anyLong())).thenReturn(Optional.of(DOCTOR));
//        when(doctorExperienceDao.create(any(), anyString(), anyString(), any(), any()))
//                .thenReturn(new DoctorExperience(DOCTOR, title, organization, startDate, endDate));
//
//        //Exercise
//        DoctorExperience result = doctorExperienceService.create(1L, title, organization, startDate, endDate);
//
//        //Postconditions
//        assertNotNull(result);
//        assertEquals(DOCTOR.getId(), result.getDoctor().getId());
//        assertEquals(title, result.getPositionTitle());
//        assertEquals(organization, result.getOrganizationName());
//        assertEquals(startDate, result.getStartDate());
//        assertEquals(endDate, result.getEndDate());
//    }
//
//    @Test
//    public void testFindByDoctorIdUserNotFound() {
//        //Preconditions
//        when(doctorExperienceDao.getByDoctorId(anyLong())).thenReturn(List.of());
//
//        //Exercise & Postconditions
//        assertThrows(UserNotFoundException.class, () -> {
//            doctorExperienceService.findByDoctorId(1L);
//        });
//    }
//}
