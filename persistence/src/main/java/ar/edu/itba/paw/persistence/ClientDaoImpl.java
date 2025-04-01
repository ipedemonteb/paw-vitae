package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.interfacePersistence.ClientDao;
import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.Coverage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
public class ClientDaoImpl implements ClientDao {

    private AppointmentDao appointmentDao;

    private JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert jdbcInsertClient;

    private final SimpleJdbcInsert jdbcInsertUser;

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

    @Autowired
    public ClientDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertClient = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("clients")
                .usingColumns("client_id", "coverage_id");
        jdbcInsertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Client> getById(long id) {
        Optional<Client> client = jdbcTemplate.query("SELECT * FROM users u JOIN clients c ON c.client_id = u.id JOIN coverage cov ON cov.id = c.coverage_id WHERE u.id = ?", ROW_MAPPER, id).stream().findFirst();
        client.ifPresent(value -> value.setAppointments(appointmentDao.getByClientId(value.getId()).orElse(new ArrayList<>())));
        return client;
    }

    @Override
    public Client create(String name, String lastName, String email, String password, String phone, Coverage coverage) {
        final Map<String, Object> argsUser = new HashMap<>();
        argsUser.put("email", email);
        argsUser.put("password", password);
        argsUser.put("name", name);
        argsUser.put("phone", phone);
        argsUser.put("last_name", lastName);
        final Number clientId = jdbcInsertUser.executeAndReturnKey(argsUser);

        final Map<String, Object> argsClient = new HashMap<>();
        argsClient.put("client_id", clientId);
        argsClient.put("coverage_id", coverage.getId());
        jdbcInsertClient.execute(argsClient);

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
