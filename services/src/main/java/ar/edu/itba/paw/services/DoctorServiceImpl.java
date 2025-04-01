package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.DoctorService;

import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Doctor;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DoctorServiceImpl implements DoctorService {

    private final DoctorDao doctorDao;

    public DoctorServiceImpl(final DoctorDao doctorDao) {
        this.doctorDao = doctorDao;
    }

    @Override
    public Doctor create(String name, String lastName, String email, String password, String phone, List<String> specialty, List<Coverage> coverages) {
        return this.doctorDao.create(name, lastName, email, password, phone, specialty, coverages);
    }

    @Override
    public Optional<Doctor> getById(long id) {
        return this.doctorDao.getById(id);
    }

}
