package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Min;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public abstract class BasePagingForm {

    @QueryParam("page")
    @DefaultValue("1")
    @Min(1)
    private int page;

    public int getPage() { return page; }
    public void setPage(int page) { this.page = page; }
}