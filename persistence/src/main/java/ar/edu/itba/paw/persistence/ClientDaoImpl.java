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
import java.util.List;
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
        return Optional.empty();
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        return Optional.empty();
    }

    @Override
    public Client create(long id, String email, String password, long coverageId, String coverage, String name, String phone, List<Appointment> appointments) {
        return null;
    }
}
