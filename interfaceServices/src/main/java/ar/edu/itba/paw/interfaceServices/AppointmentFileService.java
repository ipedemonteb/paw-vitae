package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.AppointmentFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface AppointmentFileService {

    List<AppointmentFile> create(MultipartFile[] files, String uploader_role, long appointment_id);

    Optional<AppointmentFile> getById(long id);

    List<AppointmentFile> getByAppointmentId(long appointment_id);

    Optional<AppointmentFile> getAuthorizedFile(long fileId, long appointmentId, String username);
}
