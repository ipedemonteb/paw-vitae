package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.interfaceServices.DoctorService;

import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorDao doctorDao;

    private final SpecialtyService ss;
    private final CoverageService cs;


    private final PasswordEncoder passwordEncoder;

    @Autowired
    public DoctorServiceImpl(DoctorDao doctorDao, SpecialtyService ss, CoverageService cs, PasswordEncoder passwordEncoder) {
        this.doctorDao = doctorDao;
        this.ss = ss;
        this.cs = cs;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    @Override
    public Doctor create(String name, String lastName, String email, String password, String phone, String language, List<String> specialties, List<String> coverages, List<AvailabilitySlot> availabilitySlots) {
        List<Coverage> coverageList = new ArrayList<>();
        List<Specialty> specialtyList = new ArrayList<>();
        coverages.forEach(coverage -> coverageList.add(cs.findById(Long.parseLong(coverage)).orElse(null)));
        specialties.forEach(specialty -> specialtyList.add(ss.getById(Long.parseLong(specialty)).orElse(null)));
        return this.doctorDao.create(name, lastName, email, passwordEncoder.encode(password), phone, language, specialtyList, coverageList, availabilitySlots);
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
    public Optional<Doctor> getByEmail(String email) {
        return this.doctorDao.getByEmail(email);
    }

    @Override
    public List<Doctor> getAll() {
        return this.doctorDao.getAll();
    }

    @Transactional
    @Override
    public void updateDoctor(long id, String name, String lastName, String phone, List<String> specialties, List<String> coverages, List<AvailabilitySlot> availabilitySlots) {
        List<Coverage> coverageList = new ArrayList<>();
        List<Specialty> specialtyList = new ArrayList<>();
        coverages.forEach(coverage -> coverageList.add(cs.findById(Long.parseLong(coverage)).orElse(null))); //TODO create query to fetch list instead of individually
        specialties.forEach(specialty -> specialtyList.add(ss.getById(Long.parseLong(specialty)).orElse(null))); //TODO create query to fetch list instead of individually
        doctorDao.updateDoctor(id, name, lastName, phone, specialtyList, coverageList, availabilitySlots);
    }

    @Transactional
    @Override
    public void updateDoctorAvailability(long id, List<AvailabilitySlot> availabilitySlots) {
        doctorDao.updateDoctorAvailability(id, availabilitySlots);
    }

    @Override
    public Optional<Doctor> getDoctorWithAppointments(long id) {
        return doctorDao.getDoctorWithAppointments(id);
    }

}
