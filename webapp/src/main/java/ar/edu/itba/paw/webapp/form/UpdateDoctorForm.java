package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.webapp.validation.*;
import org.springframework.web.multipart.MultipartFile;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Specialty;
import javax.validation.constraints.*;
import java.util.List;

public class UpdateDoctorForm {
    @NotEmpty
    private String name;

    @NotEmpty
    private String lastName;

    @FileType(types = {"image/jpeg", "image/png", "image/jpg"},message = "{fileType.invalid}")
    @FileSize(max = 2097154) // 2MB
    private MultipartFile image;

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    @NotEmpty
    @Pattern(regexp = "\\+?[0-9. ()-]{7,25}")
    private String phone;
    @CoverageList(message = "{coverages.invalids}")
    @NotEmpty
    private List<String> coverages;
    @SpecialtyList(message = "{specialties.invalids}")
    @NotEmpty
    private List<String> specialties;

    public void setForm(Doctor doctor) {
        this.name = doctor.getName();
        this.lastName = doctor.getLastName();
        this.phone = doctor.getPhone();
        this.coverages = doctor.getCoverageList().stream().map(Coverage::getName).toList();
        this.specialties = doctor.getSpecialtyList().stream().map(Specialty::getKey).toList();
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

