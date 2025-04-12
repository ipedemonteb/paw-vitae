package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.AppointmentDao;
import ar.edu.itba.paw.interfacePersistence.ClientDao;
import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.ClientService;
import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.Coverage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientDao clientDao;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public ClientServiceImpl(ClientDao clientDao, PasswordEncoder passwordEncoder) {
        this.clientDao = clientDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<Client> getById(long id) {
        return clientDao.getById(id);
    }

    @Override
    public Client create(String name, String lastName, String email, String password, String phone, Coverage coverage) {
        return this.clientDao.create(name, lastName, email, passwordEncoder.encode(password), phone, coverage);
    }

    @Override
    public Optional<Client> getByEmail(String email) {
        return clientDao.getByEmail(email);
    }
}
