package ar.edu.itba.paw.webapp.form;



import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.webapp.validation.Coverage;
import ar.edu.itba.paw.webapp.validation.RepeatPasswordMatch;

import javax.validation.constraints.*;
@RepeatPasswordMatch(password = "password", repeatPassword = "repeatPassword", message = "register.passwordsDoNotMatch")
public class UpdatePatientForm {
    @Pattern(regexp = ".*\\S.*", message = "name.invalid")
    @Size(min = 1, max = 30)
    private String name;
    @Pattern(regexp = ".*\\S.*", message = "lastName.invalid")
    @Size(min = 1, max = 30)
    private String lastName;

    @Pattern(regexp = "\\+?[0-9. ()-]{7,25}",message = "phone.invalid")
    private String phone;

    @Coverage(message = "coverage.invalid")
    private Long coverage;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$", message = "password.invalid")
    @Size(min = 8, max = 50)
    private String password;

    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$", message = "password.invalid")
    @Size(min = 8, max = 50)
    private String repeatPassword;

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
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

