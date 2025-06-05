package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.AppointmentFileDao;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentFile;
import ar.edu.itba.paw.models.Page;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
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
    public int getAllFilesForPatientCount(long patientId) {
        return ((Number) em.createQuery("SELECT COUNT(af) FROM AppointmentFile af WHERE af.appointment.patient.id = :patientId")
                .setParameter("patientId", patientId)
                .getSingleResult()).intValue();
    }


}
