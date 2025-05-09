package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.CoverageDao;
import ar.edu.itba.paw.interfacePersistence.PatientDao;
import ar.edu.itba.paw.interfacePersistence.UserDao;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Coverage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class PatientDaoImpl implements PatientDao {

    private static final String BASE_SQL = "SELECT u.name AS patient_name, u.id AS patient_id, u.last_name AS patient_last_name, " +
            "u.email AS patient_email, u.password AS patient_password, u.phone AS patient_phone, " +
            "u.language AS patient_language, cov.id AS coverage_id, cov.coverage_name, u.is_verified AS patient_verified " +
            "FROM Users u JOIN Clients c ON c.client_id = u.id " +
            "JOIN Coverages cov ON cov.id = c.coverage_id ";

    public static RowMapper<Patient> ROW_MAPPER;
    private final SimpleJdbcInsert jdbcInsertPatient;
    private final JdbcTemplate jdbcTemplate;
    private final UserDao userDao;
    private final CoverageDao coverageDao;


    @Autowired
    public PatientDaoImpl(final DataSource ds, UserDao userDao, CoverageDao coverageDao) {
        ROW_MAPPER = DaoRowMappers.PATIENT_ROW_MAPPER;
        this.userDao = userDao;
        this.coverageDao = coverageDao;
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertPatient = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("clients")
                .usingColumns("client_id", "coverage_id");
    }

    @Override
    public Optional<Patient> getById(long id) {
        StringBuilder sql = new StringBuilder(BASE_SQL);
        return jdbcTemplate.query(
                sql.append("WHERE u.id = ?").toString(),
                ROW_MAPPER, id
        ).stream().findFirst();
    }

//    @Override
//    public Patient create(String name, String lastName, String email, String password, String phone, String language, Coverage coverage) {
//        final Number patientId = userDao.create(name, lastName, email, password, phone, language);
//        final Map<String, Object> argsPatient = new HashMap<>();
//        argsPatient.put("client_id", patientId);
//        argsPatient.put("coverage_id", coverage.getId());
//        jdbcInsertPatient.execute(argsPatient);
//
//        return new Patient(
//                name,
//                patientId.longValue(),
//                lastName,
//                email,
//                password,
//                phone,
//                language,
//                coverage,
//                false
//        );
//    }

    @Override
    public Patient create(long id, String name, String lastName, String email, String password, String phone, String language, Long coverageId) {
        final Map<String, Object> argsPatient = new HashMap<>();
        argsPatient.put("client_id", id);
        argsPatient.put("coverage_id", coverageId);
        jdbcInsertPatient.execute(argsPatient);

        Coverage coverage = coverageDao.findById(coverageId).orElse(null); //TODO does this go here | check orElse thing

        return new Patient(
                name,
                id,
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
    public void updatePatient(long id, Long coverageId) {
        if (coverageId != null) {
            jdbcTemplate.update("UPDATE clients SET coverage_id = ? WHERE client_id = ?", coverageId, id);
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

    @Override
    public Optional<Patient> getByResetToken(String token) {
        StringBuilder sql = new StringBuilder(BASE_SQL);
        return jdbcTemplate.query(
                sql.append("WHERE u.reset_token = ?").toString(),
                ROW_MAPPER, token
        ).stream().findFirst();
    }
}
