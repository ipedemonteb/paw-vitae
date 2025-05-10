package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.AppointmentFileDao;
import ar.edu.itba.paw.models.AppointmentFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Repository
public class AppointmentFileDaoImpl implements AppointmentFileDao {

    private final static RowMapper<AppointmentFile> ROW_MAPPER = (rs, rowNum) -> new AppointmentFile(
            rs.getString("file_name"),
            rs.getBytes("file_data"),
            rs.getLong("id"),
            rs.getString("uploader_role"),
            rs.getLong("appointment_id")
    );
    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;

    @Autowired
    public AppointmentFileDaoImpl(final DataSource dataSource) {
        jdbcTemplate = new JdbcTemplate(dataSource);
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("appointment_files")
                .usingColumns("file_name", "file_data", "uploader_role", "appointment_id")
                .usingGeneratedKeyColumns("id");
    }

    @Override
    public AppointmentFile create(String fileName, byte[] fileData, String uploader_role, long appointment_id) {
        HashMap<String, Object> values = new HashMap<>();
        values.put("file_name", fileName);
        values.put("file_data", fileData);
        values.put("uploader_role", uploader_role);
        values.put("appointment_id", appointment_id);
        final Number key = jdbcInsert.executeAndReturnKey(values);
        return new AppointmentFile(fileName, fileData, key.longValue(), uploader_role, appointment_id);
    }

    @Override
    public Optional<AppointmentFile> getById(long id) {
        return jdbcTemplate.query("SELECT * FROM appointment_files WHERE id = ?", ROW_MAPPER, id)
                .stream()
                .findFirst();
    }

    @Override
    public List<AppointmentFile> getByAppointmentId(long appointment_id) {
        return jdbcTemplate.query("SELECT * FROM appointment_files WHERE appointment_id = ?", ROW_MAPPER, appointment_id);
    }
}
