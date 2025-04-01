package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Doctor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;

import java.util.*;
import java.util.stream.Stream;

public class DoctorDaoImpl implements DoctorDao {

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertUser;
    private final SimpleJdbcInsert jdbcInsertDoctor;
    private final SimpleJdbcInsert jdbcInsertDoctorCoverage;


    private final RowMapper<Doctor> ROW_MAPPER = (rs, rowNum) -> new Doctor(
            rs.getString("name"),
            rs.getLong("id"),
            rs.getString("last_name"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("phone"),
            List.of(rs.getString("specialty").split(",")),
            new ArrayList<>()
    );

    public DoctorDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertDoctor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("doctors")
                .usingColumns("doctor_id", "specialties");
        jdbcInsertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        jdbcInsertDoctorCoverage = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("doctor_obra_social")
                .usingColumns("doctor_id", "coverage_id");
    }


    @Override
    public Doctor create(String name, String lastName,String email, String password, String phone, List<String> specialty, List<Coverage> coverages) {
        final Map<String, Object> argsUser = new HashMap<>();
        argsUser.put("name", name);
        argsUser.put("last_name", lastName);
        argsUser.put("email", email);
        argsUser.put("password", password);
        argsUser.put("phone", phone);
        argsUser.put("specialty", String.join(",", specialty));
//        argsUser.put("coverages", String.join(",", coverages.stream().map(Coverage::getName).toList()));
        final Number docId = jdbcInsertUser.executeAndReturnKey(argsUser);

        final Map<String, Object> argsDoctor = new HashMap<>();
        argsDoctor.put("doctor_id", docId);
        argsDoctor.put("specialties", String.join(",", specialty));
        jdbcInsertDoctor.execute(argsDoctor);


        final Map<String, Object> argsDoctorCoverage = new HashMap<>();
        for (Coverage coverage : coverages) {
            argsDoctorCoverage.put("doctor_id", docId);
            argsDoctorCoverage.put("coverage_id", coverage.getId());
            jdbcInsertDoctorCoverage.execute(argsDoctorCoverage);
        }

        Doctor doc = new Doctor(
                name,
                docId.longValue(),
                lastName,
                email,
                password,
                phone,
                specialty,
                coverages
        );
        doc.setCoverageList(coverages);
        return doc;
    }

    @Override
    public Optional<Doctor> getById(long id) {
        Optional<Doctor> doc = jdbcTemplate.query("SELECT * FROM users JOIN doctors ON users.id = doctors.doctor_id WHERE user.id = ?", ROW_MAPPER, id).stream().findFirst();
        doc.ifPresent(value -> value.setCoverageList(jdbcTemplate.query("SELECT * FROM doctor_obra_social JOIN coverages ON doctor_obra_social.coverage_id = coverages.id WHERE doctor_obra_social.doctor_id = ?", (rs, rowNum) -> new Coverage(rs.getLong("id"), rs.getString("name")), id)));
        return doc;
    }

}
