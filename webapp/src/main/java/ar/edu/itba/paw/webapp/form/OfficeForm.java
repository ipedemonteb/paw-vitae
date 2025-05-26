package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.DoctorOfficeForm;
import ar.edu.itba.paw.webapp.validation.UniqueOffices;
import ar.edu.itba.paw.webapp.validation.ValidOfficeNames;
import ar.edu.itba.paw.webapp.validation.ValidOfficeNeighborhood;
import ar.edu.itba.paw.webapp.validation.ValidOfficeSpecialties;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

public class OfficeForm {


    @NotEmpty
    @NotNull
    @Size(min = 1, max = 7)
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
}
