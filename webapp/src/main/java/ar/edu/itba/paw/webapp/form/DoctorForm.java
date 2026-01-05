package ar.edu.itba.paw.webapp.form;



import ar.edu.itba.paw.models.DoctorOfficeForm;
import ar.edu.itba.paw.webapp.validation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.*;
import java.util.List;

@ValidNewOffice
@ValidOfficeTimeSlot(message = "{office.invalid.timeSlot}")
@OfficeAvailabilitySlotIntersection(message = "{office.availabilitySlot.intersection}")
@AcceptedSpecialties(specialties = "specialties", offices = "doctorOfficeForm")
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

    @NotEmpty
    @NotNull
    @Size(min = 1, max = 7)
    @UniqueOffices(message = "{offices.duplicate}")
    @ValidOfficeNames(message = "{offices.invalid.name}")
    @ValidOfficeSpecialties(message = "{offices.invalid.specialties}")
    @ValidOfficeNeighborhood(message = "{offices.invalid.neighborhood}")
    @NewOffice(message = "{offices.invalid.new}")
    private List<DoctorOfficeForm> doctorOfficeForm;

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

    public List<DoctorOfficeForm> getDoctorOfficeForm() {
        return doctorOfficeForm;
    }

    public void setDoctorOfficeForm(List<DoctorOfficeForm> doctorOfficeForm) {
        this.doctorOfficeForm = doctorOfficeForm;
    }

    public String getOfficeAvailabilitySlotForms() {
        return null;
    };
}

