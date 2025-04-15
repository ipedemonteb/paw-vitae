package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.ClientDao;
import ar.edu.itba.paw.interfaceServices.ClientService;
import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.interfaceServices.ImageService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.Coverage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientDao clientDao;

    private final PasswordEncoder passwordEncoder;

    private final CoverageService cs;

    @Autowired
    public ClientServiceImpl(ClientDao clientDao, PasswordEncoder passwordEncoder, CoverageService cs) {
        this.clientDao = clientDao;
        this.passwordEncoder = passwordEncoder;
        this.cs = cs;
    }

    @Override
    public Optional<Client> getById(long id) {
        return clientDao.getById(id);
    }

    public Client create(String name, String lastName, String email, String password, String phone, String coverage) {
        Coverage cov = cs.findById(Long.parseLong(coverage)).orElse(null);
        return this.clientDao.create(name, lastName, email, passwordEncoder.encode(password), phone, cov);
    }

    @Override
    public Optional<Client> getByEmail(String email) {
        return clientDao.getByEmail(email);
    }

    @Override
    public void updateClient(long id, String name, String lastName, String phone, String coverage) {
        Coverage cov = cs.findById(Long.parseLong(coverage)).orElse(null);
        clientDao.updateClient(id, name, lastName, phone, cov);
    }

    @Override
    public List<Client> getByAppointments(List<Appointment> appointments) {
        Set<Long> ids = appointments.stream().map(Appointment::getClientId).collect(Collectors.toSet());
        return ids.isEmpty()? Collections.emptyList() : clientDao.getByIds(ids);
    }
}
