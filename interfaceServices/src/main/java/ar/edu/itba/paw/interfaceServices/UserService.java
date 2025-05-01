package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface UserService {

    Optional<? extends User> getByEmail(String email);

    void changePassword(Long id, String password);
    String getLanguageById(long id);

    void changeLanguage(long id, String language);

    void setVerificationToken(User user);
    void setVerificationStatus(User user, boolean status);
    void setResetPasswordToken(User user);
    Optional<? extends User> verifyValidationToken(String token);
    void removeVerificationToken(String token);
}
