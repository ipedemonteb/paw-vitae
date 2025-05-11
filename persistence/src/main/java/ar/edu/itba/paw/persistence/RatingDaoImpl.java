package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.RatingDao;
import ar.edu.itba.paw.models.Rating;
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
public class RatingDaoImpl implements RatingDao {

    private static final RowMapper<Rating> ROW_MAPPER = (rs, rowNum) -> new Rating(
            rs.getLong("id"),
            rs.getInt("rating"),
            rs.getInt("doctor_id"),
            rs.getInt("patient_id"),
            rs.getInt("appointment_id"),
            rs.getString("comment")
    );
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public RatingDaoImpl(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("ratings")
                .usingColumns("rating", "doctor_id", "patient_id", "appointment_id", "comment")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Rating create(long rating, long doctorId, long patientId, long appointmentId, String comment) {
        final Map<String, Object> args = new HashMap<>(Map.of(
                "rating", rating,
                "doctor_id", doctorId,
                "patient_id", patientId,
                "appointment_id", appointmentId,
                "comment", comment
        ));
        final Number ratingId = jdbcInsert.executeAndReturnKey(args);
        args.put("id", ratingId);
        return new Rating(
                ratingId.longValue(),
                rating,
                doctorId,
                patientId,
                appointmentId,
                comment
        );
    }

    @Override
    public Optional<Rating> getRating(long ratingId) {
        return jdbcTemplate.query("SELECT * FROM ratings WHERE id = ?", ROW_MAPPER, ratingId)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Rating> getRatingByAppointmentId(long appointmentId) {
        return jdbcTemplate.query("SELECT * FROM ratings WHERE appointment_id = ?", ROW_MAPPER, appointmentId)
                .stream()
                .findFirst();
    }

    @Override
    public List<Rating> getRatingsByDoctorId(long doctorId) {
        return jdbcTemplate.query("SELECT * FROM ratings WHERE doctor_id = ?", ROW_MAPPER, doctorId);
    }

    @Override
    public List<Rating> getRatingsByPatientId(long patientId) {
        return jdbcTemplate.query("SELECT * FROM ratings WHERE patient_id = ?", ROW_MAPPER, patientId);
    }

    @Override
    public List<Rating> getFiveTopRatings() {
        return jdbcTemplate.query("SELECT * FROM ratings where rating >= 4 AND comment IS NOT NULL AND comment <> '' ORDER BY rating DESC LIMIT 5", ROW_MAPPER);
    }

}
