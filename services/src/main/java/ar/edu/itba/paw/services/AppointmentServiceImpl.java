package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.models.Appointment;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class AppointmentServiceImpl implements AppointmentService {

    private final AppointmentDao appointmentDao;

    public AppointmentServiceImpl(AppointmentDao appointmentDao) {
        this.appointmentDao = appointmentDao;
    }

    @Override
    public Appointment create(long clientId, long doctorId, Date startDate, String status, String reason) {
        return appointmentDao.create(clientId, doctorId, startDate, status, reason);
    }

    @Override
    public Optional<List<Appointment>> getByClientId(long clientId) {
        return appointmentDao.getByClientId(clientId);
    }

    @Override
    public Optional<List<Appointment>> getByDoctorId(long doctorId) {
        return appointmentDao.getByDoctorId(doctorId);
    }
}
