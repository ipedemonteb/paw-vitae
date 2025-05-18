package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.AppointmentFileDao;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentFile;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class AppointmentFileDaoHibeImpl implements AppointmentFileDao {
    @Override
    public AppointmentFile create(String fileName, byte[] fileData, String uploader_role, Appointment appointment) {
        return null;
    }

    @Override
    public Optional<AppointmentFile> getById(long id) {
        return Optional.empty();
    }

    @Override
    public List<AppointmentFile> getByAppointmentId(long appointment_id) {
        return List.of();
    }
}
