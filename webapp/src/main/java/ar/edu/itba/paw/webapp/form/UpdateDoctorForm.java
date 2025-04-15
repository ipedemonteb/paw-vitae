package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.webapp.validation.EmailExistance;
import ar.edu.itba.paw.webapp.validation.FileSize;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.List;

public class UpdateDoctorForm {

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
    private List<String> coverages;

    private List<String> specialties;

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

    public List<String> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<String> specialties) {
        this.specialties = specialties;
    }

    public List<String> getCoverages() {
        return coverages;
    }

    public void setCoverages(List<String> coverages) {
        this.coverages = coverages;
    }

}

