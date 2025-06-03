package ar.edu.itba.paw.models;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.swing.text.View;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//@TODO: Check if entity or mappedSuperclass
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "users")
public abstract class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_id_seq")
    @SequenceGenerator(allocationSize = 1, sequenceName = "users_id_seq", name = "users_id_seq")
    private  long id;

    @Column(name ="email",unique = true, nullable = false,length = 50)
    private  String email;

    @Column(name = "name", nullable = false, length = 50)
    private String name;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @JsonView(Doctor.Views.Private.class)
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "language", length = 10)
    private String language;

    @JsonView(Doctor.Views.Private.class)
    @Column(name = "verification_token")
    private String verificationToken;

    @JsonView(Doctor.Views.Private.class)
    @Column(name = "is_verified")
    private boolean verified = false;

    @JsonView(Doctor.Views.Private.class)
    @Column(name = "reset_token")
    private String resetPasswordToken;

    @JsonView(Doctor.Views.Private.class)
    @Column(name = "token_expiration_date")
    private LocalDateTime tokenExpiration;

    public User() {
        // For Hibernate use
    }

    public User(String name, String lastName, String email, String password, String phone, String language, boolean verified) {
        this.name = name;

        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.language = language;
        this.verified = verified;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLanguage() { return language; }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getVerificationToken() {
        return verificationToken;
    }

    public void setVerificationToken(String verificationToken) {
        this.verificationToken = verificationToken;
    }

    public boolean isVerified() { return verified; }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public String getResetPasswordToken() {
        return resetPasswordToken;
    }

    public void setResetPasswordToken(String resetPasswordToken) {
        this.resetPasswordToken = resetPasswordToken;
    }
}
