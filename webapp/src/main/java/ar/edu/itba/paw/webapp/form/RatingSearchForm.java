package ar.edu.itba.paw.webapp.form;

import javax.ws.rs.QueryParam;

public class RatingSearchForm extends PagingForm {

    @QueryParam("doctorId")
    private Long doctorId;

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }
}