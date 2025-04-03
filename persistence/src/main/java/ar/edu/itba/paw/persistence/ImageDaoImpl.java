package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.ImageDao;
import ar.edu.itba.paw.models.Images;
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
public class ImageDaoImpl implements ImageDao {

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertImage;
    private final RowMapper<Images> ROW_MAPPER = (rs, rowNum) -> new Images(
            rs.getLong("doctor_id"),
            rs.getBytes("image")
    );
    @Autowired
    public ImageDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertImage = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("images")
                .usingColumns("doctor_id", "image");
    }

    @Override
    public Images create(long doctor_id, byte[] image) {
        final Map<String,Object> args = new HashMap<>();
        args.put("doctor_id",doctor_id);
        args.put("image",image);
        jdbcInsertImage.execute(args);
        return new Images(doctor_id,image);
    }

    @Override
    public Optional<Images> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM images WHERE image_id = ?", ROW_MAPPER, id).stream().findFirst();
    }

    @Override
    public Optional<Images> findByDoctorId(long doctor_id) {
        return jdbcTemplate.query("SELECT * FROM images WHERE doctor_id = ?", ROW_MAPPER, doctor_id).stream().findFirst();
    }
}
