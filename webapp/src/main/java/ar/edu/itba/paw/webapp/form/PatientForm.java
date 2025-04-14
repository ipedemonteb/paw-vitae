package ar.edu.itba.paw.webapp.form;



import ar.edu.itba.paw.webapp.validation.EmailExistance;
import ar.edu.itba.paw.webapp.validation.FileSize;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.List;

public class PatientForm {

    @NotEmpty
    @Size(min = 1, max = 100)
    private String name;

    @NotEmpty
    @Size(min = 1, max = 100)
    private String lastName;

    @NotEmpty
    @Email
    @EmailExistance
    @Size(max = 100)
    private String email;

    @NotEmpty
    @Size(min = 6, max = 100)
    private String password;

    @NotEmpty
    @Size(min = 6, max = 100)
    private String repeatPassword;

    @NotEmpty
    @Pattern(regexp = "\\+?[0-9. ()-]{7,25}")
    private String phone;

    @NotEmpty
    private String coverage;




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

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }


}

