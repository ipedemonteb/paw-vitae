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

    public ClientServiceImpl(ClientDao clientDao) {
        this.clientDao = clientDao;
    }

    @Override
    public Optional<Client> getById(long id) {
        return clientDao.getById(id);
    }

    @Override
    public Client create(String name, String lastName, String email, String password, String phone, Coverage coverage) {
        return this.clientDao.create(name, lastName, email, password, phone, coverage);
    }

}
