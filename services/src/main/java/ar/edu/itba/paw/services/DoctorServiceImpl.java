package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.interfaceServices.DoctorService;

import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Specialty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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


    @Override
    public Doctor create(String name, String lastName, String email, String password, String phone, List<String> specialties, List<String> coverages) {
        List<Coverage> coverageList = new ArrayList<>();
        List<Specialty> specialtyList = new ArrayList<>();
        coverages.forEach(coverage -> coverageList.add(cs.findById(Long.parseLong(coverage)).orElse(null)));
        specialties.forEach(specialty -> specialtyList.add(ss.getById(Long.parseLong(specialty)).orElse(null)));
        return this.doctorDao.create(name, lastName, email, passwordEncoder.encode(password), phone, specialtyList, coverageList);
    }

    @Override
    public Optional<Doctor> getById(long id) {
        return this.doctorDao.getById(id);
    }

    @Override
    public List<Doctor> getBySpecialty(String specialty) { return this.doctorDao.getBySpecialty(specialty); }

    @Override
    public Optional<Doctor> getByEmail(String email) {
        return this.doctorDao.getByEmail(email);
    }

}
