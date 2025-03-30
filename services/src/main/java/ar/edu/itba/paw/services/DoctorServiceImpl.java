package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.DoctorService;

import ar.edu.itba.paw.models.Client;
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
    public Doctor create(String name, String email, String password, String phone, List<String> specialty, Array coverages) {
        return doctorDao.create(name, email, password, phone, specialty,coverages);
    }

    @Override
    public Optional<Doctor> findById(long id) {
        Optional<Doctor> doctor = doctorDao.findById(id);
        doctor.ifPresent(value -> value.setAppointments(appointmentService.getByDoctorId(id).orElse(new ArrayList<>())));
        return doctor;
    }

    @Override
    public Optional<Doctor> findByEmail(String email) {
        Optional<Doctor> doctor = this.doctorDao.findByEmail(email);
        doctor.ifPresent(value -> value.setAppointments(appointmentService.getByDoctorId(value.getId()).orElse(new ArrayList<>())));
        return doctor;
    }
}
