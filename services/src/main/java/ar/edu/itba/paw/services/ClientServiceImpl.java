package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.interfacePersistence.ClientDao;
import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.ClientService;
import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.Coverage;

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
    public Optional<Client> getById(long id) {
        return clientDao.getById(id);
    }

    @Override
    public Optional<Client> getByEmail(String email) {
        return this.clientDao.getByEmail(email);
    }

    @Override
    public Client create(String name, String lastName, String email, String password, String phone, Coverage coverage) {
        return this.clientDao.create(name, lastName, email, password, phone, coverage);
    }

}
