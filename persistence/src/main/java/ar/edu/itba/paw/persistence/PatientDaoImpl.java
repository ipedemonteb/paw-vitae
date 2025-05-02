package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.PatientDao;
import ar.edu.itba.paw.models.Patient;
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
public class PatientDaoImpl implements PatientDao {

    private static final String BASE_SQL = "SELECT u.name AS patient_name, u.id AS patient_id, u.last_name AS patient_last_name, " +
            "u.email AS patient_email, u.password AS patient_password, u.phone AS patient_phone, " +
            "u.language AS patient_language, cov.id AS coverage_id, cov.coverage_name, u.is_verified AS patient_verified " +
            "FROM Users u JOIN Clients c ON c.client_id = u.id " +
            "JOIN Coverages cov ON cov.id = c.coverage_id ";

    private JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert jdbcInsertPatient;

    private final SimpleJdbcInsert jdbcInsertUser;

    public static RowMapper<Patient> ROW_MAPPER;



    @Autowired
    public PatientDaoImpl(final DataSource ds, final DaoUtils daoUtils) {
        ROW_MAPPER = daoUtils.getPatientRowMapper();
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertPatient = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("clients")
                .usingColumns("client_id", "coverage_id");
        jdbcInsertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Optional<Patient> getById(long id) {
        StringBuilder sql = new StringBuilder(BASE_SQL);
        return jdbcTemplate.query(
                sql.append("WHERE u.id = ?").toString(),
                ROW_MAPPER, id
        ).stream().findFirst();
    }

    @Override
    public Patient create(String name, String lastName, String email, String password, String phone, String language, Coverage coverage) {
        final Map<String, Object> argsUser = new HashMap<>();
        argsUser.put("email", email);
        argsUser.put("password", password);
        argsUser.put("name", name);
        argsUser.put("phone", phone);
        argsUser.put("last_name", lastName);
        argsUser.put("language", language);
        argsUser.put("is_verified", false);
        final Number patientId = jdbcInsertUser.executeAndReturnKey(argsUser);
        final Map<String, Object> argsPatient = new HashMap<>();
        argsPatient.put("client_id", patientId);
        argsPatient.put("coverage_id", coverage.getId());
        jdbcInsertPatient.execute(argsPatient);

        return new Patient(
                name,
                patientId.longValue(),
                lastName,
                email,
                password,
                phone,
                language,
                coverage,
                false
        );
    }

    @Override
    public Optional<Patient> getByEmail(String email) {
        StringBuilder sql = new StringBuilder(BASE_SQL);
        return jdbcTemplate.query(
                sql.append("WHERE u.email = ?").toString(),
                ROW_MAPPER, email
        ).stream().findFirst();
    }

    @Override
    public void updatePatient(long id, String name, String lastName, String phone, Coverage coverage) {
        jdbcTemplate.update("UPDATE users SET name = ?, last_name = ?, phone = ? WHERE id = ?",
                name, lastName, phone, id);
        if (coverage != null) {
            jdbcTemplate.update("UPDATE clients SET coverage_id = ? WHERE client_id = ?", coverage.getId(), id);
        }
    }

    @Override
    public List<Patient> getByIds(Set<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        StringBuilder sql = new StringBuilder(BASE_SQL);
        return jdbcTemplate.query(
                sql.append("WHERE u.id IN (")
                        .append(String.join(",", Collections.nCopies(ids.size(), "?")))
                        .append(")").toString(),
                ROW_MAPPER, ids.toArray()
        );
    }

    @Override
    public void changePassword(long id, String password) {
        jdbcTemplate.update("UPDATE users SET password = ? WHERE id = ?", password, id);
    }
    @Override
    public String getLanguage(long id) {
        return jdbcTemplate.query("SELECT language FROM Users WHERE id = ?", (rs, rowNum) -> rs.getString("language"), id).stream().findFirst().orElse(null);
    }

    @Override
    public Optional<Patient> getByVerificationToken(String token) {
        StringBuilder sql = new StringBuilder(BASE_SQL);
        return jdbcTemplate.query(
                sql.append("WHERE u.verification_token = ?").toString(),
                ROW_MAPPER, token
        ).stream().findFirst();
    }

    @Override
    public void changeLanguage(long id, String language) {
        jdbcTemplate.update("UPDATE users SET language = ? WHERE id = ?", language, id);
    }
}
