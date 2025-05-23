package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.interfaceServices.*;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.CoverageNotFoundException;
import ar.edu.itba.paw.models.exception.SpecialtyNotFoundException;
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
    private final SpecialtyService specialtyService;
    private final CoverageService coverageService;
    private final DoctorOfficeService doctorOfficeService;

    @Autowired
    public DoctorServiceImpl(DoctorDao doctorDao, PasswordEncoder passwordEncoder, ImageService imageService, AvailabilitySlotsService availabilitySlotsService, UserService userService,
                             SpecialtyService specialtyService, CoverageService coverageService, DoctorOfficeService doctorOfficeService) {
        this.doctorDao = doctorDao;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
        this.availabilitySlotsService = availabilitySlotsService;
        this.userService = userService;
        this.specialtyService = specialtyService;
        this.coverageService = coverageService;
        this.doctorOfficeService = doctorOfficeService;
    }

    @Transactional
    @Override
    public Doctor create(String name, String lastName, String email, String password, String phone, String language, MultipartFile image, List<Long> specialties, List<Long> coverages, List<AvailabilitySlotForm> availabilitySlots, List<DoctorOfficeForm> doctorOfficeForm) {
        LOGGER.debug("Creating doctor with name: {}, lastName: {}, email: {}, phone: {}, language: {}, specialties: {}, coverages: {}", name, lastName, email, phone, language, specialties, coverages);
        Images img = imageService.create(image);
        List<AvailabilitySlotForm> filteredSlotsForm = availabilitySlots.stream()
                .filter(slot -> slot != null && slot.getStartTime() != null && slot.getEndTime() != null)
                .toList();

        String passwordEncoded = passwordEncoder.encode(password);
        List<Specialty> specialtiesList = new ArrayList<>();
        for (Long specialtyId : specialties) {
            Specialty specialty = specialtyService.getById(specialtyId).orElseThrow(SpecialtyNotFoundException::new);
            specialtiesList.add(specialty);
        }
        List<Coverage> coveragesList = new ArrayList<>();
        for (Long coverageId : coverages) {
            Coverage coverage = coverageService.findById(coverageId).orElseThrow(CoverageNotFoundException::new);
            coveragesList.add(coverage);
        }
        Doctor doctor = this.doctorDao.create(name, lastName, email, passwordEncoded, phone, language, (img == null ? null : img.getId()), specialtiesList, coveragesList);
        List<AvailabilitySlot> filteredSlots = availabilitySlotsService.transformToAvailabilitySlots(doctor,filteredSlotsForm);
        availabilitySlotsService.create(filteredSlots);
        doctor.setAvailabilitySlots(filteredSlots);
        List<DoctorOffice> doctorOffices = doctorOfficeService.transformToDoctorOffice(doctor, doctorOfficeForm);
        doctorOfficeService.create(doctorOffices);
        doctor.setDoctorOffices(doctorOffices);
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

            doctor.setName(name);
            doctor.setLastName(lastName);
            doctor.setPhone(phone);

            List<Specialty> newSpecialties = specialtyService.getByIds(specialties);
            doctor.setSpecialtyList(newSpecialties);

            List<Coverage> newCoverages = coverageService.findByIds(coverages);
            doctor.setCoverageList(newCoverages);

            if (image != null && !image.isEmpty()) {
                Long newImage = imageService.create(image).getId();
                Images oldImage = doctor.getImageId() != null ? imageService.findById(doctor.getImageId()).orElse(null) : null;
                doctor.setImageId(newImage);
                if (oldImage != null && oldImage.getId() != -1) {
                    imageService.deleteImage(oldImage.getId());
                }
            }
        LOGGER.info("Doctor updated successfully: id={}", doctor.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public Page<Doctor> getWithFilters(long specialtyId, long coverageId, List<QueryParam> weekdays, String orderBy, String direction, int page, int pageSize) {
        List<Integer> weekdayAux;
        if (weekdays == null) {
             weekdayAux = new ArrayList<>();
        }
        else {
            weekdayAux = weekdays.stream().map(queryParam -> (int) queryParam.getValue()).toList();
        }
        List<Doctor> docs = doctorDao.getWithFilters(specialtyId, coverageId, weekdayAux, orderBy, direction, page, pageSize);
        int total = doctorDao.countWithFilters(specialtyId, coverageId, weekdayAux, orderBy, direction);
        return new Page<>(docs, page, pageSize, total);
    }

    @Transactional
    @Override
    public void UpdateDoctorRating(long id, long rating) {
        LOGGER.debug("Updating doctor rating with id {}, rating {}", id, rating);

        Doctor doctor = doctorDao.getById(id).orElseThrow(() -> new IllegalArgumentException("Doctor not found with id: " + id));
        Double currentRating = doctor.getRating();
        int ratingCount = doctor.getRatingCount() + 1;
        doctor.setRatingCount(ratingCount);
        Double newRating = (currentRating * (ratingCount - 1) + rating) / ratingCount;
        doctor.setRating(newRating);

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
