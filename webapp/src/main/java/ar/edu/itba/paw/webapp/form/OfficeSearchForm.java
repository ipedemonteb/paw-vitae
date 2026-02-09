package ar.edu.itba.paw.webapp.form;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public class OfficeSearchForm {

    @QueryParam("status")
    @DefaultValue("all")
    private String status;

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}