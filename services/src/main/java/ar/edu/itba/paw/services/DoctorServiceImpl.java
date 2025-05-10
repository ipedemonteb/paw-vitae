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

@Service
public class DoctorServiceImpl implements DoctorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorServiceImpl.class);

    private final DoctorDao doctorDao;

    private final AppointmentService as;
    private final ImageService is;
    private final AvailabilitySlotsService ass;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    public DoctorServiceImpl(DoctorDao doctorDao, PasswordEncoder passwordEncoder, AppointmentService as, ImageService is, AvailabilitySlotsService ass, UserService userService) {
        this.doctorDao = doctorDao;
        this.passwordEncoder = passwordEncoder;
        this.as = as;
        this.is = is;
        this.ass = ass;
        this.userService = userService;
    }

    @Transactional
    @Override
    public Doctor create(String name, String lastName, String email, String password, String phone, String language, MultipartFile image, List<Long> specialties, List<Long> coverages, List<AvailabilitySlot> availabilitySlots) {
        Images img = is.create(image);
        List<AvailabilitySlot> filteredSlots = availabilitySlots.stream()
                .filter(slot -> slot != null && slot.getStartTime() != null && slot.getEndTime() != null)
                .toList();

        long id = userService.create(name, lastName, email, passwordEncoder.encode(password), phone, language);
        Doctor doctor = this.doctorDao.create(id, name, lastName, email, passwordEncoder.encode(password), phone, language, (img == null ? null : img.getId()), specialties, coverages);
        ass.create(id, filteredSlots);
        doctor.setAvailabilitySlots(filteredSlots);
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
        return doctor;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Doctor> getBySpecialty(long specialtyId, int page, int pageSize) {
        int total = doctorDao.countBySpecialty(specialtyId);
        List<Doctor> docs = doctorDao.getBySpecialty(specialtyId, page, pageSize);
        return new Page<>(docs, page, pageSize, total);
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Doctor> getBySpecialtyWithAppointments(long specialtyId, int page, int pageSize) {
        List<Doctor> docs = doctorDao.getBySpecialty(specialtyId, page, pageSize);
        int total = doctorDao.countBySpecialty(specialtyId);
        docs.forEach(doctor -> doctor.setAppointments(as.getByDoctorId(doctor.getId())));
        return new Page<>(docs, page, pageSize, total);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Doctor> getByEmail(String email) {
        return this.doctorDao.getByEmail(email);
    }

    @Transactional
    @Override
    public void updateDoctor(Doctor doctor, String name, String lastName, String phone, List<Long> specialties, List<Long> coverages, MultipartFile image) {
        List<Long> specialtyIds = doctor.getSpecialtyList()
                .stream()
                .map(Specialty::getId)
                .toList();
        List<Long> coverageIds = doctor.getCoverageList()
                .stream()
                .map(Coverage::getId)
                .toList();


        boolean hasChangedDoctor = !specialtyIds.equals(specialties) //TODO is the equals of lists well implemented in java?
                || !coverageIds.equals(coverages);
        boolean hasChangedUser = !doctor.getName().equals(name)
                || !doctor.getLastName().equals(lastName)
                || !doctor.getPhone().equals(phone);
        if (hasChangedDoctor) {
            doctorDao.updateDoctor(doctor.getId(), specialties, coverages);
            LOGGER.info("Doctor updated successfully: id={}", doctor.getId());
        }
        if (hasChangedUser) {
            userService.update(doctor.getId(), name, lastName, phone);
            LOGGER.info("User updated successfully: id={}", doctor.getId());
        }
        if (image != null && !image.isEmpty()) {
            Images img = is.create(image);
            doctorDao.updateImage(doctor.getId(), img.getId());
            if (doctor.getImageId() != -1) {
                is.deleteImage(doctor.getId());
            }
            LOGGER.info("Doctor image updated successfully: id={}", doctor.getId());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Doctor> getByIdWithAppointments(long id) {
        Optional<Doctor> doctor = getById(id);
        doctor.ifPresent(d -> d.setAppointments(as.getByDoctorId(id)));
        return doctor;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Doctor> getWithFilters(Long specialtyId, Long coverageId, List<Integer> weekdays, String orderBy, String direction, int page, int pageSize) {
        if (weekdays == null) {
            weekdays = new ArrayList<>();
        }
        if (page < 1) {
            page = 1;
        }
        List<Doctor> docs = doctorDao.getWithFilters(specialtyId, coverageId, weekdays, orderBy, direction, page, pageSize);
        int total = doctorDao.countWithFilters(specialtyId, coverageId, weekdays, orderBy, direction);
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
