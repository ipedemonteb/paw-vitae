package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.interfacePersistence.ClientDao;
import ar.edu.itba.paw.interfaceServices.ClientService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Client;

import java.util.ArrayList;
import java.util.Optional;

public class ClientServiceImpl implements ClientService {

    private final ClientDao clientDao;
    private final AppointmentDao appointmentDao;

    public ClientServiceImpl(final ClientDao clientDao, final AppointmentDao appointmentDao) {
        this.clientDao = clientDao;
        this.appointmentDao = appointmentDao;
    }

    @Override
    public Client create(String email, String password, long coverageId, String coverage, String name, String phone) {
        return this.clientDao.create(email, password, coverageId, coverage, name, phone);
    }

    @Override
    public Optional<Client> findById(long id) {
        Optional<Client> client = this.clientDao.findById(id);
        client.ifPresent(value -> value.setAppointments(appointmentDao.getByClientId(id).orElse(new ArrayList<>())));
        return client;
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        Optional<Client> client = this.clientDao.findByEmail(email);
        client.ifPresent(value -> value.setAppointments(appointmentDao.getByClientId(value.getId()).orElse(new ArrayList<>())));
        return client;
    }

}
