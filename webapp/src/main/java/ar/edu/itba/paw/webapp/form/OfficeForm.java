package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.DoctorOfficeForm;
import ar.edu.itba.paw.webapp.validation.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@ValidOfficeId
public class OfficeForm {

    @NotNull
    private Long doctorId;

    @NotEmpty
    @NotNull
    @Size(min = 1, max = 20)
    @ActiveOffice(message = "{offices.active}")
    @UniqueOffices(message = "{offices.duplicate}")
    @ValidOfficeNames(message = "{offices.invalid.name}")
    @ValidOfficeSpecialties(message = "{offices.invalid.specialties}")
    @ValidOfficeNeighborhood(message = "{offices.invalid.neighborhood}")
    private List<DoctorOfficeForm> doctorOfficeForm;


    public List<DoctorOfficeForm> getDoctorOfficeForm() {
        return doctorOfficeForm;
    }

    public void setDoctorOfficeForm(List<DoctorOfficeForm> doctorOfficeForm) {
        this.doctorOfficeForm = doctorOfficeForm;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(Long doctorId) {
        this.doctorId = doctorId;
    }
}
