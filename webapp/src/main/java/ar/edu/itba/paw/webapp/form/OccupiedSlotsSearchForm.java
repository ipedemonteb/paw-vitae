package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import java.time.LocalDate;

public class OccupiedSlotsSearchForm {

    @QueryParam("from")
    private String from;

    @QueryParam("to")
    private String to;

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }
}