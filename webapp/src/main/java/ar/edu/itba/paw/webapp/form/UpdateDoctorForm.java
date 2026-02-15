package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.webapp.validation.*;
import org.springframework.web.multipart.MultipartFile;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Specialty;
import javax.validation.constraints.*;
import java.util.List;

@RepeatPasswordMatch(password = "password", repeatPassword = "repeatPassword", message = "register.passwordsDoNotMatch")
public class UpdateDoctorForm {

    @Pattern(regexp = ".*\\S.*", message = "name.invalid")
    @Size(min = 1, max = 30)
    private String name;
    @Pattern(regexp = ".*\\S.*", message = "lastName.invalid")
    @Size(min = 1, max = 30)
    private String lastName;

    @Pattern(regexp = "\\+?[0-9. ()-]{7,25}",message = "phone.invalid")
    private String phone;

    @CoverageList(message = "{coverages.invalids}")
    private List<Long> coverages;

    @SpecialtyList(message = "specialties.invalids")
    private List<Long> specialties;

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

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
    public String getRepeatPassword(){
        return repeatPassword;
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

