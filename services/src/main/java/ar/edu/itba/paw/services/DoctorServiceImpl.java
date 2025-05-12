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
import org.springframework.util.NumberUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class DoctorServiceImpl implements DoctorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorServiceImpl.class);

    private final DoctorDao doctorDao;

    private final ImageService imageService;
    private final AvailabilitySlotsService availabilitySlotsService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Autowired
    public DoctorServiceImpl(DoctorDao doctorDao, PasswordEncoder passwordEncoder, ImageService imageService, AvailabilitySlotsService availabilitySlotsService, UserService userService) {
        this.doctorDao = doctorDao;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
        this.availabilitySlotsService = availabilitySlotsService;
        this.userService = userService;
    }

    @Transactional
    @Override
    public Doctor create(String name, String lastName, String email, String password, String phone, String language, MultipartFile image, List<Long> specialties, List<Long> coverages, List<AvailabilitySlot> availabilitySlots) {
        LOGGER.debug("Creating doctor with name: {}, lastName: {}, email: {}, phone: {}, language: {}, specialties: {}, coverages: {}", name, lastName, email, phone, language, specialties, coverages);
        Images img = imageService.create(image);
        List<AvailabilitySlot> filteredSlots = availabilitySlots.stream()
                .filter(slot -> slot != null && slot.getStartTime() != null && slot.getEndTime() != null)
                .toList();
        String passwordEncoded = passwordEncoder.encode(password);
        long id = userService.create(name, lastName, email, passwordEncoded, phone, language);
        Doctor doctor = this.doctorDao.create(id, name, lastName, email, passwordEncoded, phone, language, (img == null ? null : img.getId()), specialties, coverages);
        availabilitySlotsService.create(id, filteredSlots);
        doctor.setAvailabilitySlots(filteredSlots);
        LOGGER.info("Successfully created doctor: id={}, email={}", doctor.getId(), doctor.getEmail());
        return doctor;
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Doctor> getById(long id) {
        LOGGER.debug("Getting doctor with id {}", id);
        Optional<Doctor> doctor = this.doctorDao.getById(id);
        if (doctor.isEmpty()) {
            LOGGER.warn("No doctor found with id {}", id);
        }
        return doctor;
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Doctor> getBySpecialty(long specialtyId, int page, int pageSize) {
        LOGGER.debug("Getting doctors with special id {}", specialtyId);
        int total = doctorDao.countBySpecialty(specialtyId);
        List<Doctor> docs = doctorDao.getBySpecialty(specialtyId, page, pageSize);
        return new Page<>(docs, page, pageSize, total);
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Doctor> getByEmail(String email) {
        LOGGER.debug("Getting doctor with email {}", email);
        return this.doctorDao.getByEmail(email);
    }

    @Transactional
    @Override
    public void updateDoctor(Doctor doctor, String name, String lastName, String phone, List<Long> specialties, List<Long> coverages, MultipartFile image) {
        LOGGER.debug("Updating doctor with id {}, name: {}, lastName: {}, phone: {}, specialties: {}, coverages: {}", doctor.getId(), name, lastName, phone, specialties, coverages);
        List<Long> specialtyIds = doctor.getSpecialtyList()
                .stream()
                .map(Specialty::getId)
                .toList();
        List<Long> coverageIds = doctor.getCoverageList()
                .stream()
                .map(Coverage::getId)
                .toList();

        boolean hasChangedDoctor = specialtyIds.size() != specialties.size()
                || !specialtyIds.stream().sorted().toList().equals(specialties.stream().sorted().toList())
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
            Images img = imageService.create(image);
            doctorDao.updateImage(doctor.getId(), img.getId());
            if (doctor.getImageId() != -1) {
                imageService.deleteImage(doctor.getId());
            }
            LOGGER.info("Doctor image updated successfully: id={}", doctor.getId());
        }
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Doctor> getWithFilters(long specialtyId, long coverageId, List<Integer> weekdays, String orderBy, String direction, int page, int pageSize) {
        if (weekdays == null) {
            weekdays = new ArrayList<>();
        }
        List<Doctor> docs = doctorDao.getWithFilters(specialtyId, coverageId, weekdays, orderBy, direction, page, pageSize);
        int total = doctorDao.countWithFilters(specialtyId, coverageId, weekdays, orderBy, direction);
        return new Page<>(docs, page, pageSize, total);
    }

    @Transactional
    @Override
    public void UpdateDoctorRating(long id, long rating) {
        LOGGER.debug("Updating doctor rating with id {}, rating {}", id, rating);
        doctorDao.UpdateDoctorRating(id, rating);
        LOGGER.info("Rating updated for doctor with id={}, rating={}", id, rating);
    }

    @Transactional(readOnly = true)
    @Override
    public String getAllDoctorsDisplayCount() {
        Integer count = doctorDao.countAll();
        if (count < 10000) {
            return String.valueOf(count);
        } else {
            return String.valueOf(count / 1000) + "k+";
        }
    }
}
