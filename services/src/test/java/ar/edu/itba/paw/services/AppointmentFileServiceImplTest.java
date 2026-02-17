package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AppointmentFileDao;
import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.MailService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.AppointmentNotFoundException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentFileServiceImplTest {

    private static final String UPLOADER = "doctor";
    private static final long APPOINTMENT_ID = 1L;
    private static final long FILE_ID = 1L;
    private static final String USERNAME = "test@test.com";
    private static final Neighborhood NEIGHBORHOOD = new Neighborhood(1L, "Neighborhood A");
    private static final Specialty SPECIALTY = new Specialty(1L, "Cardiology");
    private static final Doctor DOCTOR = new Doctor("Jane", "Smith", "jane@test.com", "hashedpassword", "987654321", "es",
            4.5, 1, true);
    private static final DoctorOffice DOCTOR_OFFICE = new DoctorOffice(DOCTOR, NEIGHBORHOOD, List.of(SPECIALTY), "Office A");

    private static final Patient PATIENT = new Patient("John", "Doe", "john@test.com", "hashedpassword", "123456789", "en",
            new Coverage(1L, "Coverage A"), NEIGHBORHOOD, true);
    private static final Appointment APPOINTMENT = new Appointment(LocalDateTime.now(ZoneId.systemDefault()), "Confirmed", "Consultation",
            new Specialty(3L, "Cardiology"), DOCTOR, PATIENT,"Report", DOCTOR_OFFICE, true
    );
    private static final AppointmentFile APPOINTMENT_FILE = new AppointmentFile(
            "testFile",
            new byte[]{1, 2, 3},
            "doctor",
            APPOINTMENT
    );

    @Mock
    private AppointmentFileDao appointmentFileDao;
    @Mock
    private AppointmentService appointmentService;
    @Mock
    private MailService mailService;

    @InjectMocks
    private AppointmentFileServiceImpl appointmentFileService;

    @Test
    public void testCreateAppointmentWithNullFile() {
        //Preconditions

        //Exercise & Postconditions
        assertThrows(IllegalArgumentException.class,
                () -> appointmentFileService.create(null, UPLOADER, APPOINTMENT_ID));
    }

    @Test
    public void testCreateAppointmentWithEmptyFile() {
        //Preconditions
        MultipartFile emptyFile = mock(MultipartFile.class);
        when(emptyFile.isEmpty()).thenReturn(true);

        //Exercise & Postconditions
        assertThrows(IllegalArgumentException.class,
                () -> appointmentFileService.create(emptyFile, UPLOADER, APPOINTMENT_ID));
    }

    @Test
    public void testCreateAppointmentWithInvalidAppointmentId() {
        //Preconditions
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);

        //Exercise & Postconditions
        assertThrows(AppointmentNotFoundException.class,
                () -> appointmentFileService.create(file, UPLOADER, APPOINTMENT_ID));
    }

    @Test
    public void testCreateAppointment() throws IOException {
        //Preconditions
        MultipartFile file = mock(MultipartFile.class);
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("test.pdf");
        when(file.getBytes()).thenReturn(new byte[]{1, 2});
        when(appointmentService.getById(anyLong())).thenReturn(Optional.of(APPOINTMENT));
        when(appointmentFileDao.create(anyString(), any(), anyString(), any())).thenReturn(APPOINTMENT_FILE);

        //Exercise
        AppointmentFile created = appointmentFileService.create(file, UPLOADER, APPOINTMENT_ID);

        //Postconditions
        assertNotNull(created);
        assertEquals(APPOINTMENT_FILE, created);
    }

    @Test
    public void testGetAuthorizedFileEmpty() {
        //Preconditions

        //Exercise
        Optional<AppointmentFile> file = appointmentFileService.getAuthorizedFile(FILE_ID, APPOINTMENT_ID, USERNAME);

        //Postconditions
        assertFalse(file.isPresent());
    }

    @Test
    public void testGetAuthorizedFileNonMatchingAppointmentId() {
        //Preconditions
        AppointmentFile appointmentFileOtherId = new AppointmentFile(
                "testFile",
                new byte[]{1, 2, 3},
                "doctor",
                APPOINTMENT
        );
        when(appointmentFileDao.getById(FILE_ID)).thenReturn(Optional.of(appointmentFileOtherId));

        //Exercise
        Optional<AppointmentFile> file = appointmentFileService.getAuthorizedFile(FILE_ID, APPOINTMENT_ID, USERNAME);

        //Postconditions
        assertFalse(file.isPresent());
    }

    @Test
    public void testGetAuthorizedFileEmptyAppointment() {
        //Preconditions
        when(appointmentFileDao.getById(FILE_ID)).thenReturn(Optional.of(APPOINTMENT_FILE));

        //Exercise
        Optional<AppointmentFile> file = appointmentFileService.getAuthorizedFile(FILE_ID, APPOINTMENT_ID, USERNAME);

        //Postconditions
        assertFalse(file.isPresent());
    }

    @Test
    public void testGetAuthorizedFileNonMatchingUser() {
        //Preconditions
        when(appointmentFileDao.getById(FILE_ID)).thenReturn(Optional.of(APPOINTMENT_FILE));

        //Exercise
        Optional<AppointmentFile> file = appointmentFileService.getAuthorizedFile(FILE_ID, APPOINTMENT_ID, USERNAME);

        //Postconditions
        assertFalse(file.isPresent());
    }

    @Test
    public void testGetAuthorizedFile() {
        //Preconditions
        when(appointmentFileDao.getById(anyLong())).thenReturn(Optional.of(APPOINTMENT_FILE));
        when(appointmentService.getById(anyLong())).thenReturn(Optional.of(APPOINTMENT));

        //Exercise
        Optional<AppointmentFile> file = appointmentFileService.getAuthorizedFile(0L, 0L, "john@test.com");

        //Postconditions
        assertTrue(file.isPresent());
        assertEquals(0L, file.get().getId());
        assertEquals(0L, file.get().getAppointment().getId());
    }


}
