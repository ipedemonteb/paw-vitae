package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.utils.ResponseUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public class AppointmentSearchForm extends BasePagingForm {

    @QueryParam("patientId")
    private Long patientId;

    @QueryParam("collection")
    @DefaultValue("upcoming")
    private String collection;

    @QueryParam("filter")
    @DefaultValue("all")
    private String filter;

    @QueryParam("pageSize")
    @DefaultValue("10")
    @Min(1)
    @Max(ResponseUtils.MAX_APPOINTMENT_PAGE_SIZE)
    private int pageSize;

    @QueryParam("sort")
    @DefaultValue("asc")
    private String sort;


    public Long getPatientId() {
        return patientId;
    }

    public void setPatientId(Long patientId) {
        this.patientId = patientId;
    }

    public String getCollection() { return collection; }
    public void setCollection(String collection) { this.collection = collection; }

    public String getFilter() { return filter; }
    public void setFilter(String filter) { this.filter = filter; }

    public String getSort() { return sort; }
    public void setSort(String sort) { this.sort = sort; }


    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }
}