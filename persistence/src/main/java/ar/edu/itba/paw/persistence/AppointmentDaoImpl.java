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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class AppointmentDaoImpl implements AppointmentDao {

    private static final String BASE_SQL = "SELECT a.id, a.date, a.status, a.reason, " +
            "s.id AS specialty_id, s.key AS specialty_key, " +
            "d.doctor_id, d.rating AS rating, d.rating_count AS rating_count, u.name AS doctor_name, u.last_name AS doctor_last_name, u.email AS doctor_email, " +
            "u.password AS doctor_password, u.phone AS doctor_phone, u.language AS doctor_language, " +
            "p.client_id AS patient_id, pu.name AS patient_name, pu.last_name AS patient_last_name, pu.email AS patient_email, " +
            "pu.password AS patient_password, pu.phone AS patient_phone, pu.language AS patient_language, " +
            "c.id AS coverage_id, c.coverage_name " +
            "FROM Appointments a " +
            "JOIN Specialties s ON a.specialty_id = s.id " +
            "JOIN Doctors d ON a.doctor_id = d.doctor_id " +
            "JOIN Users u ON d.doctor_id = u.id " +
            "JOIN Clients p ON a.client_id = p.client_id " +
            "JOIN Users pu ON p.client_id = pu.id " +
            "LEFT JOIN Coverages c ON p.coverage_id = c.id ";

    private static final String ORDER_BY_DATE_ASC = "ORDER BY a.date ASC ";
    private static final String ORDER_BY_DATE_DESC = "ORDER BY a.date DESC ";
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private RowMapper<Doctor> DOCTOR_ROW_MAPPER;
    private RowMapper<Patient> PATIENT_ROW_MAPPER;
    private final RowMapper<Appointment> ROW_MAPPER = (rs, rowNum) -> new Appointment(
            rs.getTimestamp("date").toLocalDateTime(),
            rs.getString("status"),
            rs.getString("reason"),
            rs.getLong("id"),
            new Specialty(rs.getLong("specialty_id"), rs.getString("specialty_key")),
            DOCTOR_ROW_MAPPER.mapRow(rs, 1),
            PATIENT_ROW_MAPPER.mapRow(rs, 1)
    );
    private DoctorDao doctorDao;
    private PatientDao patientDao;

    @Autowired
    public AppointmentDaoImpl(final DataSource ds, DoctorDao doctorDao, PatientDao patientDao, DaoUtils daoUtils) {
        this.doctorDao = doctorDao;
        this.patientDao = patientDao;
        DOCTOR_ROW_MAPPER = daoUtils.getDoctorRowMapper();
        PATIENT_ROW_MAPPER = daoUtils.getPatientRowMapper();
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("appointments")
                .usingColumns("client_id", "doctor_id", "date", "reason", "specialty_id")
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
                patient
        );
    }

    @Override
    public List<Appointment> getByPatientId(long patientId) {
        String sql = BASE_SQL +
                "WHERE a.client_id = ? " +
                ORDER_BY_DATE_ASC;

        return jdbcTemplate.query(sql, ROW_MAPPER, patientId);
    }

    @Override
    public List<Appointment> getByDoctorId(long doctorId) {
        LocalDate today = LocalDate.now();
        LocalDate endOfNextMonth = today.plusMonths(1).withDayOfMonth(today.plusMonths(1).lengthOfMonth()).plusDays(1);
        String sql = BASE_SQL +
                "WHERE a.doctor_id = ? " +
                "AND a.status != 'cancelado' " +
                "AND a.date BETWEEN ? AND ? " +
                ORDER_BY_DATE_ASC;

        return jdbcTemplate.query(sql, ROW_MAPPER, doctorId, today, endOfNextMonth);
    }


    @Override
    public void cancelAppointment(long appointmentId) {
        jdbcTemplate.update("UPDATE Appointments SET status = ? WHERE id = ?", AppointmentStatus.CANCELADO.getValue(), appointmentId);
    }

    @Override
    public void acceptAppointment(long appointmentId) {
        jdbcTemplate.update("UPDATE Appointments SET status = ? WHERE id = ?", AppointmentStatus.CONFIRMADO.getValue(), appointmentId);
    }


    @Override
    public Optional<Appointment> getById(long appointmentId) {
        String sql = BASE_SQL +
                "WHERE a.id = ?";

        return jdbcTemplate.query(sql, ROW_MAPPER, appointmentId).stream().findFirst();
    }


    @Override
    public List<Appointment> getPastDoctorAppointments(long doctorId, int page, int size, String dateRange, String status) {
        String sql = BASE_SQL + BuildStatusCondition("a.doctor_id", status) +
                ORDER_BY_DATE_DESC + "LIMIT ? OFFSET ?";

        return jdbcTemplate.query(sql, ROW_MAPPER, doctorId, size, (page - 1) * size);
    }

    @Override
    public List<Appointment> getFutureDoctorAppointments(long doctorId, int page, int size, String dateRange, String status) {
        String sql = BASE_SQL + buildDateRangeCondition("a.doctor_id", dateRange) +
                ORDER_BY_DATE_ASC + "LIMIT ? OFFSET ?";

        return jdbcTemplate.query(sql, ROW_MAPPER, doctorId, size, (page - 1) * size);
    }

    @Override
    public List<Appointment> getFuturePatientAppointments(long patientId, int page, int size, String dateRange, String status) {
        String sql = BASE_SQL + buildDateRangeCondition("a.client_id", dateRange) +
                ORDER_BY_DATE_ASC + "LIMIT ? OFFSET ?";
        return jdbcTemplate.query(sql, ROW_MAPPER, patientId, size, (page - 1) * size);
    }

    @Override
    public List<Appointment> getPastPatientAppointments(long patientId, int page, int size, String dateRange, String status) {
        String sql = BASE_SQL + BuildStatusCondition("a.client_id", status) +
                ORDER_BY_DATE_DESC + "LIMIT ? OFFSET ?";

        return jdbcTemplate.query(sql, ROW_MAPPER, patientId, size, (page - 1) * size);
    }

    @Override
    public List<Appointment> getAppointmentsByDate(LocalDate today) {
        String sql = BASE_SQL +
                "WHERE DATE(a.date) = ? " +
                ORDER_BY_DATE_ASC;

        return jdbcTemplate.query(sql, ROW_MAPPER, today);
    }

    @Override
    public int countFuturePatientAppointments(long patientId, String dateRange) {
        String sql = "SELECT COUNT(*) FROM Appointments a ";
        sql += buildDateRangeCondition("a.client_id", dateRange);
        return jdbcTemplate.queryForObject(sql, Integer.class, patientId);
    }

    @Override
    public int countPastPatientAppointments(long patientId,String status) {
        String sql = "SELECT COUNT(*) FROM Appointments a ";
        sql += BuildStatusCondition("a.client_id", status);
        return jdbcTemplate.queryForObject(sql, Integer.class, patientId);
    }

    @Override
    public int countFutureDoctorAppointments(long doctorId,String dateRange) {
        String sql = "SELECT COUNT(*) FROM Appointments a ";
        sql += buildDateRangeCondition("a.doctor_id", dateRange);
        return jdbcTemplate.queryForObject(sql, Integer.class, doctorId);
    }

    @Override
    public int countPastDoctorAppointments(long doctorId,String status) {
        String sql = "SELECT COUNT(*) FROM Appointments a ";
        sql += BuildStatusCondition("a.doctor_id", status);
        return jdbcTemplate.queryForObject(sql, Integer.class, doctorId);
    }

    private String buildDateRangeCondition(String idColumn, String dateRange) {
        return switch (dateRange) {
            case "today" -> "WHERE " + idColumn + " = ? AND DATE(a.date)  = CURRENT_DATE AND a.status <> 'cancelado' AND a.date > NOW() ";
            case "week" -> "WHERE " + idColumn + " = ? AND a.date BETWEEN NOW() AND NOW() + INTERVAL '7 DAYS' AND a.status <> 'cancelado' ";
            case "month" -> "WHERE " + idColumn + " = ? AND a.date BETWEEN NOW() AND NOW() + INTERVAL '1 MONTH' AND a.status <> 'cancelado' ";
            default -> "WHERE " + idColumn + " = ? AND a.date > NOW() AND a.status <> 'cancelado' ";
        };
    }

    private String BuildStatusCondition(String idColumn, String status) {
        return switch (status) {
            case "completed" -> "WHERE " + idColumn + " = ? AND a.status = 'confirmado' AND a.date < NOW() ";
            case "cancelled" -> "WHERE " + idColumn + " = ? AND a.status = 'cancelado' ";
            case "all" -> "WHERE " + idColumn + " = ? AND a.date < NOW() ";
            default -> "";
            case "confirmed" -> "WHERE " + idColumn + " = ? AND a.status = 'confirmado' AND a.date < NOW() ";
        };
    }



}