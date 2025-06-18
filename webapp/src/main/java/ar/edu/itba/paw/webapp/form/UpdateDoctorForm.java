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
    @Size(min = 1, max = 30)
    @NotEmpty
    private String name;

    @Size(min = 1, max = 30)
    @NotEmpty
    private String lastName;

    @FileType(types = {"image/jpeg", "image/png", "image/jpg"},message = "{fileType.invalid}")
    @FileSize(max = 2097154)
    private MultipartFile image;

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    @NotEmpty
    @Pattern(regexp = "\\+?[0-9. ()-]{7,25}",message = "{phone.invalid}")
    private String phone;
    @CoverageList(message = "{coverages.invalids}")
    @NotEmpty
    private List<Long> coverages;
    @SpecialtyList(message = "{specialties.invalids}")
    @NotEmpty
    private List<Long> specialties;

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

    public List<Long> getCoverages() {
        return coverages;
    }

    public void setCoverages(List<Long> coverages) {
        this.coverages = coverages;
    }

    public List<Long> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<Long> specialties) {
        this.specialties = specialties;
    }
}

