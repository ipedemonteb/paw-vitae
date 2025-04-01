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
    private final AppointmentService appointmentService;

    public DoctorServiceImpl(final DoctorDao doctorDao, final AppointmentService appointmentService) {
        this.doctorDao = doctorDao;
        this.appointmentService = appointmentService;
    }

    @Override
    public Doctor create(String name, String lastName, String email, String password, String phone, List<String> specialty, List<Coverage> coverages) {
        return null;
    }

    @Override
    public Optional<Doctor> getById(long id) {
        return Optional.empty();
    }

    @Override
    public Optional<Doctor> getByEmail(String email) {
        return Optional.empty();
    }
}
