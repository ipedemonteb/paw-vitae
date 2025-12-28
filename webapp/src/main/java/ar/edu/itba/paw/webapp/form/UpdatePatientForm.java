package ar.edu.itba.paw.webapp.form;



import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.webapp.validation.Coverage;

import javax.validation.constraints.*;

public class UpdatePatientForm {

    @Size(min = 1, max = 30)
    private String name;

    @Size(min = 1, max = 30)
    private String lastName;

    @Pattern(regexp = "\\+?[0-9. ()-]{7,25}",message = "{phone.invalid}")
    private String phone;

    @Coverage(message = "{coverage.invalid}")
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

