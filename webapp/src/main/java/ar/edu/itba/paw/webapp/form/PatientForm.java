package ar.edu.itba.paw.webapp.form;



import ar.edu.itba.paw.webapp.validation.Coverage;
import ar.edu.itba.paw.webapp.validation.EmailExistance;
import ar.edu.itba.paw.webapp.validation.FileSize;
import ar.edu.itba.paw.webapp.validation.RepeatPasswordMatch;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.List;

@RepeatPasswordMatch(password = "password", repeatPassword = "repeatPassword", message = "{register.passwordsDoNotMatch}")
public class PatientForm {

    @NotEmpty
    @Size(min = 1, max = 30)
    private String name;

    @NotEmpty
    @Size(min = 1, max = 30)
    private String lastName;

    @NotEmpty
    @Email
    @EmailExistance
    @Size(max = 100)
    private String email;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$", message = "{password.invalid}")
    @Size(min = 8, max = 50)
    private String password;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$", message = "{password.invalid}")
    @Size(min = 8, max = 50)
    private String repeatPassword;

    @NotEmpty
    @Pattern(regexp = "\\+?[0-9. ()-]{7,25}")
    private String phone;
    @Coverage(message = "{coverage.invalid}")
    @NotNull
    private Long coverage;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRepeatPassword() {
        return repeatPassword;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Long getCoverage() {
        return coverage;
    }

    public void setCoverage(Long coverage) {
        this.coverage = coverage;
    }
}

