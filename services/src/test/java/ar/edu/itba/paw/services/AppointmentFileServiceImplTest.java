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
            1L, 4.5, 10, true);
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
        when(appointmentService.getById(anyLong())).thenReturn(Optional.of(APPOINTMENT));
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
        when(appointmentService.getById(anyLong())).thenReturn(Optional.of(APPOINTMENT));
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

        //Exercise & Postconditions
        assertThrows(AppointmentNotFoundException.class, () ->
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
                "doctor",
                APPOINTMENT
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

    @Test
    public void testGetGroupedFilesForPatient() {
        //Preconditions
        long patientId = 1L;
        int page = 0, pageSize = 2;
        String direction = "desc";
        Appointment appointment1 = mock(Appointment.class);
        when(appointment1.getId()).thenReturn(1L);
        Appointment appointment2 = mock(Appointment.class);
        when(appointment2.getId()).thenReturn(2L);
        List<Appointment> appointments = List.of(appointment1, appointment2);
        Page<Appointment> appointmentPage = new Page<>(appointments, page, pageSize, 2);
        AppointmentFile file1 = new AppointmentFile("file1", new byte[]{1}, "doctor", appointment1);
        AppointmentFile file2 = new AppointmentFile("file2", new byte[]{2}, "doctor", appointment2);
        when(appointmentService.getAppointmentsForPatientWithFilesOrReport(patientId, page, pageSize, direction))
                .thenReturn(appointmentPage);
        when(appointmentFileDao.getFilesByAppointmentIds(anyList()))
                .thenReturn(List.of(file1, file2));

        //Exercise
        Page<Map.Entry<Appointment, List<AppointmentFile>>> result = appointmentFileService.getGroupedFilesForPatient(patientId, page, pageSize, direction);

        //Postconditions
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertEquals(appointment1.getId(), result.getContent().getFirst().getKey().getId());
        assertEquals(appointment2.getId(), result.getContent().getLast().getKey().getId());
        assertArrayEquals(file1.getFileData(), result.getContent().getFirst().getValue().getFirst().getFileData());
        assertArrayEquals(file2.getFileData(), result.getContent().getLast().getValue().getFirst().getFileData());
    }
}
