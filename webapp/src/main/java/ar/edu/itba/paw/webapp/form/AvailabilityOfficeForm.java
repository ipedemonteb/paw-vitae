package ar.edu.itba.paw.webapp.form;

import javax.ws.rs.QueryParam;

public class AvailabilityOfficeForm {
    @QueryParam("officeId")
    private Long officeId;

    public Long getOfficeId() {
        return officeId;
    }

    public void setOfficeId(Long officeId) {
        this.officeId = officeId;
    }
}
