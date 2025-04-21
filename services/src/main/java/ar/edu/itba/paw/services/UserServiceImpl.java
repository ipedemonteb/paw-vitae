package ar.edu.itba.paw.services;

import ar.edu.itba.paw.interfacePersistence.ClientDao;
import ar.edu.itba.paw.interfacePersistence.DoctorDao;
import ar.edu.itba.paw.interfaceServices.UserService;
import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
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

    public String getLanguageById(long id) {
        Optional<Client> client = clientDao.getById(id);
        if (client.isPresent()) {
            return clientDao.getLanguage(id);
        } else {
            return doctorDao.getLanguage(id);
        }
    }

    public void changeLanguage(long id, String language) {
        Optional<Client> client = clientDao.getById(id);
        if (client.isPresent()) {
            clientDao.changeLanguage(id, language);
        } else {
            doctorDao.changeLanguage(id, language);
        }
    }
}
