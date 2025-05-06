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

    private final static RowMapper<Coverage> COVERAGE_MAPPER = (rs, rowNum) -> new Coverage(rs.getLong("id"), rs.getString("coverage_name"));
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;

    @Autowired
    public CoverageDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("coverages").usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Coverage> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM coverages WHERE id = ?", COVERAGE_MAPPER, id).stream().findFirst();
    }

    @Override
    public Coverage create(String name) {
        final Map<String, Object> values = new HashMap<>();
        values.put("coverage_name", name);
        final Number key = jdbcInsert.executeAndReturnKey(values);
        return new Coverage(key.longValue(), name);

    }

    @Override
    public Optional<Coverage> findByName(String name) {
        return jdbcTemplate.query("SELECT * FROM coverages WHERE coverage_name = ?", COVERAGE_MAPPER, name).stream().findFirst();
    }

    @Override
    public List<Coverage> getAll() {
        return jdbcTemplate.query("SELECT * FROM coverages", COVERAGE_MAPPER);
    }

    @Override
    public List<Coverage> findByIds(List<Long> ids) {
        return jdbcTemplate.query("SELECT * FROM coverages WHERE id IN (" + String.join(",", ids.stream().map(String::valueOf).toArray(String[]::new)) + ")", COVERAGE_MAPPER);
    }

    @Override
    public List<Coverage> findByDoctorId(long id) {
        return jdbcTemplate.query("SELECT * FROM coverages c " +
                        "JOIN doctor_coverages dc ON c.id = dc.coverage_id " +
                        "WHERE dc.doctor_id = ?",
                COVERAGE_MAPPER, id);
    }
}
