package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.models.Appointment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
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

    private final static RowMapper<Appointment> ROW_MAPPER = new RowMapper<Appointment>() {
        @Override
        public Appointment mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Appointment(
                    rs.getLong("client_id"),
                    rs.getLong("doctor_id"),
                    rs.getTimestamp("start_date").toLocalDateTime(),
                    rs.getString("status"),
                    rs.getString("reason"),
                    rs.getLong("id")
            );
        }
    };

    @Autowired
    public AppointmentDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("appointments")
                .usingGeneratedKeyColumns("id");
    }


    @Override
    public Appointment create(long clientId, long doctorId, LocalDateTime startDate, String status, String reason) {
        final Map<String, Object> args = new HashMap<>();
        args.put("client_id", clientId);
        args.put("doctor_id", doctorId);
        args.put("start_date", java.sql.Timestamp.valueOf(startDate));
        args.put("status", status);
        args.put("reason", reason);
        final Number appointmentId = jdbcInsert.executeAndReturnKey(args);
        return new Appointment(
                clientId,
                doctorId,
                startDate,
                status,
                reason,
                appointmentId.longValue()
        );
    }

    @Override
    public Optional<List<Appointment>> getByClientId(long clientId) {
        List<Appointment> appointments = jdbcTemplate.query(
                "SELECT * FROM Appointments WHERE client_id = ?", ROW_MAPPER, clientId);
        return appointments.isEmpty() ? Optional.empty() : Optional.of(appointments);
    }

    @Override
    public Optional<List<Appointment>> getByDoctorId(long doctorId) {
        List<Appointment> appointments = jdbcTemplate.query(
                "SELECT * FROM Appointments WHERE doctor_id = ?", ROW_MAPPER, doctorId);
        return appointments.isEmpty() ? Optional.empty() : Optional.of(appointments);
    }
}
