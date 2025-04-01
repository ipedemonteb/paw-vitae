package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.interfacePersistence.ClientDao;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.Coverage;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class ClientDaoImpl implements ClientDao {

    private AppointmentDao appointmentDao;

    private JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<Client> ROW_MAPPER = new RowMapper<Client>() {
        @Override
        public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Client(
                    rs.getString("name"),
                    rs.getLong("id"),
                    rs.getString("last_name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone"),
                    new Coverage(rs.getLong("coverage_id"), rs.getString("coverage_name"))
            );
        }
    };

    public ClientDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("clients")
                .usingColumns("client_id");
    }

    @Override
    public Optional<Client> getById(long id) {
        Optional<Client> client = jdbcTemplate.query("SELECT * FROM users u JOIN clients c ON c.client_id = u.id JOIN coverage cov ON cov.id = c.coverage_id WHERE u.id = ?", ROW_MAPPER, id).stream().findFirst();
        client.ifPresent(value -> value.setAppointments(appointmentDao.getByClientId(value.getId()).orElse(new ArrayList<>())));
        return client;
    }

    @Override
    public Optional<Client> getByEmail(String email) {
        Optional<Client> client = jdbcTemplate.query("SELECT * FROM users u JOIN clients c ON c.client_id = u.id JOIN coverage cov ON cov.id = c.coverage_id WHERE u.email = ?", ROW_MAPPER, email).stream().findFirst();
        client.ifPresent(value -> value.setAppointments(appointmentDao.getByClientId(value.getId()).orElse(new ArrayList<>())));
        return client;
    }

    @Override
    public Client create(String name, String lastName, String email, String password, String phone, Coverage coverage) {
        final Map<String, Object> args = new HashMap<>();
        args.put("email", email);
        args.put("password", password);
        args.put("coverage", coverage);
        args.put("name", name);
        args.put("phone", phone);
        args.put("last_name", lastName);
        final Number clientId = jdbcInsert.executeAndReturnKey(args);
        return new Client (
                name,
                clientId.longValue(),
                lastName,
                email,
                password,
                phone,
                coverage
        );
    }
}
