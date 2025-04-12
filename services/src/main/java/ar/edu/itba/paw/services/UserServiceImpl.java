package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.ClientDao;
import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.interfaceServices.UserService;
import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private ClientDao clientDao;
    private DoctorDao doctorDao;

    @Autowired
    public UserServiceImpl(ClientDao clientDao, DoctorDao doctorDao) {
        this.clientDao = clientDao;
        this.doctorDao = doctorDao;
    }

    @Override
    public Optional<? extends User> getByEmail(String email) {
        Optional<Client> client = clientDao.getByEmail(email);
        return client.isPresent() ? client : doctorDao.getByEmail(email);
    }
}
