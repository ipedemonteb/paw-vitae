package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.ClientDao;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Client;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class ClientDaoImpl implements ClientDao {

    private JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert jdbcInsert;

    private final static RowMapper<Client> ROW_MAPPER = new RowMapper<Client>() {
        @Override
        public Client mapRow(ResultSet rs, int rowNum) throws SQLException {
            return new Client(
                    rs.getLong("id"),
                    rs.getLong("coverage_id"),
                    rs.getString("coverage"),
                    rs.getString("name"),
                    rs.getString("email"),
                    rs.getString("password"),
                    rs.getString("phone")
            );
        }
    };

    public ClientDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("clients")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Client> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM clients WHERE id = ?", ROW_MAPPER, id).stream().findFirst();
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM clients WHERE email = ?", ROW_MAPPER, email).stream().findFirst();
    }

    @Override
    public Client create(String email, String password, long coverageId, String coverage, String name, String phone) {
        final Map<String, Object> args = new HashMap<>();
        args.put("email", email);
        args.put("password", password);
        args.put("coverage_id", coverageId);
        args.put("coverage", coverage);
        args.put("name", name);
        args.put("phone", phone);
        final Number clientId = jdbcInsert.executeAndReturnKey(args);
        return new Client(
                clientId.longValue(),
                coverageId,
                coverage,
                name,
                email,
                password,
                phone
        );
    }
}
