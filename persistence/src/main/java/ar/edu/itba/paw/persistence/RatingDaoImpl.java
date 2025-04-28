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
import java.util.Map;
import java.util.Optional;
@Repository
public class RatingDaoImpl implements RatingDao
{
    private JdbcTemplate jdbcTemplate;
    private SimpleJdbcInsert jdbcInsert;
    @Autowired
    public RatingDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("ratings")
                .usingColumns("rating", "doctor_id", "patient_id", "appointment_id", "comment")
                .usingGeneratedKeyColumns("id");
    }
    private static final RowMapper<Rating> ROW_MAPPER = (rs, rowNum) -> {
        return new Rating(
            rs.getDouble("rating"),
            rs.getInt("doctor_id"),
            rs.getInt("patient_id"),
            rs.getInt("appointment_id"),
            rs.getString("comment"),
            rs.getInt("id")
        );
    };
    @Override
    public Rating create(double rating, int doctorId, int patientId, int appointmentId, String comment, long id) {
        final Map<String,Object> args = new HashMap<>(Map.of(
                "rating", rating,
                "doctor_id", doctorId,
                "patient_id", patientId,
                "appointment_id", appointmentId,
                "comment", comment
        ));
        final Number ratingId = jdbcInsert.executeAndReturnKey(args);
        args.put("id", ratingId);
        return new Rating(
                rating,
                doctorId,
                patientId,
                appointmentId,
                comment,
                ratingId.longValue()
        );
    }

    @Override
    public Optional<Rating> getRating(int ratingId) {
        return jdbcTemplate.query("SELECT * FROM ratings WHERE id = ?", ROW_MAPPER, ratingId)
                .stream()
                .findFirst();
    }

    @Override
    public Optional<Rating> getRatingByAppointmentId(int appointmentId) {
        return jdbcTemplate.query("SELECT * FROM ratings WHERE appointment_id = ?", ROW_MAPPER, appointmentId)
                .stream()
                .findFirst();
    }

}
