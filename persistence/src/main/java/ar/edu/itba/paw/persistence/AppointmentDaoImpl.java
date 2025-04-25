package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.AppointmentStatus;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Specialty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Repository
public class AppointmentDaoImpl implements AppointmentDao {

    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert jdbcInsert;

    private DoctorDaoImpl doctorDaoImpl;
    private ClientDaoImpl clientDaoImpl;

    private final RowMapper<Appointment> ROW_MAPPER = new RowMapper<Appointment>() {

        @Override
        public Appointment mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Appointment(
                    rs.getTimestamp("date").toLocalDateTime(),
                    rs.getString("status"),
                    rs.getString("reason"),
                    rs.getLong("id"),
                    new Specialty(rs.getLong("specialty_id"), rs.getString("key")),
                    doctorDaoImpl.getById(rs.getLong("doctor_id")).orElse(null),
                    clientDaoImpl.getById(rs.getLong("client_id")).orElse(null)
            );
        }
    };

    @Autowired
    public AppointmentDaoImpl(final DataSource ds, final DoctorDaoImpl doctorDaoImpl,final ClientDaoImpl clientDaoImpl) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("appointments")
                .usingColumns("client_id", "doctor_id", "date", "reason", "specialty_id")
                .usingGeneratedKeyColumns("id");
        this.doctorDaoImpl = doctorDaoImpl;
        this.clientDaoImpl = clientDaoImpl;
    }


    @Override
    public Appointment create(long clientId, long doctorId, LocalDateTime startDate, String reason, Specialty specialty) {
        final Map<String, Object> args = new HashMap<>();
        args.put("client_id", clientId);
        args.put("doctor_id", doctorId);
        args.put("date", java.sql.Timestamp.valueOf(startDate));
        args.put("reason", reason);
        args.put("specialty_id", specialty.getId());
        final Number appointmentId = jdbcInsert.executeAndReturnKey(args);
        return new Appointment(
                startDate,
                AppointmentStatus.PENDIENTE.getValue(),
                reason,
                appointmentId.longValue(),
                specialty,
                doctorDaoImpl.getById(doctorId).orElse(null),
                clientDaoImpl.getById(clientId).orElse(null)
        );
    }

    @Override
    public Optional<List<Appointment>> getByClientId(long clientId) {
        List<Appointment> appointments = jdbcTemplate.query(
                "SELECT * FROM Appointments JOIN Specialties on Appointments.specialty_id = Specialties.id WHERE client_id = ? ORDER BY Appointments.date", ROW_MAPPER, clientId);
        return appointments.isEmpty() ? Optional.empty() : Optional.of(appointments);
    }

    @Override
    public Optional<List<Appointment>> getByDoctorId(long doctorId) {
        List<Appointment> appointments = jdbcTemplate.query(
                "SELECT * FROM Appointments JOIN Specialties on Appointments.specialty_id = Specialties.id WHERE doctor_id = ? ORDER BY Appointments.date", ROW_MAPPER, doctorId);
        return appointments.isEmpty() ? Optional.empty() : Optional.of(appointments);
    }

    @Override
    public void cancelApointment(long appointmentId) {
        jdbcTemplate.update("UPDATE Appointments SET status = ? WHERE id = ?", AppointmentStatus.CANCELADO.getValue(), appointmentId);
    }

    @Override
    public void acceptAppointment(long appointmentId) {
        jdbcTemplate.update("UPDATE Appointments SET status = ? WHERE id = ?", AppointmentStatus.CONFIRMADO.getValue(), appointmentId);
    }
    @Override
    public Optional<Appointment> getById(long appointmentId) {
        return jdbcTemplate.query("SELECT * FROM Appointments JOIN Specialties on Appointments.specialty_id = Specialties.id  WHERE Appointments.id = ?", ROW_MAPPER, appointmentId).stream().findFirst();
    }

    @Override
    public Optional<List<Appointment>> getAllFutureAppointments(List<Long> doctorIds) {
        String sql = "SELECT * FROM Appointments JOIN Specialties on Appointments.specialty_id = Specialties.id WHERE doctor_id IN (" +
                String.join(",", Collections.nCopies(doctorIds.size(), "?")) + ") AND date > NOW() AND status <> 'cancelado'";
        List<Appointment> appointments = jdbcTemplate.query(sql, ROW_MAPPER, doctorIds.toArray());
        return appointments.isEmpty() ? Optional.empty() : Optional.of(appointments);
    }
}