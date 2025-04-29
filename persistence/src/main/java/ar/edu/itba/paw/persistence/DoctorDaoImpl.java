package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.*;

@Repository
public class DoctorDaoImpl implements DoctorDao {

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertUser;
    private final SimpleJdbcInsert jdbcInsertDoctor;
    private final SimpleJdbcInsert jdbcInsertDoctorCoverage;
    private final SimpleJdbcInsert jdbcInsertDoctorSpecialty;
    private final SimpleJdbcInsert jdbcInsertDoctorAvailability;

    public static final RowMapper<Doctor> ROW_MAPPER = (rs, rowNum) -> new Doctor(
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

    @Autowired
    public DoctorDaoImpl(final DataSource ds) {
        jdbcTemplate = new JdbcTemplate(ds);
        jdbcInsertDoctor = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("doctors")
                .usingColumns("doctor_id");
        jdbcInsertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");
        jdbcInsertDoctorCoverage = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("doctor_coverages")
                .usingColumns("doctor_id", "coverage_id");
        jdbcInsertDoctorSpecialty = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("doctor_specialties")
                .usingColumns("doctor_id", "specialty_id");
        jdbcInsertDoctorAvailability = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("doctor_availability")
                .usingColumns("doctor_id", "day_of_week", "start_time", "end_time");
    }

    @Override
    public Doctor create(String name, String lastName, String email, String password, String phone, String language,
                         List<Specialty> specialties, List<Coverage> coverages, List<AvailabilitySlot> availabilityList) {
        final Map<String, Object> argsUser = new HashMap<>();
        argsUser.put("name", name);
        argsUser.put("last_name", lastName);
        argsUser.put("email", email);
        argsUser.put("password", password);
        argsUser.put("phone", phone);
        argsUser.put("language", language);
        final Number docId = jdbcInsertUser.executeAndReturnKey(argsUser);

        final Map<String, Object> argsDoctor = new HashMap<>();
        argsDoctor.put("doctor_id", docId);
        jdbcInsertDoctor.execute(argsDoctor);

        for (Specialty specialty : specialties) {
            final Map<String, Object> args = new HashMap<>();
            args.put("doctor_id", docId);
            args.put("specialty_id", specialty.getId());
            jdbcInsertDoctorSpecialty.execute(args);
        }

        for (Coverage coverage : coverages) {
            final Map<String, Object> args = new HashMap<>();
            args.put("doctor_id", docId);
            args.put("coverage_id", coverage.getId());
            jdbcInsertDoctorCoverage.execute(args);
        }

        for (AvailabilitySlot slot : availabilityList) {
            final Map<String, Object> args = new HashMap<>();
            args.put("doctor_id", docId);
            args.put("day_of_week", slot.getDayOfWeek());
            args.put("start_time", slot.getStartTime());
            args.put("end_time", slot.getEndTime());
            jdbcInsertDoctorAvailability.execute(args);
        }

        Doctor doc = new Doctor(
                name,
                docId.longValue(),
                lastName,
                email,
                password,
                phone,
                language
        );
        doc.setCoverageList(coverages);
        doc.setSpecialtyList(specialties);
        doc.setAvailabilitySlots(availabilityList);
        return doc;
    }

    @Override
    public Optional<Doctor> getById(long id) {
        Optional<Doctor> doc = jdbcTemplate.query(
                "SELECT * FROM users JOIN doctors ON users.id = doctors.doctor_id WHERE users.id = ?",
                ROW_MAPPER, id
        ).stream().findFirst();
        doc.ifPresent(this::populateDoctorDetails);
        return doc;
    }

    @Override
    public Optional<Doctor> getByEmail(String email) {
        Optional<Doctor> doc = jdbcTemplate.query(
                "SELECT * FROM users JOIN doctors ON users.id = doctors.doctor_id WHERE users.email = ?",
                ROW_MAPPER, email
        ).stream().findFirst();

        doc.ifPresent(this::populateDoctorDetails);
        return doc;
    }

    @Override
    public List<Doctor> getAll() {
        List<Doctor> doctors = jdbcTemplate.query(
                "SELECT * FROM users JOIN doctors ON users.id = doctors.doctor_id",
                ROW_MAPPER
        );

        for (Doctor doctor : doctors) {
            populateDoctorDetails(doctor);
        }

        return doctors;
    }

    @Override
    public void UpdateDoctorRating(long id, double newRating) {
        jdbcTemplate.update(
                "UPDATE doctors " +
                        "SET rating = ((rating * rating_count + ?) / (rating_count + 1)), " +
                        "    rating_count = rating_count + 1 " +
                        "WHERE doctor_id = ?",
                newRating, id
        );
    }

    @Override
    public List<Doctor> getByIds(Set<Long> ids) {
        if (ids.isEmpty()) {
            return Collections.emptyList();
        }
        return jdbcTemplate.query("SELECT * FROM Users u JOIN Doctors d ON d.doctor_id = u.id  WHERE u.id IN (" + String.join(",", Collections.nCopies(ids.size(), "?")) + ")", ROW_MAPPER, ids.toArray());
    }

    @Override
    public List<Doctor> getBySpecialty(long specialtyId, int page, int pageSize) {
        List<Doctor> doctors = jdbcTemplate.query(
                "SELECT * FROM users JOIN doctors ON users.id = doctors.doctor_id " +
                        "JOIN doctor_specialties ON doctors.doctor_id = doctor_specialties.doctor_id " +
                        "JOIN specialties ON doctor_specialties.specialty_id = specialties.id " +
                        "WHERE doctor_specialties.specialty_id = ?"
                        + " LIMIT ? OFFSET ?"
                ,
                new Object[]{specialtyId, pageSize, (page - 1) * pageSize},
                ROW_MAPPER
        );

        for (Doctor doctor : doctors) {
            populateDoctorDetails(doctor);
        }

        return doctors;
    }

    @Override
    public void updateDoctor(long id, String name, String lastName, String phone, List<Specialty> specialties, List<Coverage> coverages, List<AvailabilitySlot> availabilityList) {
        Doctor currentDoctor = getById(id).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        String updatedName = (name != null) ? name : currentDoctor.getName();
        String updatedLastName = (lastName != null) ? lastName : currentDoctor.getLastName();
        String updatedPhone = (phone != null) ? phone : currentDoctor.getPhone();
        List<Specialty> updatedSpecialties = (specialties != null) ? specialties : currentDoctor.getSpecialtyList();
        List<Coverage> updatedCoverages = (coverages != null) ? coverages : currentDoctor.getCoverageList();
        List<AvailabilitySlot> updatedAvailability = (availabilityList != null) ? availabilityList : currentDoctor.getAvailabilitySlots();

        // Update basic doctor info
        jdbcTemplate.update(
                "UPDATE users SET name = ?, last_name = ?, phone = ? WHERE id = ?",
                updatedName, updatedLastName, updatedPhone, id
        );

        // Update specialties
        jdbcTemplate.update("DELETE FROM doctor_specialties WHERE doctor_id = ?", id);
        for (Specialty specialty : updatedSpecialties) {
            jdbcTemplate.update(
                    "INSERT INTO doctor_specialties (doctor_id, specialty_id) VALUES (?, ?)",
                    id, specialty.getId()
            );
        }

        // Update coverages
        jdbcTemplate.update("DELETE FROM doctor_coverages WHERE doctor_id = ?", id);
        for (Coverage coverage : updatedCoverages) {
            jdbcTemplate.update(
                    "INSERT INTO doctor_coverages (doctor_id, coverage_id) VALUES (?, ?)",
                    id, coverage.getId()
            );
        }

        // Update availability
        jdbcTemplate.update("DELETE FROM doctor_availability WHERE doctor_id = ?", id);
        for (AvailabilitySlot slot : updatedAvailability) {
            Map<String, Object> params = new HashMap<>();
            params.put("doctor_id", id);
            params.put("day_of_week", slot.getDayOfWeek());
            params.put("start_time", slot.getStartTime());
            params.put("end_time", slot.getEndTime());
            jdbcInsertDoctorAvailability.execute(params);
        }
    }


    @Override
    public void addAvailability(long doctorId, List<AvailabilitySlot> availabilityList) {
        for (AvailabilitySlot slot : availabilityList) {
            Map<String, Object> params = new HashMap<>();
            params.put("doctor_id", doctorId);
            params.put("day_of_week", slot.getDayOfWeek());
            params.put("start_time", slot.getStartTime());
            params.put("end_time", slot.getEndTime());
            jdbcInsertDoctorAvailability.execute(params);
        }
    }

    @Override
    public List<AvailabilitySlot> getAvailabilityByDoctorId(long doctorId) {
        return jdbcTemplate.query(
                "SELECT * FROM doctor_availability WHERE doctor_id = ?",
                (rs, rowNum) -> new AvailabilitySlot(
                        rs.getInt("day_of_week"),
                        rs.getTime("start_time").toLocalTime(),
                        rs.getTime("end_time").toLocalTime()
                ),
                doctorId
        );
    }

    private void populateDoctorDetails(Doctor doctor) {
        long id = doctor.getId();

        doctor.setCoverageList(jdbcTemplate.query(
                "SELECT * FROM doctor_coverages JOIN coverages ON doctor_coverages.coverage_id = coverages.id WHERE doctor_coverages.doctor_id = ?",
                (rs, rowNum) -> new Coverage(rs.getLong("id"), rs.getString("coverage_name")),
                id
        ));

        doctor.setSpecialtyList(jdbcTemplate.query(
                "SELECT * FROM doctor_specialties JOIN specialties ON doctor_specialties.specialty_id = specialties.id WHERE doctor_specialties.doctor_id = ?",
                (rs, rowNum) -> new Specialty(rs.getLong("id"), rs.getString("key")),
                id
        ));
        doctor.setAvailabilitySlots(getAvailabilityByDoctorId(id));
    }

    // Add this method to the DoctorDaoImpl class
    @Override
    public void updateDoctorAvailability(long id, List<AvailabilitySlot> availabilitySlots) {
        // Delete existing availability slots
        jdbcTemplate.update("DELETE FROM doctor_availability WHERE doctor_id = ?", id);

        // Add new availability slots
        for (AvailabilitySlot slot : availabilitySlots) {
            Map<String, Object> params = new HashMap<>();
            params.put("doctor_id", id);
            params.put("day_of_week", slot.getDayOfWeek());
            params.put("start_time", slot.getStartTime());
            params.put("end_time", slot.getEndTime());
            jdbcInsertDoctorAvailability.execute(params);
        }
    }

    @Override
    public void changePassword(long id, String password) {
        jdbcTemplate.update("UPDATE users SET password = ? WHERE id = ?", password, id);
    }

    @Override
    public String getLanguage(long id) {
        return jdbcTemplate.query("SELECT language FROM Users WHERE id = ?", (rs, rowNum) -> rs.getString("language"), id).stream().findFirst().orElse(null);
    }

    @Override
    public void changeLanguage(long id, String language) {
        jdbcTemplate.update("UPDATE Users SET language = ? WHERE id = ?", language, id);
    }

    @Override
    public int countBySpecialty(long specialtyId) {
        return jdbcTemplate.queryForObject(
                "SELECT COUNT(*) FROM doctor_specialties WHERE specialty_id = ?",
                Integer.class,
                specialtyId
        );
    }
}
