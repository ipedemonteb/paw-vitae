package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.DoctorDao;
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
    private DoctorDao doctorDao;
    private final SimpleJdbcInsert jdbcInsertImage;
    private final RowMapper<Images> ROW_MAPPER = (rs, rowNum) -> new Images(
            rs.getLong("id"),
            rs.getBytes("image")
    );
    @Autowired
    public ImageDaoImpl(final DataSource ds,
                        final DoctorDao doctorDao) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertImage = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("images")
                .usingColumns( "image")
                .usingGeneratedKeyColumns("id");
        this.doctorDao = doctorDao;
    }

    @Override
    public Images create(byte[] image) {
        final Map<String,Object> args = new HashMap<>();
        args.put("image", image);
        Number id = jdbcInsertImage.executeAndReturnKey(args);
        return new Images(id.longValue(), image);
    }

    @Override
    public Optional<Images> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM images WHERE id = ?", ROW_MAPPER, id).stream().findFirst();
    }
}
