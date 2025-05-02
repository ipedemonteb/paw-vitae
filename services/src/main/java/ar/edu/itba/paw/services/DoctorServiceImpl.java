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

    Logger LOGGER = LoggerFactory.getLogger(DoctorServiceImpl.class);

    private final DoctorDao doctorDao;

    private final SpecialtyService ss;
    private final CoverageService cs;
    private final AppointmentService as;
    private final ImageService is;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DoctorServiceImpl(DoctorDao doctorDao, SpecialtyService ss, CoverageService cs, PasswordEncoder passwordEncoder, AppointmentService as,ImageService is) {
        this.doctorDao = doctorDao;
        this.ss = ss;
        this.cs = cs;
        this.passwordEncoder = passwordEncoder;
        this.as = as;
        this.is = is;
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
                name, lastName, email, passwordEncoder.encode(password), phone, language,(img == null ? -1 : img.getId()), specialtyList, coverageList, filteredSlots
        );

        LOGGER.debug("Doctor creado exitosamente: id={}, email={}", doctor.getId(), doctor.getEmail());

        return doctor;
    }

    @Override
    public Optional<Doctor> getById(long id) {
        return this.doctorDao.getById(id);
    }

    @Override
    public Page<Doctor> getBySpecialty(long specialtyId, int page, int pageSize) {
        int total = doctorDao.countBySpecialty(specialtyId);
        List<Doctor> docs = doctorDao.getBySpecialty(specialtyId, page, pageSize);
        return new Page<>(docs, page, pageSize, total);
    }

    @Override
    public Page<Doctor> getBySpecialtyWithAppointments(long specialtyId, int page, int pageSize) {
        List<Doctor> docs = doctorDao.getBySpecialty(specialtyId, page, pageSize);
        int total = doctorDao.countBySpecialty(specialtyId);
        docs.forEach(doctor -> doctor.setAppointments(as.getByDoctorId(doctor.getId())));
        return new Page<>(docs, page, pageSize, total);
    }

    @Override
    public Optional<Doctor> getByEmail(String email) {
        return this.doctorDao.getByEmail(email);
    }

    @Transactional
    @Override
    public void updateDoctor(long id, String name, String lastName, String phone, List<String> specialties, List<String> coverages) {
        Doctor currentDoctor = getById(id).orElseThrow(() -> new IllegalArgumentException("Doctor not found"));

        List<Coverage> coverageList = cs.findByIds(coverages.stream().map(Long::valueOf).collect(Collectors.toList()));
        List<Specialty> specialtyList = ss.getByIds(specialties.stream().map(Long::valueOf).collect(Collectors.toList()));

        boolean hasChanged = !currentDoctor.getName().equals(name)
                || !currentDoctor.getLastName().equals(lastName)
                || !currentDoctor.getPhone().equals(phone)
                || !currentDoctor.getSpecialtyList().equals(specialtyList)
                || !currentDoctor.getCoverageList().equals(coverageList);

        if (hasChanged) {
            doctorDao.updateDoctor(id, name, lastName, phone, specialtyList, coverageList);
            LOGGER.debug("Doctor actualizado exitosamente: id={}, nombre={}, apellido={}", id, name, lastName);
        }
    }

    @Transactional
    @Override
    public void updateDoctorAvailability(long id, List<AvailabilitySlot> availabilitySlots) {
        List<AvailabilitySlot> filteredSlots = availabilitySlots.stream()
                .filter(slot -> slot != null && slot.getStartTime() != null && slot.getEndTime() != null)
                .toList();
        doctorDao.updateDoctorAvailability(id, filteredSlots);
        LOGGER.debug("Disponibilidad actualizada para el doctor con id={}, slots={}", id, availabilitySlots.size()); //Only log the new size, if the user has many, then it might be too heavy
    }

    @Override
    public Optional<Doctor> getByIdWithAppointments(long id) {
        Optional<Doctor> doctor = doctorDao.getById(id);
        doctor.ifPresent(d -> d.setAppointments(as.getByDoctorId(id)));
        return doctor;
    }

    @Override
    public Page<Doctor> getWithFilters(Long specialtyId, Long coverageId, List<Integer> weekdays, String orderBy, String direction, int page, int pageSize) {
        if (weekdays == null) {
            weekdays = new ArrayList<>();
        }
        List<Doctor> docs = doctorDao.getWithFilters(specialtyId, coverageId, weekdays, orderBy, direction, page, pageSize);
        int total = doctorDao.countWithFilters(specialtyId, coverageId, weekdays, orderBy, direction);
        return new Page<>(docs, page, pageSize, total);
    }
    @Transactional
    @Override
    public void UpdateDoctorRating(long id, double rating) {
        doctorDao.UpdateDoctorRating(id, rating);
        LOGGER.debug("Rating actualizado para el doctor con id={}, rating={}", id, rating);
    }
}
