package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.AvailabilitySlotsDao;
import ar.edu.itba.paw.models.AvailabilitySlot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class AvailabilitySlotDaoImpl implements AvailabilitySlotsDao {
    private  final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert simpleJdbcInsert;

    @Autowired
    public AvailabilitySlotDaoImpl(final DataSource ds) {
          this.jdbcTemplate = new JdbcTemplate(ds);
          this.simpleJdbcInsert = new SimpleJdbcInsert(jdbcTemplate)
                  .withTableName("doctor_availability")
                  .usingColumns("start_time", "end_time", "doctor_id", "day_of_week");
    }

    private static final RowMapper<AvailabilitySlot> AVAILABILITY_SLOT_ROW_MAPPER = (rs, rowNum) -> new AvailabilitySlot(
            rs.getInt("day_of_week"),
            rs.getTime("start_time").toLocalTime(),
            rs.getTime("end_time").toLocalTime()
    );

    @Override
    public AvailabilitySlot create(long docId, AvailabilitySlot slot) {
        final Map<String, Object> args = new HashMap<>();
        args.put("doctor_id", docId);
        args.put("day_of_week", slot.getDayOfWeek());
        args.put("start_time", slot.getStartTime());
        args.put("end_time", slot.getEndTime());
        simpleJdbcInsert.execute(args);
        return new AvailabilitySlot(slot.getDayOfWeek(), slot.getStartTime(), slot.getEndTime());
    }

    @Override
    public void updateDoctorAvailability(long id, List<AvailabilitySlot> availabilitySlots) {
        jdbcTemplate.update("DELETE FROM doctor_availability WHERE doctor_id = ?", id);
        for (AvailabilitySlot slot : availabilitySlots) {
            Map<String, Object> params = new HashMap<>();
            params.put("doctor_id", id);
            params.put("day_of_week", slot.getDayOfWeek());
            params.put("start_time", slot.getStartTime());
            params.put("end_time", slot.getEndTime());
            simpleJdbcInsert.execute(params);
        }
    }

    @Override
    public List<AvailabilitySlot> getAvailabilityByDoctorId(long doctorId) {
        return jdbcTemplate.query(
                "SELECT * FROM doctor_availability WHERE doctor_id = ?",AVAILABILITY_SLOT_ROW_MAPPER,
                doctorId
        );
    }
}