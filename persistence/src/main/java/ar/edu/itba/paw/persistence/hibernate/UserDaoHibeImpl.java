package ar.edu.itba.paw.persistence.hibernate;

import ar.edu.itba.paw.interfacePersistence.UserDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDateTime;

@Repository
public class UserDaoHibeImpl implements UserDao {

    @PersistenceContext
    private EntityManager em;



    @Override
    public void setVerificationToken(long id, String token, LocalDateTime expirationDate) {
        em.createQuery("UPDATE User u SET u.verificationToken = :token, u.tokenExpiration = :expirationDate WHERE u.id = :id")
                .setParameter("token", token)
                .setParameter("expirationDate", expirationDate)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void setVerificationStatus(long id, boolean status) {
        em.createQuery("UPDATE User u SET u.verified = :status WHERE u.id = :id")
                .setParameter("status", status)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void setResetPasswordToken(long id, String token, LocalDateTime expirationDate) {
        em.createQuery("UPDATE User u SET u.resetPasswordToken = :token, u.tokenExpiration = :expirationDate WHERE u.id = :id")
                .setParameter("token", token)
                .setParameter("expirationDate", expirationDate)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void removeVerificationToken(String token) {
                em.createQuery("UPDATE User u SET u.verificationToken = null WHERE u.verificationToken = :token")
                .setParameter("token", token)
                .executeUpdate();
    }

    @Override
    public void changePassword(long id, String token) {
        em.createQuery("UPDATE User u SET u.password = :password WHERE u.id = :id")
                .setParameter("password", token)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public LocalDateTime tokenExpirationDate(String token) {
        return (LocalDateTime) em.createQuery("SELECT u.tokenExpiration FROM User u WHERE u.verificationToken = :token OR u.resetPasswordToken = :token")
                .setParameter("token", token)
                .getSingleResult();
    }

    @Override
    public String getLanguageById(long id) {
        return (String) em.createQuery("SELECT u.language FROM User u WHERE u.id = :id")
                .setParameter("id", id)
                .getSingleResult();
    }

    @Override
    public void changeLanguage(long id, String language) {
        em.createQuery("UPDATE User u SET u.language = :language WHERE u.id = :id")
                .setParameter("language", language)
                .setParameter("id", id)
                .executeUpdate();
    }

    @Override
    public void removeResetToken(String token) {
        em.createQuery("UPDATE User u SET u.resetPasswordToken = null WHERE u.resetPasswordToken = :token")
                .setParameter("token", token)
                .executeUpdate();
    }
}
