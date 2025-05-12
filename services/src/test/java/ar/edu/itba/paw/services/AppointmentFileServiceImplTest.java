package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AppointmentFileDao;
import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.MailService;
import ar.edu.itba.paw.models.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentFileServiceImplTest {

    private static final String UPLOADER = "doctor";
    private static final long APPOINTMENT_ID = 1L;
    private static final long FILE_ID = 1L;
    private static final String USERNAME = "test@test.com";
    private static final AppointmentFile APPOINTMENT_FILE = new AppointmentFile(
            "testFile",
            new byte[]{1, 2, 3},
            FILE_ID,
            "doctor",
            APPOINTMENT_ID
    );
    private static final Appointment APPOINTMENT = new Appointment(
            LocalDateTime.now(),
            "Confirmado",
            "Consulta",
            APPOINTMENT_ID,
            new Specialty(3L, "Cardiology"),
            new Doctor("John", 2L, "Doe", "john@test.com", "hashedpassword", "123456789", "es",
                    1L, true),
            new Patient("Jane", 1L, "Smith", "jane@test.com", "hashedpassword", "987654321", "es",
                    new Coverage(1L, "OSDE"), true),
            "Report"
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
    public void testCreateAppointmentFileEmptyLength() {
        //Preconditions
        MultipartFile[] file = new MultipartFile[0];

        //Exercise
        List<AppointmentFile> appointmentFiles = appointmentFileService.create(file, UPLOADER, APPOINTMENT_ID);

        //Postconditions
        assertNull(appointmentFiles);
    }

    @Test
    public void testCreateAppointmentWithNullFile() {
        //Preconditions
        MultipartFile[] file = new MultipartFile[1];
        file[0] = null;

        //Exercise
        List<AppointmentFile> appointmentFiles = appointmentFileService.create(file, UPLOADER, APPOINTMENT_ID);

        //Postconditions
        assertTrue(appointmentFiles.isEmpty());
    }

    @Test
    public void testCreateAppointmentWithEmptyFile() {
        //Preconditions
        MultipartFile[] file = new MultipartFile[1];
        MultipartFile emptyFile = mock(MultipartFile.class);
        file[0] = emptyFile;
        when(emptyFile.isEmpty()).thenReturn(true);

        //Exercise
        List<AppointmentFile> appointmentFiles = appointmentFileService.create(file, UPLOADER, APPOINTMENT_ID);

        //Postconditions
        assertTrue(appointmentFiles.isEmpty());
    }

    @Test
    public void testCreateAppointmentWithInvalidAppointmentId() {
        //Preconditions
        MultipartFile[] files = new MultipartFile[1];
        MultipartFile file = mock(MultipartFile.class);
        files[0] = file;
        when(file.isEmpty()).thenReturn(false);
        when(appointmentService.getById(anyLong())).thenThrow(new IllegalArgumentException("Appointment not found"));

        //Exercise & Postconditions
        assertThrows(IllegalArgumentException.class, () ->
                appointmentFileService.create(files, UPLOADER, APPOINTMENT_ID)
        );
    }

    @Test
    public void testCreateAppointment() {
        //Preconditions
        MultipartFile[] files = new MultipartFile[1];
        MultipartFile file = mock(MultipartFile.class);
        files[0] = file;
        when(file.isEmpty()).thenReturn(false);
        when(appointmentService.getById(anyLong())).thenReturn(Optional.of(APPOINTMENT));

        //Exercise
        List<AppointmentFile> appointmentFiles = appointmentFileService.create(files, UPLOADER, APPOINTMENT_ID);

        //Postconditions
        assertNotNull(appointmentFiles);
        assertFalse(appointmentFiles.isEmpty());
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
        AppointmentFile appointmentFileOtheId = new AppointmentFile(
                "testFile",
                new byte[]{1, 2, 3},
                FILE_ID,
                "doctor",
                2L
        );
        when(appointmentFileDao.getById(FILE_ID)).thenReturn(Optional.of(appointmentFileOtheId));

        //Exercise
        Optional<AppointmentFile> file = appointmentFileService.getAuthorizedFile(FILE_ID, APPOINTMENT_ID, USERNAME);

        //Postconditions
        assertFalse(file.isPresent());
    }

    @Test
    public void testGetAuthorizedFileEmptyAppointment() {
        //Preconditions
        when(appointmentFileDao.getById(FILE_ID)).thenReturn(Optional.of(APPOINTMENT_FILE));
        when(appointmentService.getById(anyLong())).thenReturn(Optional.empty());

        //Exercise
        Optional<AppointmentFile> file = appointmentFileService.getAuthorizedFile(FILE_ID, APPOINTMENT_ID, USERNAME);

        //Postconditions
        assertFalse(file.isPresent());
    }

    @Test
    public void testGetAuthorizedFileNonMatchingUser() {
        //Preconditions
        when(appointmentFileDao.getById(FILE_ID)).thenReturn(Optional.of(APPOINTMENT_FILE));
        when(appointmentService.getById(anyLong())).thenReturn(Optional.of(APPOINTMENT));

        //Exercise
        Optional<AppointmentFile> file = appointmentFileService.getAuthorizedFile(FILE_ID, APPOINTMENT_ID, USERNAME);

        //Postconditions
        assertFalse(file.isPresent());
    }

    @Test
    public void testGetAuthorizedFile() {
        //Preconditions
        when(appointmentFileDao.getById(FILE_ID)).thenReturn(Optional.of(APPOINTMENT_FILE));
        when(appointmentService.getById(anyLong())).thenReturn(Optional.of(APPOINTMENT));

        //Exercise
        Optional<AppointmentFile> file = appointmentFileService.getAuthorizedFile(FILE_ID, APPOINTMENT_ID, "john@test.com");

        //Postconditions
        assertTrue(file.isPresent());
        assertEquals(FILE_ID, file.get().getId());
        assertEquals(APPOINTMENT_ID, file.get().getAppointment_id());
    }
}
