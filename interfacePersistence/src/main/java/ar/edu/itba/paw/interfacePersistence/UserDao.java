package ar.edu.itba.paw.interfacePersistence;

import java.time.LocalDateTime;

public interface UserDao {

    void removeVerificationToken(String token);

    LocalDateTime tokenExpirationDate(String token);

}
