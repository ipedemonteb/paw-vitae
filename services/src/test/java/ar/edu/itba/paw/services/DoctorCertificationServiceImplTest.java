//package ar.edu.itba.paw.services;
//
//import ar.edu.itba.paw.interfacePersistence.DoctorCertificationDao;
//import ar.edu.itba.paw.interfaceServices.DoctorService;
//import ar.edu.itba.paw.models.Doctor;
//import ar.edu.itba.paw.models.DoctorCertification;
//import ar.edu.itba.paw.models.exception.UserNotFoundException;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.time.LocalDate;
//import java.util.Optional;
//
//import static org.junit.Assert.*;
//import static org.mockito.ArgumentMatchers.anyLong;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public class DoctorCertificationServiceImplTest {
//
//    private static final Doctor DOCTOR = new Doctor("Jane", "Smith", "jane@test.com", "hashedpassword", "987654321", "es",
//            1L, 4.5, 10, true);
//
//    @Mock
//    private DoctorCertificationDao doctorCertificationDao;
//    @Mock
//    private DoctorService doctorService;
//    @InjectMocks
//    private DoctorCertificationServiceImpl doctorCertificationService;
//
//    @Test
//    public void testCreateInvalidDoctor() {
//        //Preconditions
//        when(doctorService.getById(anyLong())).thenReturn(Optional.empty());
//
//        //Exercise & Postconditions
//        assertThrows(UserNotFoundException.class, () -> {
//            doctorCertificationService.create(1L, "Certificate", "Entity", LocalDate.of(2020, 1, 1));
//        });
//    }
//
//    @Test
//    public void testCreate() {
//        //Preconditions
//        String name = "Certificate";
//        String entity = "Entity";
//        LocalDate issueDate = LocalDate.of(2020, 1, 1);
//        when(doctorService.getById(anyLong())).thenReturn(Optional.of(DOCTOR));
//        when(doctorCertificationDao.create(DOCTOR, "Certificate", "Entity", LocalDate.of(2020, 1, 1)))
//                .thenReturn(new DoctorCertification(DOCTOR, name, entity, issueDate));
//
//        //Exercise
//        DoctorCertification result = doctorCertificationService.create(1L, "Certificate", "Entity", issueDate);
//
//        //Postconditions
//        assertNotNull(result);
//        assertEquals(DOCTOR.getId(), result.getDoctor().getId());
//        assertEquals(name, result.getCertificateName());
//        assertEquals(entity, result.getIssuingEntity());
//        assertEquals(issueDate, result.getIssueDate());
//    }
//
//    @Test
//    public void testFindByDoctorIdNotExists() {
//        //Preconditions
//
//        //Exercise & Postconditions
//        assertThrows(UserNotFoundException.class, () -> {
//            doctorCertificationService.findByDoctorId(1L);
//        });
//    }
//
//}
