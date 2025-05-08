package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.models.*;
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

    final static RowMapper<Patient> PATIENT_ROW_MAPPER = (rs, rowNum) -> new Patient(
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

    final static RowMapper<Appointment> APPOINTMENT_ROW_MAPPER = (rs, rowNum) -> new Appointment(
            rs.getTimestamp("date").toLocalDateTime(),
            rs.getString("status"),
            rs.getString("reason"),
            rs.getLong("id"),
            new Specialty(rs.getLong("specialty_id"), rs.getString("specialty_key")),
            DOCTOR_ROW_MAPPER.mapRow(rs, 1),
            PATIENT_ROW_MAPPER.mapRow(rs, 1),
            rs.getString("report")
    );
}


