package ar.edu.itba.paw.interfaceServices;

import ar.edu.itba.paw.models.AvailabilitySlot;
import ar.edu.itba.paw.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserService {

    Optional<? extends User> getByEmail(String email);

    Optional<? extends User> getById(long id);

    boolean changePassword(String token, String password);

    void changeLanguage(long id, String language);

    void setVerificationToken(String email);

    void setVerificationStatus(User user, boolean status);

    void setResetPasswordToken(String email);

    Optional<? extends User> verifyValidationToken(String token);

    Optional<? extends User> getByResetToken(String token);

    boolean verifyRecoveryToken(String token);

    Long getImageId(User user);

    long create(String name, String lastName, String email, String password, String phone, String language);

    void update(long id, String name, String lastName, String phone);

}
