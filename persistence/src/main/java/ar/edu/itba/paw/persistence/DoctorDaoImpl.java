package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.models.Doctor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DoctorDaoImpl implements DoctorDao {

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<Doctor> ROW_MAPPER = (rs, rowNum) -> new Doctor(
            rs.getLong("id"),
            rs.getString("name"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("phone"),
            List.of(rs.getString("specialty").split(","))
    );

    public DoctorDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("doctors")
                .usingGeneratedKeyColumns("id");
    }


    @Override
    public Doctor create(String name, String email, String password, String phone, List<String> specialty) {
         final Map<String, Object> args = new HashMap<>();
            args.put("name", name);
            args.put("email", email);
            args.put("password", password);
            args.put("phone", phone);
            args.put("specialty", String.join(",", specialty));
            final Number doctorId = jdbcInsert.executeAndReturnKey(args);
            return new Doctor(doctorId.longValue(), name, email, password, phone, specialty);
    }

    @Override
    public Optional<Doctor> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM doctors WHERE id = ?", ROW_MAPPER, id).stream().findFirst();
    }

    @Override
    public Optional<Doctor> findByEmail(String email) {
        return jdbcTemplate.query("SELECT * FROM doctors WHERE email = ?", ROW_MAPPER, email).stream().findFirst();
    }
}
