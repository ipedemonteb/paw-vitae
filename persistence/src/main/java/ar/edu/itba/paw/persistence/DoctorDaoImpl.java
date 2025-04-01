package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Doctor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;

import java.sql.Array;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DoctorDaoImpl implements DoctorDao {

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final RowMapper<Doctor> ROW_MAPPER = (rs, rowNum) -> new Doctor(
            rs.getString("name"),
            rs.getLong("id"),
            rs.getString("last_name"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("phone"),
            List.of(rs.getString("specialty").split(","))
    );

    public DoctorDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("doctors")
                .usingColumns("doctor_id");
    }


    @Override
    public Doctor create(String name, String lastName,String email, String password, String phone, List<String> specialty, List<Coverage> coverages) {
        final Map<String, Object> args = new HashMap<>();
        args.put("name", name);
        args.put("last_name", lastName);
        args.put("email", email);
        args.put("password", password);
        args.put("phone", phone);
        args.put("specialty", String.join(",", specialty));
        args.put("coverages", String.join(",", coverages.stream().map(Coverage::getName).toList()));
        final Number docId = jdbcInsert.executeAndReturnKey(args);
        Doctor doc = new Doctor(
                name,
                docId.longValue(),
                lastName,
                email,
                password,
                phone,
                specialty
        );
        doc.setCoverageList(coverages);
        return doc;
    }

    @Override
    public Optional<Doctor> getById(long id) {
        return jdbcTemplate.query("SELECT d.*, STRING_AGG(c.name, ',') AS coverages " +
                        "FROM doctors d " +
                        "JOIN users u ON d.doctor_id = u.id " +
                        "LEFT JOIN Doctor_Obra_Social dos ON d.doctor_id = dos.doctor_id " +
                        "LEFT JOIN coverages c ON dos.coverage_id = c.id " +
                        "WHERE d.doctor_id = ? " +
                        "GROUP BY d.doctor_id, u.id",
                ROW_MAPPER, id).stream().findFirst();
    }


    @Override
    public Optional<Doctor> getByEmail(String email) {
        return jdbcTemplate.query("SELECT d.*, STRING_AGG(c.name, ',') AS coverages " +
                        "FROM doctors d " +
                        "JOIN users u ON d.doctor_id = u.id " +
                        "LEFT JOIN Doctor_Obra_Social dos ON d.doctor_id = dos.doctor_id " +
                        "LEFT JOIN coverages c ON dos.coverage_id = c.id " +
                        "WHERE u.email = ? " +
                        "GROUP BY d.doctor_id, u.id",
                ROW_MAPPER, email).stream().findFirst();
    }

}
