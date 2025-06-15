package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.models.*;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class AppointmentDaoHibeImpl implements AppointmentDao {

    @PersistenceContext
    private EntityManager em;

    LocalDateTime now = LocalDateTime.now().withMinute(5).withSecond(0).withNano(0);

    @Override
    public Appointment create(LocalDateTime date, String status, String reason, Specialty specialty, Doctor doctor, Patient patient, String report, DoctorOffice doctorOffice, boolean allowFullHistory) {
        Appointment appointment = new Appointment(date, status, reason, specialty, doctor, patient, report, doctorOffice, allowFullHistory);
        em.persist(appointment);
        return appointment;
    }

    @Override
    public List<Appointment> getPastConfirmedAppointments() {
        LocalDateTime fiveDaysAgo = now.minusDays(5);
        TypedQuery<Appointment> query = em.createQuery("FROM Appointment a WHERE a.status = 'confirmado' AND a.date BETWEEN :start AND :end", Appointment.class);
        query.setParameter("start", fiveDaysAgo);
        query.setParameter("end", now);
        return query.getResultList();
    }

    @Override
    public Optional<Appointment> getById(long appointmentId) {
        return Optional.ofNullable(em.find(Appointment.class, appointmentId));
    }

    @Override
    public List<Appointment> getAppointments(long userId, boolean isFuture, int page, int size, String filter) {
        Query nativeQuery = getNativeQuery(userId, isFuture, filter, false);
        nativeQuery.setFirstResult((page-1) * size);
        nativeQuery.setMaxResults(size);

        List<?> appointmentIdsRaw = nativeQuery.getResultList();
        List<Long> appointmentIds = appointmentIdsRaw.stream().map(id -> ((Number) id).longValue()).collect(Collectors.toList());
        TypedQuery<Appointment> query = em.createQuery("FROM Appointment a WHERE a.id IN (:appointmentIds) order by a.date asc", Appointment.class);
        query.setParameter("appointmentIds", appointmentIds);

        return query.getResultList();
    }

    @Override
    public List<Appointment> getAppointmentsByUserAndDate(long userId, LocalDate date, Integer time) {
        LocalDateTime targetDateTime = LocalDateTime.of(date, LocalTime.of(time, 0));
        String queryStr = "SELECT a FROM Appointment a WHERE (a.doctor.id = :userId OR a.patient.id = :userId) AND a.date = :datetime";
        TypedQuery<Appointment> query = em.createQuery(queryStr, Appointment.class)
                .setParameter("userId", userId)
                .setParameter("datetime", targetDateTime);
        return query.getResultList();
    }

    @Override
    public List<Appointment> getAppointmentsByDate(LocalDate today) {
        LocalDateTime startOfDay = today.atStartOfDay();
        LocalDateTime endOfDay = today.atTime(LocalTime.MAX);
        TypedQuery<Appointment> query = em.createQuery(
                "SELECT a FROM Appointment a WHERE a.date >= :startOfDay AND a.date <= :endOfDay", Appointment.class);
        query.setParameter("startOfDay", startOfDay);
        query.setParameter("endOfDay", endOfDay);
        return query.getResultList();
    }

    @Override
    public List<Appointment> getFutureAppointmentsByUser(long userId) {
        TypedQuery<Appointment> query = em.createQuery("SELECT a FROM Appointment a WHERE (a.doctor.id = :userId OR a.patient.id = :userId) AND a.date > :now", Appointment.class);
        query.setParameter("userId", userId);
        query.setParameter("now", now);
        return query.getResultList();
    }

    @Override
    public int countAppointments(long userId, boolean isFuture, String filter) {
        Query nativeQuery = getNativeQuery(userId, isFuture, filter, true);
        return ((Number) nativeQuery.getSingleResult()).intValue();
    }

    @Override
    public List<Appointment> getAppointmentsByPatient(long patientId, int page, int size) {
        int firstResult = (page - 1) * size;
        return em.createQuery("FROM Appointment a WHERE a.patient.id = :patientId ORDER BY a.date ASC", Appointment.class)
                .setParameter("patientId", patientId)
                .setFirstResult(firstResult)
                .setMaxResults(size)
                .getResultList();
    }

    public List<Appointment> getAppointmentsByPatientWithFilesOrReport(long patientId, int page, int size) {
        int firstResult = (page - 1) * size;
        return em.createQuery(
                        "SELECT DISTINCT a FROM Appointment a " +
                                "LEFT JOIN a.appointmentFiles af " +
                                "WHERE a.patient.id = :patientId " +
                                "AND (af.id IS NOT NULL OR (a.report IS NOT NULL AND a.report <> '')) " +
                                "ORDER BY a.date DESC",
                        Appointment.class)
                .setParameter("patientId", patientId)
                .setFirstResult(firstResult)
                .setMaxResults(size)
                .getResultList();
    }

    public int countAppointmentsByPatientWithFilesOrReport(long patientId) {
        return ((Number) em.createQuery(
                        "SELECT COUNT(DISTINCT a) FROM Appointment a " +
                                "LEFT JOIN a.appointmentFiles af " +
                                "WHERE a.patient.id = :patientId " +
                                "AND (af.id IS NOT NULL OR (a.report IS NOT NULL AND a.report <> ''))")
                .setParameter("patientId", patientId)
                .getSingleResult()).intValue();
    }

    @Override
    public int countAppointmentsByPatient(long patientId) {
        return ((Number) em.createQuery("SELECT COUNT(a) FROM Appointment a WHERE a.patient.id = :patientId")
                .setParameter("patientId", patientId)
                .getSingleResult()).intValue();
    }

    @Override
    public boolean hasFullMedicalHistoryEnabled(long patientId, long doctorId) {
        TypedQuery<Appointment> query = em.createQuery(
                "SELECT a FROM Appointment a WHERE a.patient.id = :patientId AND a.doctor.id = :doctorId AND a.allowFullHistory = true",
                Appointment.class);
        query.setParameter("patientId", patientId);
        query.setParameter("doctorId", doctorId);
        return !query.getResultList().isEmpty();
    }

    //TODO: CHECK
    private static StringBuilder getSql(boolean isFuture, String filter, boolean isCount) {
        StringBuilder sql = new StringBuilder();
        if (isCount) {
            sql.append("SELECT COUNT(DISTINCT a.id) ");
        } else {
            sql.append("SELECT a.id FROM ( SELECT a.id, a.date ");
        }
        sql.append("FROM Appointments a WHERE (a.doctor_id = :userId OR a.patient_id = :userId) ");

        if (isFuture) {
            if (filter != null && !filter.isEmpty()) {
                switch (filter) {
                    case "today":
                        sql.append("AND DATE(a.date) = CURRENT_DATE AND a.status <> 'cancelado' AND a.date > NOW() ");
                        break;
                    case "week":
                        sql.append("AND a.date BETWEEN NOW() AND NOW() + INTERVAL '7 DAYS' AND a.status <> 'cancelado' ");
                        break;
                    case "month":
                        sql.append("AND a.date BETWEEN NOW() AND NOW() + INTERVAL '1 MONTH' AND a.status <> 'cancelado' ");
                        break;
                    default:
                        sql.append("AND a.date > NOW() AND a.status <> 'cancelado' ");
                        break;
                }
            } else {
                sql.append("AND a.date > NOW() AND a.status <> 'cancelado' ");
            }
        } else {
            switch (filter) {
                case "completed":
                    sql.append("AND a.status = 'completo' AND a.date < NOW() ");
                    break;
                case "cancelled":
                    sql.append("AND a.status = 'cancelado' ");
                    break;
                default:
                    sql.append("AND (a.date < NOW() OR a.status = 'cancelado') ");
                    break;
            }
        }
        if (!isCount) {
            sql.append("ORDER BY a.date ASC ) AS a ");
        }

        return sql;
    }

    private Query getNativeQuery(long userId, boolean isFuture, String filter, boolean isCount) {
        StringBuilder sql = getSql(isFuture, filter, isCount);
        Query nativeQuery = em.createNativeQuery(sql.toString());
        nativeQuery.setParameter("userId", userId);
        return nativeQuery;
    }

    //make it look between a range back
    @Override
    public List<Appointment> getAppointmentsWithHistoryAllowedBefore(LocalDateTime dateTime) {
        TypedQuery<Appointment> query = em.createQuery(
                "SELECT a FROM Appointment a WHERE a.allowFullHistory = true AND a.date < :dateTime",
                Appointment.class
        );
        query.setParameter("dateTime", dateTime);
        return query.getResultList();
    }



    // -------------------------------------
    //  DEPRECATED METHODS
    // -------------------------------------

    //DEPRECATED
    @Override
    public void cancelAppointment(long appointmentId) {}

    //DEPRECATED
    @Override
    public void completeAppointments() {}

    //DEPRECATED
    @Override
    public void updateReport(long appointmentId, String report) {}

}

