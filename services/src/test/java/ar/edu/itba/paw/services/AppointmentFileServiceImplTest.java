package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AppointmentFileDao;
import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.models.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AppointmentFileServiceImplTest {

    @Mock
    private AppointmentFileDao appointmentFileDao;

    @Mock
    private AppointmentService appointmentService;

    @InjectMocks
    private AppointmentFileServiceImpl appointmentFileService;


    @Test
    public void testCreateAppointmentFile() throws IOException {
        //Preconditions
        MultipartFile file = mock(MultipartFile.class);
        byte[] data = new byte[]{1, 2, 3};
        AppointmentFile expectedFile = mock(AppointmentFile.class);
        List<AppointmentFile> result = new LinkedList<>();
        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("file1.txt");
        when(file.getBytes()).thenReturn(data);
        when(appointmentFileDao.create("file1.txt", data, "uploader_role", 1L)).thenReturn(expectedFile);
        MultipartFile[] files = new MultipartFile[]{file};

        //Exercise
        try {
            result = appointmentFileService.create(files, "uploader_role", 1L);
        } catch (Exception e) {
            fail("Unexpected error during creation of image: " + e.getMessage());
        }

        //Postconditions
        assertFalse(result.isEmpty());
        assertEquals(expectedFile, result.getFirst());
    }

    @Test
    public void testGetAuthorizedFile() {
        //Preconditions
        long fileId = 1L;
        long appointmentId = 2L;
        String username = "doctor@example.com";
        AppointmentFile mockFile = mock(AppointmentFile.class);
        when(mockFile.getAppointment_id()).thenReturn(appointmentId);
        Appointment mockAppointment = mock(Appointment.class);
        Doctor mockDoctor = mock(Doctor.class);
        when(mockDoctor.getEmail()).thenReturn(username);
        when(mockAppointment.getDoctor()).thenReturn(mockDoctor);
        when(appointmentFileDao.getById(fileId)).thenReturn(Optional.of(mockFile));
        when(appointmentService.getById(appointmentId)).thenReturn(Optional.of(mockAppointment));

        //Exercise
        Optional<AppointmentFile> result = appointmentFileService.getAuthorizedFile(fileId, appointmentId, username);

        //Postconditions
        assertTrue(result.isPresent());
        assertEquals(mockFile, result.get());
    }

    @Test
    public void testGetAuthorizedFileFalse() {
        //Preconditions
        long fileId = 1L;
        long appointmentId = 2L;
        String unauthorizedUsername = "another@example.com";
        AppointmentFile mockFile = mock(AppointmentFile.class);
        when(mockFile.getAppointment_id()).thenReturn(appointmentId);
        Appointment mockAppointment = mock(Appointment.class);
        Doctor mockDoctor = mock(Doctor.class);
        Patient mockPatient = mock(Patient.class);
        when(mockDoctor.getEmail()).thenReturn("doctor@example.com");
        when(mockPatient.getEmail()).thenReturn("patient@example.com");
        when(mockAppointment.getDoctor()).thenReturn(mockDoctor);
        when(mockAppointment.getPatient()).thenReturn(mockPatient);
        when(appointmentFileDao.getById(fileId)).thenReturn(Optional.of(mockFile));
        when(appointmentService.getById(appointmentId)).thenReturn(Optional.of(mockAppointment));

        //Exercise
        Optional<AppointmentFile> result = appointmentFileService.getAuthorizedFile(fileId, appointmentId, unauthorizedUsername);

        //Postconditions
        assertTrue(result.isEmpty());
    }
}
