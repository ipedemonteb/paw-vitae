package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.interfacePersistence.ClientDao;
import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.ClientService;
import ar.edu.itba.paw.models.Client;

import java.util.ArrayList;
import java.util.Optional;

public class ClientServiceImpl implements ClientService {

    private final ClientDao clientDao;
    private final AppointmentService appointmentService;

    public ClientServiceImpl(final ClientDao clientDao, final AppointmentService appointmentService) {
        this.clientDao = clientDao;
        this.appointmentService = appointmentService;
    }

    @Override
    public Client create(String email, String password, long coverageId, String coverage, String name, String phone) {
        return this.clientDao.create(email, password, coverageId, coverage, name, phone);
    }

    @Override
    public Optional<Client> findById(long id) {
        Optional<Client> client = this.clientDao.findById(id);
        client.ifPresent(value -> value.setAppointments(appointmentService.getByClientId(id).orElse(new ArrayList<>())));
        return client;
    }

    @Override
    public Optional<Client> findByEmail(String email) {
        Optional<Client> client = this.clientDao.findByEmail(email);
        client.ifPresent(value -> value.setAppointments(appointmentService.getByClientId(value.getId()).orElse(new ArrayList<>())));
        return client;
    }

}
