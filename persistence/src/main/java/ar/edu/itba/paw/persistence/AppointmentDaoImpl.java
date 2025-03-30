package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.models.Appointment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class AppointmentDaoImpl implements AppointmentDao {

    private JdbcTemplate jdbcTemplate;

    private SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<Appointment> ROW_MAPPER = new RowMapper<Appointment>() {
        @Override
        public Appointment mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Appointment(
                    rs.getLong("clientId"),
                    rs.getLong("doctorId"),
                    rs.getDate("startDate"),
                    rs.getDate("endDate"),
                    rs.getString("status"),
                    rs.getString("reason")
            );
        }
    };

    public AppointmentDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("appointments")
                .usingGeneratedKeyColumns("clientId", "doctorId", "startDate", "endDate");
    }


    @Override
    public Appointment create(long clientId, long doctorId, Date startDate, Date endDate, String status, String reason) {
        final Map<String, Object> args = new HashMap<>();
        args.put("clientId", clientId);
        args.put("doctorId", doctorId);
        args.put("startDate", startDate);
        args.put("endDate", endDate);
        args.put("status", status);
        args.put("reason", reason);
        jdbcInsert.execute(args);
        return new Appointment(
                clientId,
                doctorId,
                startDate,
                endDate,
                status,
                reason
        );
    }

    @Override
    public Optional<List<Appointment>> getByClientId(long clientId) {
        List<Appointment> appointments = jdbcTemplate.query(
                "SELECT * FROM appointments WHERE clientId = ?", ROW_MAPPER, clientId);
        return appointments.isEmpty() ? Optional.empty() : Optional.of(appointments);
    }

    @Override
    public Optional<List<Appointment>> getByDoctorId(long doctorId) {
        List<Appointment> appointments = jdbcTemplate.query(
                "SELECT * FROM appointments WHERE doctorId = ?", ROW_MAPPER, doctorId);
        return appointments.isEmpty() ? Optional.empty() : Optional.of(appointments);
    }
}
