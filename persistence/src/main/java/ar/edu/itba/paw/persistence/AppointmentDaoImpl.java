package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
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

    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert jdbcInsert;

//    private DoctorDaoImpl doctorDaoImpl;
//    private PatientDaoImpl patientDaoImpl;

    private final RowMapper<Appointment> ROW_MAPPER = (rs, rowNum) -> new Appointment(
            rs.getTimestamp("date").toLocalDateTime(),
            rs.getString("status"),
            rs.getString("reason"),
            rs.getLong("id"),
            new Specialty(rs.getLong("specialty_id"), rs.getString("specialty_key")),
            new Doctor(
                    rs.getString("doctor_name"),
                    rs.getLong("doctor_id"),
                    rs.getString("doctor_last_name"),
                    rs.getString("doctor_email"),
                    rs.getString("doctor_password"),
                    rs.getString("doctor_phone"),
                    rs.getString("doctor_language"),
                    rs.getDouble("rating"),
                    rs.getInt("rating_count")
            ),
            new Patient(
                    rs.getString("patient_name"),
                    rs.getLong("patient_id"),
                    rs.getString("patient_last_name"),
                    rs.getString("patient_email"),
                    rs.getString("patient_password"),
                    rs.getString("patient_phone"),
                    rs.getString("patient_language"),
                    new Coverage(rs.getLong("coverage_id"), rs.getString("coverage_name"))
            )
    );

    //todo: change signature
    @Autowired
    public AppointmentDaoImpl(final DataSource ds, final DoctorDaoImpl doctorDaoImpl,final PatientDaoImpl patientDaoImpl) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("appointments")
                .usingColumns("client_id", "doctor_id", "date", "reason", "specialty_id")
                .usingGeneratedKeyColumns("id");
//        this.doctorDaoImpl = doctorDaoImpl;
//        this.patientDaoImpl = patientDaoImpl;
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

        // Query for doctor details
        String doctorSql = "SELECT u.name AS doctor_name, u.last_name AS doctor_last_name, u.email AS doctor_email, " +
                "u.password AS doctor_password, u.phone AS doctor_phone, u.language AS doctor_language,d.rating AS rating, d.rating_count AS rating_count " +
                "FROM Doctors d JOIN Users u ON d.doctor_id = u.id WHERE d.doctor_id = ?";
        Doctor doctor = jdbcTemplate.queryForObject(doctorSql, (rs, rowNum) -> new Doctor(
                rs.getString("doctor_name"),
                doctorId,
                rs.getString("doctor_last_name"),
                rs.getString("doctor_email"),
                rs.getString("doctor_password"),
                rs.getString("doctor_phone"),
                rs.getString("doctor_language"),
                rs.getDouble("rating"),
                rs.getInt("rating_count")
        ), doctorId);

        // Query for patient details
        String patientSql = "SELECT u.name AS patient_name, u.last_name AS patient_last_name, u.email AS patient_email, " +
                "u.password AS patient_password, u.phone AS patient_phone, u.language AS patient_language, " +
                "c.id AS coverage_id, c.coverage_name " +
                "FROM Clients p JOIN Users u ON p.client_id = u.id " +
                "LEFT JOIN Coverages c ON p.coverage_id = c.id WHERE p.client_id = ?";
        Patient patient = jdbcTemplate.queryForObject(patientSql, (rs, rowNum) -> new Patient(
                rs.getString("patient_name"),
                patientId,
                rs.getString("patient_last_name"),
                rs.getString("patient_email"),
                rs.getString("patient_password"),
                rs.getString("patient_phone"),
                rs.getString("patient_language"),
                new Coverage(rs.getLong("coverage_id"), rs.getString("coverage_name"))
        ), patientId);

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

//    @Override
//    public Optional<List<Appointment>> getByPatientId(long patientId) {
//        List<Appointment> appointments = jdbcTemplate.query(
//                "SELECT * FROM Appointments JOIN Specialties on Appointments.specialty_id = Specialties.id WHERE client_id = ? ORDER BY Appointments.date", ROW_MAPPER, patientId);
//        return appointments.isEmpty() ? Optional.empty() : Optional.of(appointments);
//    }

    @Override
    public Optional<List<Appointment>> getByPatientId(long patientId) {
        String sql = "SELECT a.id, a.date, a.status, a.reason, " +
                "s.id AS specialty_id, s.key AS specialty_key, " +
                "d.doctor_id,d.rating AS rating, d.rating_count AS rating_count, u.name AS doctor_name, u.last_name AS doctor_last_name, u.email AS doctor_email, " +
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
                "LEFT JOIN Coverages c ON p.coverage_id = c.id " +
                "WHERE a.client_id = ? " +
                "ORDER BY a.date";

        List<Appointment> appointments = jdbcTemplate.query(sql, ROW_MAPPER, patientId);
        return appointments.isEmpty() ? Optional.empty() : Optional.of(appointments);
    }

//    @Override
//    public Optional<List<Appointment>> getByDoctorId(long doctorId) {
//        List<Appointment> appointments = jdbcTemplate.query(
//                "SELECT * FROM Appointments JOIN Specialties on Appointments.specialty_id = Specialties.id WHERE doctor_id = ? ORDER BY Appointments.date", ROW_MAPPER, doctorId);
//        return appointments.isEmpty() ? Optional.empty() : Optional.of(appointments);
//    }

    @Override
    public Optional<List<Appointment>> getByDoctorId(long doctorId) {
        LocalDate today = LocalDate.now();
        LocalDate endOfNextMonth = today.plusMonths(1).withDayOfMonth(today.plusMonths(1).lengthOfMonth()).plusDays(1);
        String sql = "SELECT a.id, a.date, a.status, a.reason, " +
                "s.id AS specialty_id, s.key AS specialty_key, " +
                "d.doctor_id,d.rating AS rating, d.rating_count AS rating_count, u.name AS doctor_name, u.last_name AS doctor_last_name, u.email AS doctor_email, " +
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
                "LEFT JOIN Coverages c ON p.coverage_id = c.id " +
                "WHERE a.doctor_id = ? " +
                "AND a.status != 'cancelado' " +
                "AND a.date BETWEEN ? AND ? " +
                "ORDER BY a.date";

        List<Appointment> appointments = jdbcTemplate.query(
                sql,
                ROW_MAPPER,
                doctorId,
                today,
                endOfNextMonth
        );

        return appointments.isEmpty() ? Optional.empty() : Optional.of(appointments);
    }


    @Override
    public void cancelAppointment(long appointmentId) {
        jdbcTemplate.update("UPDATE Appointments SET status = ? WHERE id = ?", AppointmentStatus.CANCELADO.getValue(), appointmentId);
    }

    @Override
    public void acceptAppointment(long appointmentId) {
        jdbcTemplate.update("UPDATE Appointments SET status = ? WHERE id = ?", AppointmentStatus.CONFIRMADO.getValue(), appointmentId);
    }
//    @Override
//    public Optional<Appointment> getById(long appointmentId) {
//        return jdbcTemplate.query("SELECT * FROM Appointments JOIN Specialties on Appointments.specialty_id = Specialties.id  WHERE Appointments.id = ?", ROW_MAPPER, appointmentId).stream().findFirst();
//    }

    @Override
    public Optional<Appointment> getById(long appointmentId) {
        String sql = "SELECT a.id, a.date, a.status, a.reason, " +
                "s.id AS specialty_id, s.key AS specialty_key, " +
                "d.doctor_id,d.rating AS rating, d.rating_count AS rating_count, u.name AS doctor_name, u.last_name AS doctor_last_name, u.email AS doctor_email, " +
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
                "LEFT JOIN Coverages c ON p.coverage_id = c.id " +
                "WHERE a.id = ?";

        return jdbcTemplate.query(sql, ROW_MAPPER, appointmentId).stream().findFirst();
    }


    @Override
    public Optional<List<Appointment>> getPastDoctorAppointments(long doctorId, int page, int size) {
        String sql = "SELECT a.id, a.date, a.status, a.reason, " +
                "s.id AS specialty_id, s.key AS specialty_key, " +
                "d.doctor_id,d.rating AS rating, d.rating_count AS rating_count, u.name AS doctor_name, u.last_name AS doctor_last_name, u.email AS doctor_email, " +
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
                "LEFT JOIN Coverages c ON p.coverage_id = c.id " +
                "WHERE a.doctor_id = ? AND (a.date < NOW() OR a.status = 'cancelado') " +
                "ORDER BY a.date DESC " +
                "LIMIT ? OFFSET ?";

        List<Appointment> apps = jdbcTemplate.query(sql, ROW_MAPPER, doctorId, size, (page-1) * size);
        return apps.isEmpty() ? Optional.empty() : Optional.of(apps);
    }

    @Override
    public Optional<List<Appointment>> getFutureDoctorAppointments(long doctorId, int page, int size) {
        String sql = "SELECT a.id, a.date, a.status, a.reason, " +
                "s.id AS specialty_id, s.key AS specialty_key, " +
                "d.doctor_id,d.rating AS rating, d.rating_count AS rating_count, u.name AS doctor_name, u.last_name AS doctor_last_name, u.email AS doctor_email, " +
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
                "LEFT JOIN Coverages c ON p.coverage_id = c.id " +
                "WHERE a.doctor_id = ? AND a.date > NOW() AND a.status <> 'cancelado' " +
                "ORDER BY a.date ASC " +
                "LIMIT ? OFFSET ?";

        List<Appointment> apps = jdbcTemplate.query(sql, ROW_MAPPER, doctorId, size, (page-1) * size);
        return apps.isEmpty() ? Optional.empty() : Optional.of(apps);
    }

    @Override
    public Optional<List<Appointment>> getFuturePatientAppointments(long patientId, int page, int size) {
        String sql = "SELECT a.id, a.date, a.status, a.reason, " +
                "s.id AS specialty_id, s.key AS specialty_key, " +
                "d.doctor_id,d.rating AS rating, d.rating_count AS rating_count, u.name AS doctor_name, u.last_name AS doctor_last_name, u.email AS doctor_email, " +
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
                "LEFT JOIN Coverages c ON p.coverage_id = c.id " +
                "WHERE a.client_id = ? AND a.date > NOW() AND a.status <> 'cancelado' " +
                "ORDER BY a.date ASC " +
                "LIMIT ? OFFSET ?";

        List<Appointment> apps = jdbcTemplate.query(sql, ROW_MAPPER, patientId, size, (page-1) * size);
        return apps.isEmpty() ? Optional.empty() : Optional.of(apps);
    }

    @Override
    public Optional<List<Appointment>> getPastPatientAppointments(long patientId, int page, int size) {
        String sql = "SELECT a.id, a.date, a.status, a.reason, " +
                "s.id AS specialty_id, s.key AS specialty_key, " +
                "d.doctor_id,d.rating AS rating, d.rating_count AS rating_count, u.name AS doctor_name, u.last_name AS doctor_last_name, u.email AS doctor_email, " +
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
                "LEFT JOIN Coverages c ON p.coverage_id = c.id " +
                "WHERE a.client_id = ? AND (a.date < NOW() OR a.status = 'cancelado') " +
                "ORDER BY a.date DESC " +
                "LIMIT ? OFFSET ?";

        List<Appointment> apps = jdbcTemplate.query(sql, ROW_MAPPER, patientId, size, (page-1) * size);
        return apps.isEmpty() ? Optional.empty() : Optional.of(apps);
    }

    @Override
    public List<Appointment> getAppointmentsByDate(LocalDate today) {
        String sql = "SELECT a.id, a.date, a.status, a.reason, " +
                "s.id AS specialty_id, s.key AS specialty_key, " +
                "d.doctor_id,d.rating AS rating, d.rating_count AS rating_count, u.name AS doctor_name, u.last_name AS doctor_last_name, u.email AS doctor_email, " +
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
                "LEFT JOIN Coverages c ON p.coverage_id = c.id " +
                "WHERE DATE(a.date) = ? " +
                "ORDER BY a.date";

        return jdbcTemplate.query(sql, ROW_MAPPER, today);
    }

    @Override
    public int countFuturePatientAppointments(long patientId) {
        String sql = "SELECT COUNT(*) FROM Appointments WHERE client_id = ? AND date > NOW() AND status <> 'cancelado'";
        return jdbcTemplate.queryForObject(sql, Integer.class, patientId);
    }

    @Override
    public int countPastPatientAppointments(long patientId) {
        String sql = "SELECT COUNT(*) FROM Appointments WHERE client_id = ? AND (date < NOW() OR status = 'cancelado')";
        return jdbcTemplate.queryForObject(sql, Integer.class, patientId);
    }

    @Override
    public int countFutureDoctorAppointments(long doctorId) {
        String sql = "SELECT COUNT(*) FROM Appointments WHERE doctor_id = ? AND date > NOW() AND status <> 'cancelado'";
        return jdbcTemplate.queryForObject(sql, Integer.class, doctorId);
    }

    @Override
    public int countPastDoctorAppointments(long doctorId) {
        String sql = "SELECT COUNT(*) FROM Appointments WHERE doctor_id = ? AND (date < NOW() OR status = 'cancelado')";
        return jdbcTemplate.queryForObject(sql, Integer.class, doctorId);
    }

}