package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.interfacePersistence.PatientDao;
import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.*;

@Repository
public class AppointmentDaoImpl implements AppointmentDao {

    private static final String BASE_SQL = "SELECT a.id, a.date, a.status, a.reason, a.report, s.id AS specialty_id, s.key AS specialty_key, d.doctor_id, d.rating AS rating, d.rating_count AS rating_count,d.image_id AS image_id, u.name AS doctor_name, u.last_name AS doctor_last_name, u.email AS doctor_email, u.password AS doctor_password, u.phone AS doctor_phone, u.language AS doctor_language, p.client_id AS patient_id, pu.name AS patient_name, pu.last_name AS patient_last_name, pu.email AS patient_email, pu.password AS patient_password, pu.phone AS patient_phone, pu.language AS patient_language, c.id AS coverage_id, c.coverage_name, pu.is_verified AS patient_verified, u.is_verified AS doctor_verified FROM Appointments a JOIN Specialties s ON a.specialty_id = s.id JOIN Doctors d ON a.doctor_id = d.doctor_id JOIN Users u ON d.doctor_id = u.id JOIN Clients p ON a.client_id = p.client_id JOIN Users pu ON p.client_id = pu.id LEFT JOIN Coverages c ON p.coverage_id = c.id ";

    private static final String ORDER_BY_DATE_ASC = "ORDER BY a.date ASC ";
    private static final String ORDER_BY_DATE_DESC = "ORDER BY a.date DESC ";
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private static RowMapper<Appointment> APPOINTMENT_ROW_MAPPER;
    private final DoctorDao doctorDao;
    private final PatientDao patientDao;

    @Autowired
    public AppointmentDaoImpl(final DataSource dataSource, DoctorDao doctorDao, PatientDao patientDao) {
        this.doctorDao = doctorDao;
        this.patientDao = patientDao;
        APPOINTMENT_ROW_MAPPER = DaoRowMappers.APPOINTMENT_ROW_MAPPER;
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("appointments")
                .usingColumns("client_id", "doctor_id", "date", "reason", "specialty_id", "report")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Appointment create(long patientId, long doctorId, LocalDateTime startDate, String reason, Specialty specialty) {
        final Map<String, Object> args = new HashMap<>();
        args.put("client_id", patientId);
        args.put("doctor_id", doctorId);
        args.put("date", java.sql.Timestamp.valueOf(startDate));
        args.put("reason", reason);
        args.put("specialty_id", specialty.getId());
        args.put("report", "");
        final Number appointmentId = jdbcInsert.executeAndReturnKey(args);
        Doctor doctor = doctorDao.getById(doctorId).orElse(null);
        Patient patient = patientDao.getById(patientId).orElse(null);

        return new Appointment(
                startDate,
                AppointmentStatus.CONFIRMADO.getValue(),
                reason,
                appointmentId.longValue(),
                specialty,
                doctor,
                patient,
                ""
        );
    }

    @Override
    public void cancelAppointment(long appointmentId) {
        jdbcTemplate.update("UPDATE Appointments SET status = ? WHERE id = ?", AppointmentStatus.CANCELADO.getValue(), appointmentId);
    }

    @Override
    public void completeAppointments() {
        LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")).withMinute(5).withSecond(0).withNano(0);
        LocalDateTime fiveDaysAgo = now.minusDays(5);
        String sql = "UPDATE Appointments SET status = ? WHERE Appointments.date BETWEEN ? AND ? AND status = ?";
        jdbcTemplate.update(sql, AppointmentStatus.COMPLETO.getValue(), fiveDaysAgo, now, AppointmentStatus.CONFIRMADO.getValue());
    }

    @Override
    public Optional<Appointment> getById(long appointmentId) {
        StringBuilder sql = new StringBuilder(BASE_SQL).append("WHERE a.id = ?");
        return jdbcTemplate.query(sql.toString(), APPOINTMENT_ROW_MAPPER, appointmentId).stream().findFirst();
    }

    @Override
    public List<Appointment> getAppointments(long userId, boolean isFuture, int page, int size, String filter) {
        String condition = isFuture ? buildDateRangeCondition(filter) : buildStatusCondition(filter);
        String order = isFuture ? ORDER_BY_DATE_ASC : ORDER_BY_DATE_DESC;
        String sql = BASE_SQL + condition + order + "LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, APPOINTMENT_ROW_MAPPER, userId, userId, size, (page - 1) * size);
    }

    @Override
    public List<Appointment> getAppointmentsByDate(LocalDate today) {
        StringBuilder sql = new StringBuilder(BASE_SQL).append("WHERE CAST(a.date AS DATE) = ? ").append(ORDER_BY_DATE_ASC);
        return jdbcTemplate.query(sql.toString(), APPOINTMENT_ROW_MAPPER, today);
    }

    @Override
    public int countAppointments(long userId, boolean isFuture, String filter) {
        String sql = "SELECT COUNT(*) FROM Appointments a ";
        sql += isFuture ? buildDateRangeCondition(filter) : buildStatusCondition(filter);
        return jdbcTemplate.queryForObject(sql, Integer.class, userId, userId);
    }

    @Override
    public void updateReport(long appointmentId, String report) {
        jdbcTemplate.update("UPDATE Appointments SET report = ? WHERE id = ?", report, appointmentId);
    }

    private String buildDateRangeCondition(String dateRange) {

        return switch (dateRange) {
            case "today" ->
                    "WHERE (a.doctor_id = ? OR a.client_id = ?) AND DATE(a.date) = CURRENT_DATE AND a.status <> 'cancelado' AND a.date > NOW() ";
            case "week" ->
                    "WHERE (a.doctor_id = ? OR a.client_id = ?) AND a.date BETWEEN NOW() AND NOW() + INTERVAL '7 DAYS' AND a.status <> 'cancelado' ";
            case "month" ->
                   "WHERE (a.doctor_id = ? OR a.client_id = ?) AND a.date BETWEEN NOW() AND NOW() + INTERVAL '1 MONTH' AND a.status <> 'cancelado' ";
            default ->
                    "WHERE (a.doctor_id = ? OR a.client_id = ?) AND a.date > NOW() AND a.status <> 'cancelado' ";
        };
    }

    private String buildStatusCondition(String status) {
        return switch (status) {
            case "completed" ->
                    "WHERE (a.doctor_id = ? OR a.client_id = ?) AND a.status = 'completo' AND a.date < NOW() ";
            case "cancelled" ->
                    "WHERE (a.doctor_id = ? OR a.client_id = ?) AND a.status = 'cancelado' ";
            default -> "WHERE (a.doctor_id = ? OR a.client_id = ?) AND (a.date < NOW() OR a.status = 'cancelado')";

        };
    }

    @Override
    public List<Appointment> getAppointmentsByUserAndDate(long userId, LocalDate date, Integer time) {
        StringBuilder sql = new StringBuilder(BASE_SQL)
                .append("WHERE (a.doctor_id = ? OR a.client_id = ?) ")
                .append("AND CAST(a.date AS DATE) = ? ")
                .append("AND CASt(a.date AS TIME) = ? ")
                .append(ORDER_BY_DATE_ASC);

        return jdbcTemplate.query(sql.toString(), APPOINTMENT_ROW_MAPPER, userId, userId, Date.valueOf(date), Time.valueOf(LocalTime.of(time, 0)));
    }

    @Override
    public List<Appointment> getFutureAppointmentsByUser(long userId) {
        StringBuilder sql = new StringBuilder(BASE_SQL)
                .append("WHERE (a.doctor_id = ? OR a.client_id = ?) ")
                .append("AND a.date > NOW() ")
                .append("AND a.status <> 'cancelado' ")
                .append(ORDER_BY_DATE_ASC);

        return jdbcTemplate.query(sql.toString(), APPOINTMENT_ROW_MAPPER, userId, userId);
    }
}