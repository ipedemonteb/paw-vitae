package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.AvailabilitySlot;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Specialty;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import javax.print.Doc;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalTime;
import java.util.*;

public class DoctorExtractor implements ResultSetExtractor<List<Doctor>> {

    @Override
    public List<Doctor> extractData(ResultSet rs)
            throws SQLException, DataAccessException {

        Map<Long, Doctor> map = new LinkedHashMap<>();
        while (rs.next()) {
            long id = rs.getLong("doctor_id");
            Doctor doc = map.get(id);
            if (doc == null) {
                doc = new Doctor(
                        rs.getString("doctor_name"),
                        id,
                        rs.getString("doctor_last_name"),
                        rs.getString("doctor_email"),
                        rs.getString("doctor_password"),
                        rs.getString("doctor_phone"),
                        rs.getString("doctor_language"),
                        rs.getBoolean("doctor_verified"),
                        new ArrayList<>(), // will fill below
                        new ArrayList<>(),
                        new ArrayList<>(),
                        rs.getDouble("rating"),
                        rs.getInt("rating_count"),
                        rs.getLong("image_id")
                );
                map.put(id, doc);
            }

            // specialties
            long specId = rs.getLong("specialty_id");
            if (!rs.wasNull()) {
                Specialty s = new Specialty(specId, rs.getString("specialty_key"));
                if (!doc.getSpecialtyList().contains(s)) {
                    doc.getSpecialtyList().add(s);
                }
            }

            // coverages
            long covId = rs.getLong("coverage_id");
            if (!rs.wasNull()) {
                Coverage c = new Coverage(covId, rs.getString("coverage_name"));
                if (!doc.getCoverageList().contains(c)) {
                    doc.getCoverageList().add(c);
                }
            }

            // availability
            int dow = rs.getInt("day_of_week");
            if (!rs.wasNull()) {
                LocalTime start = rs.getTime("start_time").toLocalTime();
                LocalTime end = rs.getTime("end_time").toLocalTime();
                AvailabilitySlot slot = new AvailabilitySlot(dow, start, end);
                if (!doc.getAvailabilitySlots().contains(slot)) {
                    doc.getAvailabilitySlots().add(slot);
                }
            }
        }
        return new ArrayList<>(map.values());
    }
}
