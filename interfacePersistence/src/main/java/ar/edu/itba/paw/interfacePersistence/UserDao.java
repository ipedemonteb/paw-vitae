package ar.edu.itba.paw.interfacePersistence;

import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface UserDao {
    void setVerificationToken(long id, String token);
    void setVerificationStatus(long id, boolean status);
    void setResetPasswordToken(long id, String token);
    void removeVerificationToken(String token);
    void changePassword(long id, String token);
    String getLanguageById(long id);
    void changeLanguage(long id, String language);

}
