package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.interfaceServices.*;

import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.CoverageNotFoundException;
import ar.edu.itba.paw.models.exception.SpecialtyNotFoundException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DoctorServiceImpl implements DoctorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorServiceImpl.class);

    private final DoctorDao doctorDao;

    private final ImageService imageService;
    private final PasswordEncoder passwordEncoder;
    private final SpecialtyService specialtyService;
    private final CoverageService coverageService;
    private final DoctorOfficeService doctorOfficeService;
    private final UserService userService;

    @Autowired
    public DoctorServiceImpl(DoctorDao doctorDao, PasswordEncoder passwordEncoder, ImageService imageService,
                             SpecialtyService specialtyService, CoverageService coverageService, DoctorOfficeService doctorOfficeService,UserService userService) {
        this.doctorDao = doctorDao;
        this.passwordEncoder = passwordEncoder;
        this.imageService = imageService;
        this.specialtyService = specialtyService;
        this.coverageService = coverageService;
        this.doctorOfficeService = doctorOfficeService;
        this.userService = userService;
    }

    @Transactional
    @Override
    public Doctor create(String name, String lastName, String email, String password, String phone, List<Locale> locales, List<Long> specialties, List<Long> coverages ) {
        String language = locales.isEmpty() ? Locale.ENGLISH.getLanguage() : locales.getFirst().getLanguage();
        LOGGER.debug("Creating doctor with name: {}, lastName: {}, email: {}, phone: {}, language: {}, specialties: {}, coverages: {}", name, lastName, email, phone, language, specialties, coverages);

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
        //TODO: Maybe remove field imageId?
        Doctor doctor = this.doctorDao.create(name, lastName, email, passwordEncoded, phone, language, null, specialtiesList, coveragesList);
        LOGGER.info("Successfully created doctor: id={}, email={}", doctor.getId(), doctor.getEmail());
        userService.setVerificationToken(email);
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

    @Override
    public Optional<Doctor> getByIdWithAvailableOffices(long id) {
        LOGGER.debug("Getting doctor with id {} with available offices", id);
        Optional<Doctor> doctor = this.doctorDao.getByIdWithAvailableOffices(id);
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
    public void updateDoctor(Doctor doctor, String name, String lastName, String phone, List<Long> specialties, List<Long> coverages) {
        LOGGER.debug("Updating doctor with id {}, name: {}, lastName: {}, phone: {}, specialties: {}, coverages: {}", doctor.getId(), name, lastName, phone, specialties, coverages);

            doctor.setName(name);
            doctor.setLastName(lastName);
            doctor.setPhone(phone);

            List<Specialty> newSpecialties = specialtyService.getByIds(specialties);
            doctor.setSpecialtyList(newSpecialties);

            List<Coverage> newCoverages = coverageService.findByIds(coverages);
            doctor.setCoverageList(newCoverages);

        LOGGER.info("Doctor updated successfully: id={}", doctor.getId());
    }

    @Transactional
    @Override
    public void setImage(long doctorId, long imageId) {
        LOGGER.debug("Updating doctor with id {}, imageId: {}", doctorId, imageId);
        Doctor doctor = doctorDao.getById(doctorId).orElseThrow(UserNotFoundException::new);
        Images oldImage = null;
        if (doctor.getImageId() != null) {
            oldImage = imageService.findById(doctor.getImageId()).orElse(null);
        }
        doctor.setImageId(imageId);
        if (oldImage != null) {
            imageService.deleteImage(oldImage.getId());
        }
        LOGGER.info("Doctor image updated successfully: id={}", doctor.getId());
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Images> getDoctorImage(long doctorId) {
        LOGGER.debug("Getting image for doctor with id {}", doctorId);
        Optional<Doctor> doctorOpt = doctorDao.getById(doctorId);
        if (doctorOpt.isEmpty() || doctorOpt.get().getImageId() == null) {
            LOGGER.warn("No image found for doctor with id {}", doctorId);
            return Optional.empty();
        }
        return imageService.findById(doctorOpt.get().getImageId());
    }


    @Transactional(readOnly = true)
    @Override
    public Page<Doctor> getWithFilters(long specialtyId, long coverageId, List<Integer> weekdays, String keyword, String orderBy, String direction, int page, int pageSize) {
        if (weekdays == null) {
             weekdays = new ArrayList<>();
        }
        List<Doctor> docs = doctorDao.getWithFilters(specialtyId, coverageId, weekdays, keyword, orderBy, direction, page, pageSize);
        int total = doctorDao.countWithFilters(specialtyId, coverageId, weekdays, keyword, orderBy, direction);
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
    public long getAllDoctorsDisplayCount() {
        return doctorDao.countAll();
    }

    @Transactional(readOnly = true)
    @Override
    public String search(String keyword, int results) {
        LOGGER.debug("Searching for doctors with keyword: {}", keyword);
        List<Doctor> doctors = doctorDao.search(keyword, results);
        if (doctors.isEmpty()) {
            LOGGER.warn("No doctors found for keyword: {}", keyword);
            return "{\"doctors\": []}";
        }
        Map<String, Object> response = new HashMap<>();
        response.put("doctors", doctors);
        return JsonUtils.toJson(response, Doctor.Views.Public.class);
    }

    @Override
    public void setResetPasswordToken(String email) {
        userService.setResetPasswordToken(email);
    }
    @Override
    public void changePassword(long userId,String password) {
        userService.changePassword(userId,password);
    }
}
