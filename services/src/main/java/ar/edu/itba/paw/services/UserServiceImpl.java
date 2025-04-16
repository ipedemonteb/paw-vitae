package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.ClientDao;
import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.interfaceServices.UserService;
import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private ClientDao clientDao;
    private DoctorDao doctorDao;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(ClientDao clientDao, DoctorDao doctorDao, PasswordEncoder passwordEncoder) {
        this.clientDao = clientDao;
        this.doctorDao = doctorDao;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<? extends User> getByEmail(String email) {
        Optional<Client> client = clientDao.getByEmail(email);
        return client.isPresent() ? client : doctorDao.getByEmail(email);
    }

    @Override
    public void changePassword(Long id, String password) {
        Optional<Client> client = clientDao.getById(id);
        String newPassword = passwordEncoder.encode(password);
        if (client.isPresent()) {
            clientDao.changePassword(id, newPassword);
        } else {
            doctorDao.changePassword(id, newPassword);
        }
    }
}
