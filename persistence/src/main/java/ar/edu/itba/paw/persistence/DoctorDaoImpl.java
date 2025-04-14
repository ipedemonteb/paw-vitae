package ar.edu.itba.paw.persistence;

import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Specialty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.util.*;

@Repository
public class DoctorDaoImpl implements DoctorDao {

    private JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsertUser;
    private final SimpleJdbcInsert jdbcInsertDoctor;
    private final SimpleJdbcInsert jdbcInsertDoctorCoverage;
    private final SimpleJdbcInsert jdbcInsertDoctorSpecialty;


    private final RowMapper<Doctor> ROW_MAPPER = (rs, rowNum) -> new Doctor(
            rs.getString("name"),
            rs.getLong("id"),
            rs.getString("last_name"),
            rs.getString("email"),
            rs.getString("password"),
            rs.getString("phone"),
            new ArrayList<>(),
            new ArrayList<>()
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
    }


    @Override
    public Doctor create(String name, String lastName, String email, String password, String phone, List<Specialty> specialties, List<Coverage> coverages) {
        final Map<String, Object> argsUser = new HashMap<>();
        argsUser.put("name", name);
        argsUser.put("last_name", lastName);
        argsUser.put("email", email);
        argsUser.put("password", password);
        argsUser.put("phone", phone);
        final Number docId = jdbcInsertUser.executeAndReturnKey(argsUser);

        final Map<String, Object> argsDoctor = new HashMap<>();
        argsDoctor.put("doctor_id", docId);
        jdbcInsertDoctor.execute(argsDoctor);


        for(Specialty specialty : specialties) {
            final Map<String, Object> argsDoctorSpecialty = new HashMap<>();
            argsDoctorSpecialty.put("doctor_id", docId);
            argsDoctorSpecialty.put("specialty_id", specialty.getId());
            jdbcInsertDoctorSpecialty.execute(argsDoctorSpecialty);
        }


        final Map<String, Object> argsDoctorCoverage = new HashMap<>();
        for (Coverage coverage : coverages) {
            argsDoctorCoverage.put("doctor_id", docId);
            argsDoctorCoverage.put("coverage_id", coverage.getId());
            jdbcInsertDoctorCoverage.execute(argsDoctorCoverage);
        }



        return new Doctor(
                name,
                docId.longValue(),
                lastName,
                email,
                password,
                phone,
                specialties,
                coverages
        );
    }

    @Override
    public Optional<Doctor> getById(long id) {
        Optional<Doctor> doc = jdbcTemplate.query("SELECT * FROM users JOIN doctors ON users.id = doctors.doctor_id WHERE users.id = ?",ROW_MAPPER,id).stream().findFirst();
        doc.ifPresent(value -> value.setCoverageList(jdbcTemplate.query("SELECT * FROM doctor_coverages JOIN coverages ON doctor_coverages.coverage_id = coverages.id WHERE doctor_coverages.doctor_id = ?", (rs, rowNum) -> new Coverage(rs.getLong("id"), rs.getString("coverage_name")), id)));
        doc.ifPresent(value -> value.setSpecialtyList(jdbcTemplate.query("SELECT * FROM doctor_specialties JOIN specialties ON doctor_specialties.specialty_id = specialties.id WHERE doctor_specialties.doctor_id = ?", (rs, rowNum) -> new Specialty(rs.getLong("id"), rs.getString("key")), id)));
        return doc;
    }

    @Override
    public Optional<Doctor> getByEmail(String email) {
       Optional<Doctor> doc =  jdbcTemplate.query("SELECT * FROM users JOIN doctors ON users.id = doctors.doctor_id WHERE users.email= ?",ROW_MAPPER,email).stream().findFirst();
       Long id = doc.map(Doctor::getId).orElse(null);
        doc.ifPresent(value -> value.setCoverageList(jdbcTemplate.query("SELECT * FROM doctor_coverages JOIN coverages ON doctor_coverages.coverage_id = coverages.id WHERE doctor_coverages.doctor_id = ?", (rs, rowNum) -> new Coverage(rs.getLong("id"), rs.getString("coverage_name")), id)));
        doc.ifPresent(value -> value.setSpecialtyList(jdbcTemplate.query("SELECT * FROM doctor_specialties JOIN specialties ON doctor_specialties.specialty_id = specialties.id WHERE doctor_specialties.doctor_id = ?", (rs, rowNum) -> new Specialty(rs.getLong("id"), rs.getString("key")), id)));
        return doc;
    }

    @Override
    public List<Doctor> getAll() {
        List<Doctor> doctors = jdbcTemplate.query("SELECT * FROM users JOIN doctors ON users.id = doctors.doctor_id", ROW_MAPPER);
        for (Doctor doctor : doctors) {
            doctor.setCoverageList(jdbcTemplate.query("SELECT * FROM doctor_coverages JOIN coverages ON doctor_coverages.coverage_id = coverages.id WHERE doctor_coverages.doctor_id = ?", (rs, rowNum) -> new Coverage(rs.getLong("id"), rs.getString("coverage_name")), doctor.getId()));
            doctor.setSpecialtyList(jdbcTemplate.query("SELECT * FROM doctor_specialties JOIN specialties ON doctor_specialties.specialty_id = specialties.id WHERE doctor_specialties.doctor_id = ?", (rs, rowNum) -> new Specialty(rs.getLong("id"), rs.getString("key")), doctor.getId()));
        }
        return doctors;
    }

    @Override
    public List<Doctor> getByIds(Set<Long> ids) {
        return jdbcTemplate.query("SELECT * FROM Users u JOIN Doctors d ON d.doctor_id = u.id  WHERE u.id IN (" + String.join(",", Collections.nCopies(ids.size(), "?")) + ")", ROW_MAPPER, ids.toArray());
    }

    @Override
    public List<Doctor> getBySpecialty(String specialty) {
        List<Doctor> doctors = jdbcTemplate.query("SELECT * FROM users JOIN doctors ON users.id = doctors.doctor_id JOIN doctor_specialties ON doctors.doctor_id = doctor_specialties.doctor_id JOIN specialties ON doctor_specialties.specialty_id = specialties.id WHERE specialties.key = ?", new Object[]{specialty}, ROW_MAPPER);
        for (Doctor doctor : doctors) {
            doctor.setCoverageList(jdbcTemplate.query("SELECT * FROM doctor_coverages JOIN coverages ON doctor_coverages.coverage_id = coverages.id WHERE doctor_coverages.doctor_id = ?", (rs, rowNum) -> new Coverage(rs.getLong("id"), rs.getString("coverage_name")), doctor.getId()));
            doctor.setSpecialtyList(jdbcTemplate.query("SELECT * FROM doctor_specialties JOIN specialties ON doctor_specialties.specialty_id = specialties.id WHERE doctor_specialties.doctor_id = ?", (rs, rowNum) -> new Specialty(rs.getLong("id"), rs.getString("key")), doctor.getId()));
        }
        return doctors;
    }

    @Override
    public void updateDoctor(long id, String name, String lastName, String email, String phone, List<Specialty> specialties, List<Coverage> coverages) {
        Doctor currentDoctor = getById(id).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        String updatedName = (name != null) ? name : currentDoctor.getName();
        String updatedLastName = (lastName != null) ? lastName : currentDoctor.getLastName();
        String updatedEmail = (email != null) ? email : currentDoctor.getEmail();
        String updatedPhone = (phone != null) ? phone : currentDoctor.getPhone();
        List<Specialty> updatedSpecialties = (specialties != null) ? specialties : currentDoctor.getSpecialtyList();
        List<Coverage> updatedCoverages = (coverages != null) ? coverages : currentDoctor.getCoverageList();


        jdbcTemplate.update("UPDATE users SET name = ?, last_name = ?, email = ?, phone = ? WHERE id = ?",
                updatedName, updatedLastName, updatedEmail, updatedPhone, id);

        jdbcTemplate.update("DELETE FROM doctor_specialties WHERE doctor_id = ?", id);
        for (Specialty specialty : updatedSpecialties) {
            jdbcTemplate.update("INSERT INTO doctor_specialties (doctor_id, specialty_id) VALUES (?, ?)", id, specialty.getId());
        }

        jdbcTemplate.update("DELETE FROM doctor_coverages WHERE doctor_id = ?", id);
        for (Coverage coverage : updatedCoverages) {
            jdbcTemplate.update("INSERT INTO doctor_coverages (doctor_id, coverage_id) VALUES (?, ?)", id, coverage.getId());
        }
    }

}
