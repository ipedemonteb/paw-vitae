package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.SpecialtyDao;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Specialty;
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
public class SpecialtyDaoImpl implements SpecialtyDao {

    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    private final static RowMapper<Specialty> SPECIALTY_MAPPER = (rs, rowNum) -> new Specialty(rs.getLong("id"), rs.getString("key"));

    @Autowired
    public SpecialtyDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("coverages").usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Specialty> getById(long id) {
        return jdbcTemplate.query("SELECT * FROM specialties WHERE id = ?", SPECIALTY_MAPPER,id).stream().findFirst();
    }

    @Override
    public Optional<Specialty> getByName(String name) {
        return jdbcTemplate.query("SELECT * FROM specialties WHERE key = ?", SPECIALTY_MAPPER,name).stream().findFirst();
    }

    @Override
    public List<Specialty> getAll() {
        return jdbcTemplate.query("SELECT * FROM specialties", SPECIALTY_MAPPER);
    }

    @Override
    public List<Specialty> getByIds(List<Long> ids) {
        return jdbcTemplate.query("SELECT * FROM specialties WHERE id IN (" + String.join(",", ids.stream().map(String::valueOf).toArray(String[]::new)) + ")", SPECIALTY_MAPPER);
    }
}
