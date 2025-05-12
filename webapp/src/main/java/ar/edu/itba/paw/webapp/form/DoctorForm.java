package ar.edu.itba.paw.webapp.form;



import ar.edu.itba.paw.models.AvailabilitySlot;
import ar.edu.itba.paw.webapp.validation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.time.LocalTime;
import java.util.List;

@RepeatPasswordMatch(password = "password", repeatPassword = "repeatPassword", message = "{register.passwordsDoNotMatch}")
public class DoctorForm {

    @NotEmpty
    @Size(min = 1, max = 30)
    private String name;

    @NotEmpty
    @Size(min = 1, max = 30)
    private String lastName;

    @NotEmpty
    @Email
    @EmailExistance( message = "{email.existance}")
    @Size(max = 100)
    private String email;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$", message = "{password.invalid}")
    @Size(min = 8, max = 50)
    private String password;

    @NotEmpty
    @Size(min = 8, max = 50)
    private String repeatPassword;

    @NotEmpty
    @Pattern(regexp = "\\+?[0-9. ()-]{7,25}",message = "{phone.invalid}")
    private String phone;
    @SpecialtyList(message = "{specialties.invalids}")
    @NotEmpty
    private List<Long> specialties;
    @CoverageList(message = "{coverages.invalids}")
    @NotEmpty
    private List<Long> coverages;
    @FileType(types = {"image/jpeg", "image/png", "image/jpg"},message = "{fileType.invalid}")
    @FileSize(max = 2097154) // 2MB
    private MultipartFile image;

    @NotEmpty
    @ValidTimeSlot(message = "{slots.invalid}")
    @TimeSlotIntersection(message = "{slots.overlap}")
    private List<AvailabilitySlot> availabilitySlots;


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

    public List<Long> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<Long> specialties) {
        this.specialties = specialties;
    }

    public List<Long> getCoverages() {
        return coverages;
    }

    public void setCoverages(List<Long> coverages) {
        this.coverages = coverages;
    }

    public MultipartFile getImage() {
        return image;
    }

    public void setImage(MultipartFile image) {
        this.image = image;
    }

    public List<AvailabilitySlot> getAvailabilitySlots() {
        return availabilitySlots;
    }
    public void setAvailabilitySlots(List<AvailabilitySlot> availabilitySlots) {
        this.availabilitySlots = availabilitySlots;
    }
}

