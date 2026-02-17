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
    public void removeVerificationToken(String token) {
                em.createQuery("UPDATE User u SET u.verificationToken = null WHERE u.verificationToken = :token")
                .setParameter("token", token)
                .executeUpdate();
    }

    @Override
    public LocalDateTime tokenExpirationDate(String token) {
        return em.createQuery("SELECT u.tokenExpiration FROM User u WHERE u.verificationToken = :token OR u.resetPasswordToken = :token", LocalDateTime.class)
                .setParameter("token", token)
                .getResultList()
                .stream()
                .findFirst()
                .orElse(null);
    }

}
