package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentFile;
import ar.edu.itba.paw.models.Page;

import java.util.List;
import java.util.Optional;

public interface AppointmentFileDao {

    AppointmentFile create(String fileName, byte[] fileData, String uploader_role, Appointment appointment);
    List<AppointmentFile> getByAppointmentIdForDoctor(long appointment_id);
    Optional<AppointmentFile> getById(long id);

    List<AppointmentFile> getByAppointmentId(long appointment_id);

    List<AppointmentFile> getAllFilesForPatient(long patientId, int pageNumber, int pageSize);

    int getAllFilesForPatientCount(long patientId);

    List<AppointmentFile> getFilesByAppointmentIds(List<Long> appointmentIds);

}
