package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentFile;
import ar.edu.itba.paw.models.Page;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface AppointmentFileService {

    AppointmentFile create(MultipartFile file, String uploader_role, long appointment_id);

    Optional<AppointmentFile> getById(long id);

    List<AppointmentFile> getByAppointmentId(long appointment_id);

    Optional<AppointmentFile> getAuthorizedFile(long fileId, long appointmentId, String username);

    Page<AppointmentFile> getAllFilesForPatient(long patientId, int pageNumber, int pageSize);

    Page<Map.Entry<Appointment, List<AppointmentFile>>> getGroupedFilesForPatient(long patientId, int page, int pageSize, String direction);

    List<AppointmentFile> getByAppointmentIdForDoctor(long appointmentId);
    }
