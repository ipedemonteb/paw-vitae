package ar.edu.itba.paw.webapp.form;



import ar.edu.itba.paw.models.Coverage;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class DoctorForm {

    @NotEmpty
    @Size(min = 1, max = 100)
    private String name;

    @NotEmpty
    @Size(min = 1, max = 100)
    private String lastName;

    @NotEmpty
    @Email
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
    private String[] specialty;

    private String coverages;

//    @NotEmpty
//    private List<Coverage> coverageList;

    // Getters and Setters

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

    public String[] getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String[] specialty) {
        this.specialty = specialty;
    }

    public String getSpecialtyAsString() {
        if (specialty == null || specialty.length == 0) {
            return "";
        }
        return String.join(", ", specialty);
    }

    public List<String> getCoverages() {
        return List.of(coverages);
    }

    public void setCoverages(String coverage) {
        this.coverages = coverage;
    }

//    public void addCoverage(Coverage coverage) {
//        this.coverageList.add(coverage);
//    }
//
//    public void removeCoverage(Coverage coverage) {
//        this.coverageList.remove(coverage);
//    }
}

