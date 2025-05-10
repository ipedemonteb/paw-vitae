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

    private final SimpleJdbcInsert jdbcInsertImage;
    private final RowMapper<Images> ROW_MAPPER = (rs, rowNum) -> new Images(
            rs.getLong("id"),
            rs.getBytes("image")
    );
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ImageDaoImpl(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsertImage = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("images")
                .usingColumns("image")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public Images create(byte[] image) {
        final Map<String, Object> args = new HashMap<>();
        args.put("image", image);
        Number id = jdbcInsertImage.executeAndReturnKey(args);
        return new Images(id.longValue(), image);
    }

    @Override
    public void deleteImage(long id) {
        jdbcTemplate.update("DELETE FROM images WHERE id = ?", id);
    }

    @Override
    public Optional<Images> findById(long id) {
        return jdbcTemplate.query("SELECT * FROM images WHERE id = ?", ROW_MAPPER, id).stream().findFirst();
    }
}
