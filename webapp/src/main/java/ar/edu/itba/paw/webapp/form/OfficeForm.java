package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.DoctorOfficeForm;
import ar.edu.itba.paw.webapp.validation.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

//@ValidOfficeId
//@ActiveOfficeAvailability
//@MaxNumberOfOffices
@ValidNewOffice
public class OfficeForm {


    @NotNull
//    @ActiveOffice(message = "{offices.active}")
    @UniqueOffices(message = "{offices.duplicate}")
    @ValidOfficeNames(message = "offices.invalid.name")
    @ValidOfficeSpecialties(message = "offices.invalid.specialties")
    @ValidOfficeNeighborhood(message = "offices.invalid.neighborhood")
    private DoctorOfficeForm doctorOfficeForm;


    public DoctorOfficeForm getDoctorOfficeForm() {
        return doctorOfficeForm;
    }

    public void setDoctorOfficeForm(DoctorOfficeForm doctorOfficeForm) {
        this.doctorOfficeForm = doctorOfficeForm;
    }


}
