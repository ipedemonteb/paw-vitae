package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Patient;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

public final class DaoRowMappers {

    private static final RowMapper<Doctor> DOCTOR_ROW_MAPPER = (rs, rowNum) -> new Doctor(
            rs.getString("doctor_name"),
            rs.getLong("doctor_id"),
            rs.getString("doctor_last_name"),
            rs.getString("doctor_email"),
            rs.getString("doctor_password"),
            rs.getString("doctor_phone"),
            rs.getString("doctor_language"),
            rs.getLong("image_id"),
            rs.getDouble("rating"),
            rs.getInt("rating_count"),
            rs.getBoolean("doctor_verified")
    );

    private final static RowMapper<Patient> PATIENT_ROW_MAPPER = (rs, rowNum) -> new Patient(
            rs.getString("patient_name"), // Use the alias patient_name
            rs.getLong("patient_id"),    // Use the alias patient_id
            rs.getString("patient_last_name"),
            rs.getString("patient_email"),
            rs.getString("patient_password"),
            rs.getString("patient_phone"),
            rs.getString("patient_language"),
            new Coverage(rs.getLong("coverage_id"), rs.getString("coverage_name")),
            rs.getBoolean("patient_verified")
    );

    public static RowMapper<Patient> getPatientRowMapper() {
        return PATIENT_ROW_MAPPER;
    }

    public static RowMapper<Doctor> getDoctorRowMapper() { return DOCTOR_ROW_MAPPER; }
}


