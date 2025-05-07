package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.interfaceServices.*;

import ar.edu.itba.paw.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class DoctorServiceImpl implements DoctorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorServiceImpl.class);

    private final DoctorDao doctorDao;

    private final SpecialtyService ss;
    private final CoverageService cs;
    private final AppointmentService as;
    private final ImageService is;
    private final AvailabilitySlotsService ass;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DoctorServiceImpl(DoctorDao doctorDao, SpecialtyService ss, CoverageService cs, PasswordEncoder passwordEncoder, AppointmentService as, ImageService is, AvailabilitySlotsService ass) {
        this.doctorDao = doctorDao;
        this.ss = ss;
        this.cs = cs;
        this.passwordEncoder = passwordEncoder;
        this.as = as;
        this.is = is;
        this.ass = ass;
    }

    private void populateDoctorDetails(Doctor doctor) {
        long id = doctor.getId();
        populateDoctorDetails(doctor, ss.getByDoctorId(id), cs.findByDoctorId(id), ass.getAvailabilityByDoctorId(id));
    }

    private void populateDoctorDetails(Doctor doctor, List<Specialty> specialties, List<Coverage> coverages, List<AvailabilitySlot> availabilitySlots) {
        doctor.setCoverageList(coverages);
        doctor.setSpecialtyList(specialties);
        doctor.setAvailabilitySlots(availabilitySlots);
    }

    private void populateDoctorDetails(List<Doctor> doctors) {
        doctors.forEach(this::populateDoctorDetails);
    }

    @Transactional
    @Override
    public Doctor create(String name, String lastName, String email, String password, String phone, String language, MultipartFile image, List<String> specialties, List<String> coverages, List<AvailabilitySlot> availabilitySlots) {
        List<Coverage> coverageList = cs.findByIds(coverages.stream().map(Long::valueOf).collect(Collectors.toList()));
        List<Specialty> specialtyList = ss.getByIds(specialties.stream().map(Long::valueOf).collect(Collectors.toList()));
        Images img = is.create(image);
        List<AvailabilitySlot> filteredSlots = availabilitySlots.stream()
                .filter(slot -> slot != null && slot.getStartTime() != null && slot.getEndTime() != null)
                .toList();
        Doctor doctor = this.doctorDao.create(
                name, lastName, email, passwordEncoder.encode(password), phone, language, (img == null ? null : img.getId()), specialtyList, coverageList
        );
        ass.create(doctor.getId(), filteredSlots);
        populateDoctorDetails(doctor, specialtyList, coverageList, filteredSlots);
        LOGGER.info("Doctor creado exitosamente: id={}, email={}", doctor.getId(), doctor.getEmail());
        return doctor;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Doctor> getById(long id) {
        Optional<Doctor> doctor = this.doctorDao.getById(id);
        if (doctor.isEmpty()) {
            LOGGER.warn("No doctor found with id {}", id);
        }
        doctor.ifPresent(this::populateDoctorDetails);
        return doctor;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Doctor> getBySpecialty(long specialtyId, int page, int pageSize) {
        int total = doctorDao.countBySpecialty(specialtyId);
        List<Doctor> docs = doctorDao.getBySpecialty(specialtyId, page, pageSize);
        populateDoctorDetails(docs);
        return new Page<>(docs, page, pageSize, total);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Doctor> getBySpecialtyWithAppointments(long specialtyId, int page, int pageSize) {
        List<Doctor> docs = doctorDao.getBySpecialty(specialtyId, page, pageSize);
        int total = doctorDao.countBySpecialty(specialtyId);
        populateDoctorDetails(docs);
        docs.forEach(doctor -> doctor.setAppointments(as.getByDoctorId(doctor.getId())));
        return new Page<>(docs, page, pageSize, total);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Doctor> getByEmail(String email) {
        Optional<Doctor> doctor = this.doctorDao.getByEmail(email);
        doctor.ifPresent(this::populateDoctorDetails);
        return doctor;
    }

    @Transactional
    @Override
    public void updateDoctor(long id, String name, String lastName, String phone, List<String> specialties, List<String> coverages) {
        Doctor currentDoctor = getById(id)
                .orElseThrow(() -> {
                    LOGGER.warn("Attempted to update non-existent doctor with id={}", id);
                    return new IllegalArgumentException("Doctor not found");
                });
        List<Coverage> coverageList = cs.findByIds(coverages.stream().map(Long::valueOf).collect(Collectors.toList()));
        List<Specialty> specialtyList = ss.getByIds(specialties.stream().map(Long::valueOf).collect(Collectors.toList()));

        boolean hasChanged = !currentDoctor.getName().equals(name)
                || !currentDoctor.getLastName().equals(lastName)
                || !currentDoctor.getPhone().equals(phone)
                || !currentDoctor.getSpecialtyList().equals(specialtyList)
                || !currentDoctor.getCoverageList().equals(coverageList);

        if (hasChanged) {
            doctorDao.updateDoctor(id, name, lastName, phone, specialtyList, coverageList);
            LOGGER.info("Doctor updated successfully: id={}", id);
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Doctor> getByIdWithAppointments(long id) {
        Optional<Doctor> doctor = getById(id);
        doctor.ifPresent(d -> {
            populateDoctorDetails(d);
            d.setAppointments(as.getByDoctorId(id));
        });
        return doctor;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Doctor> getWithFilters(Long specialtyId, Long coverageId, List<Integer> weekdays, String orderBy, String direction, int page, int pageSize) {
        if (weekdays == null) {
            weekdays = new ArrayList<>();
        }
        List<Doctor> docs = doctorDao.getWithFilters(specialtyId, coverageId, weekdays, orderBy, direction, page, pageSize);
        int total = doctorDao.countWithFilters(specialtyId, coverageId, weekdays, orderBy, direction);
        populateDoctorDetails(docs);
        return new Page<>(docs, page, pageSize, total);
    }

    @Transactional
    @Override
    public void UpdateDoctorRating(long id, long rating) {
        doctorDao.UpdateDoctorRating(id, rating);
        LOGGER.info("Rating updated for doctor with id={}, rating={}", id, rating);
    }

    @Transactional(readOnly = true)
    public Optional<Doctor> getByResetToken(String token) {
        return doctorDao.getByResetToken(token);
    }

    @Transactional(readOnly = true)
    public Optional<Doctor> getByVerificationToken(String token) {
        return doctorDao.getByVerificationToken(token);
    }

}
