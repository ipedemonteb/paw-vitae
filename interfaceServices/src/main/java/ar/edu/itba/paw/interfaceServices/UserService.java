package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.User;

import java.util.Optional;

public interface UserService {

    Optional<? extends User> getByEmail(String email);

    boolean changePassword(String token, String password);

    void changeLanguage(long id, String language);

    void setVerificationToken(String email);

    void setVerificationStatus(User user, boolean status);

    void setResetPasswordToken(String email);

    Optional<? extends User> verifyValidationToken(String token);

    Optional<? extends User> getByResetToken(String token);

    boolean verifyRecoveryToken(String token);

    Long getImageId(User user);
}
