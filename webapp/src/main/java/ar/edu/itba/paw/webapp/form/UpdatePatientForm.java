package ar.edu.itba.paw.webapp.form;



import ar.edu.itba.paw.models.Patient;

import javax.validation.constraints.*;

public class UpdatePatientForm {

    @NotEmpty
    @Size(min = 1, max = 100)
    private String name;

    @NotEmpty
    @Size(min = 1, max = 100)
    private String lastName;


    @NotEmpty
    @Pattern(regexp = "\\+?[0-9. ()-]{7,25}")
    private String phone;

    @NotEmpty
    private String coverage;


    public void setForm(Patient patient) {
        this.name = patient.getName();
        this.lastName = patient.getLastName();
        this.phone = patient.getPhone();
        this.coverage = patient.getCoverage().getName();
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

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }




}

