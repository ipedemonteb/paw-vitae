package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Patient;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class DaoUtils {

    public static final RowMapper<Doctor> DOCTOR_ROW_MAPPER = (rs, rowNum) -> new Doctor(
            rs.getString("name"),
            rs.getLong("id"),
            rs.getString("last_name"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("phone"),
            rs.getString("language"),
            rs.getDouble("rating"),
            rs.getInt("rating_count")
    );

    public final static RowMapper<Patient> PATIENT_ROW_MAPPER = (rs, rowNum) -> new Patient(
            rs.getString("name"),
            rs.getLong("id"),
            rs.getString("last_name"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("phone"),
            rs.getString("language"),
            new Coverage(rs.getLong("coverage_id"), rs.getString("coverage_name"))
    );

    public RowMapper<Patient> getPatientRowMapper() {
        return PATIENT_ROW_MAPPER;
    }

    public RowMapper<Doctor> getDoctorRowMapper() {
        return DOCTOR_ROW_MAPPER;
    }
}


