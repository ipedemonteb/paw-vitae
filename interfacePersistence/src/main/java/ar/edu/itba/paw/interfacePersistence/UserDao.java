package ar.edu.itba.paw.interfacePersistence;

public interface UserDao {

    Number create(String name, String lastName, String email, String password, String phone, String language);

    void setVerificationToken(long id, String token);

    void setVerificationStatus(long id, boolean status);

    void setResetPasswordToken(long id, String token);

    void removeVerificationToken(String token);

    void changePassword(long id, String token);

    String getLanguageById(long id);

    void changeLanguage(long id, String language);

    void removeResetToken(String token);

    void update(long id, String name, String lastName, String phone);

}
