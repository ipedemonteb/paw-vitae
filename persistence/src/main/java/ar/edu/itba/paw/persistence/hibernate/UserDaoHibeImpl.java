package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.UserDao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

public class UserDaoHibeImpl implements UserDao {

    @PersistenceContext
    private EntityManager em;

    @Override
    public Number create(String name, String lastName, String email, String password, String phone, String language) {

    }

    @Override
    public void setVerificationToken(long id, String token, LocalDateTime expirationDate) {

    }

    @Override
    public void setVerificationStatus(long id, boolean status) {

    }

    @Override
    public void setResetPasswordToken(long id, String token, LocalDateTime expirationDate) {

    }

    @Override
    public void removeVerificationToken(String token) {

    }

    @Override
    public void changePassword(long id, String token) {

    }

    @Override
    public LocalDateTime tokenExpirationDate(String token) {
        return null;
    }

    @Override
    public String getLanguageById(long id) {
        return null;
    }

    @Override
    public void changeLanguage(long id, String language) {

    }

    @Override
    public void removeResetToken(String token) {

    }

    @Override
    public void update(long id, String name, String lastName, String phone) {

    }
}
