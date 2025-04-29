package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.AppointmentFile;

import java.util.List;
import java.util.Optional;

public interface AppointmentFileDao {
    AppointmentFile create(String fileName, byte[] fileData, String uploader_role, long appointment_id);
    Optional<AppointmentFile> getById(long id);
     List<AppointmentFile> getByAppointmentId(long appointment_id);
}
