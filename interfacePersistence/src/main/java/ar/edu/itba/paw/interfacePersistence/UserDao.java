package ar.edu.itba.paw.interfacePersistence;

import java.time.LocalDateTime;

public interface UserDao {



    void setVerificationToken(long id, String token,LocalDateTime expirationDate);

    void setVerificationStatus(long id, boolean status);

    void setResetPasswordToken(long id, String token,LocalDateTime expirationDate);

    void removeVerificationToken(String token);

    void changePassword(long id, String token);

    LocalDateTime tokenExpirationDate(String token);

    String getLanguageById(long id);

    void changeLanguage(long id, String language);

    void removeResetToken(String token);


}
