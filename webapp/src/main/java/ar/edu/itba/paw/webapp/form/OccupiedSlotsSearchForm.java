package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;
import javax.ws.rs.QueryParam;

public class OccupiedSlotsSearchForm {

    @QueryParam("from")
    @NotNull(message = "From date is required")
    private String from;

    @QueryParam("to")
    @NotNull(message = "To date is required")
    private String to;

    public String getFrom() { return from; }
    public void setFrom(String from) { this.from = from; }

    public String getTo() { return to; }
    public void setTo(String to) { this.to = to; }
}