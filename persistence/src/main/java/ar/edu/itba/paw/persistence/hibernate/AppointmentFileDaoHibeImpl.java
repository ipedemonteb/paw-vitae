package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.AppointmentFileDao;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentFile;
import ar.edu.itba.paw.models.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class AppointmentFileDaoHibeImpl implements AppointmentFileDao {
    @PersistenceContext
    private EntityManager em;

    @Override
    public AppointmentFile create(String fileName, byte[] fileData, String uploader_role, Appointment appointment) {
        final AppointmentFile appointmentFile = new AppointmentFile(fileName, fileData, uploader_role, appointment);
           em.persist(appointmentFile);
        return appointmentFile;
    }

    @Override
    public List<AppointmentFile> getByAppointmentIdForDoctor(long appointment_id) {
        return em.createQuery("FROM AppointmentFile af WHERE af.appointment.id = :appointment_id AND af.uploaderRole = 'doctor'", AppointmentFile.class)
                .setParameter("appointment_id", appointment_id)
                .getResultList();
    }

    @Override
    public Optional<AppointmentFile> getById(long id) {
        return Optional.ofNullable(em.find(AppointmentFile.class, id));
    }

    @Override
    public List<AppointmentFile> getByAppointmentId(long appointment_id) {
        return em.createQuery("FROM AppointmentFile af WHERE af.appointment.id = :appointment_id", AppointmentFile.class)
                .setParameter("appointment_id", appointment_id)
                .getResultList();
    }

    @Override
    public List<AppointmentFile> getAllFilesForPatient(long patientId, int pageNumber, int pageSize) {
        int firstResult = (pageNumber - 1) * pageSize;
        return em.createQuery("FROM AppointmentFile af WHERE af.appointment.patient.id = :patientId", AppointmentFile.class)
                .setParameter("patientId", patientId)
                .setFirstResult(firstResult)
                .setMaxResults(pageSize)
                .getResultList();
    }

    @Override
    public List<AppointmentFile> getFilesByAppointmentIds(List<Long> appointmentIds) {
        if (appointmentIds.isEmpty()) {
            return Collections.emptyList();
        }
        return em.createQuery("FROM AppointmentFile af WHERE af.appointment.id IN :appointmentIds", AppointmentFile.class)
                .setParameter("appointmentIds", appointmentIds)
                .getResultList();
    }



    @Override
    public int getAllFilesForPatientCount(long patientId) {
        return ((Number) em.createQuery("SELECT COUNT(af) FROM AppointmentFile af WHERE af.appointment.patient.id = :patientId")
                .setParameter("patientId", patientId)
                .getSingleResult()).intValue();
    }


}
