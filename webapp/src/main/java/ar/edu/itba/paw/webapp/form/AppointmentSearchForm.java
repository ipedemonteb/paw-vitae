package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotNull;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public class AppointmentSearchForm extends PagingForm {

    @QueryParam("userId")
    @NotNull
    private Long userId;

    @QueryParam("doctorId")
    private Long doctorId;

    @QueryParam("collection")
    @DefaultValue("upcoming")
    private String collection;

    @QueryParam("filter")
    @DefaultValue("all")
    private String filter;

    @QueryParam("sort")
    @DefaultValue("asc")
    private String sort;


    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public Long getDoctorId() { return doctorId; }
    public void setDoctorId(Long doctorId) { this.doctorId = doctorId; }

    public String getCollection() { return collection; }
    public void setCollection(String collection) { this.collection = collection; }

    public String getFilter() { return filter; }
    public void setFilter(String filter) { this.filter = filter; }

    public String getSort() { return sort; }
    public void setSort(String sort) { this.sort = sort; }
}