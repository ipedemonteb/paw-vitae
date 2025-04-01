package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.CoverageDao;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class CoverageDaoImpl implements CoverageDao {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private final static RowMapper<Coverage> COVERAGE_MAPPER = (rs, rowNum) -> new Coverage(rs.getLong("id"), rs.getString("name"));

    @Autowired
    public CoverageDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("Obras_Sociales").usingGeneratedKeyColumns("id_obra_social");
    }

    @Override
    public Optional<Coverage> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM Obras_Sociales WHERE id = ?", new Object[]{id}, COVERAGE_MAPPER).stream().findFirst();
    }

    @Override
    public Coverage create(String name) {
        final Map<String, Object> values = new HashMap<>();
        values.put("nombre_obra_social", name);
        final Number key = jdbcInsert.executeAndReturnKey(values);
        return new Coverage(key.longValue(), name);

    }

    @Override
    public Optional<Coverage> findByName(String name) {
        return jdbcTemplate.query("SELECT * FROM Obras_Sociales WHERE name = ?", new Object[]{name}, COVERAGE_MAPPER).stream().findFirst();
    }

    @Override
    public Optional<List<Coverage>> getAll() {
        return Optional.of(jdbcTemplate.query("SELECT * FROM Obras_Sociales", COVERAGE_MAPPER));
    }
}
