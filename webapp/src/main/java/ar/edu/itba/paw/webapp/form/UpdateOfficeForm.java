package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.DoctorOfficeForm;

import javax.validation.constraints.NotNull;

public class UpdateOfficeForm {
    @NotNull
    private DoctorOfficeForm doctorOfficeForm;

    public DoctorOfficeForm getDoctorOfficeForm() {
        return doctorOfficeForm;
    }

    public void setDoctorOfficeForm(DoctorOfficeForm doctorOfficeForm) {
        this.doctorOfficeForm = doctorOfficeForm;
    }
}
