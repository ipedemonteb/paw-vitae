package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Min;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;
import java.util.List;


public class DoctorSearchForm extends PagingForm {

    @QueryParam("specialty")
    @DefaultValue("0")
    @Min(0)
    private long specialtyId;

    @QueryParam("coverage")
    @DefaultValue("0")
    @Min(0)
    private long coverageId;

    @QueryParam("weekdays")
    private List<Integer> weekdays;

    @QueryParam("keyword")
    @DefaultValue("")
    private String keyword;

    @QueryParam("orderBy")
    @DefaultValue("name")
    private String orderBy;

    @QueryParam("direction")
    @DefaultValue("asc")
    private String direction;

    public long getSpecialtyId() {
        return specialtyId;
    }

    public void setSpecialtyId(long specialtyId) {
        this.specialtyId = specialtyId;
    }

    public long getCoverageId() {
        return coverageId;
    }

    public void setCoverageId(long coverageId) {
        this.coverageId = coverageId;
    }

    public List<Integer> getWeekdays() {
        return weekdays;
    }

    public void setWeekdays(List<Integer> weekdays) {
        this.weekdays = weekdays;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public String getOrderBy() {
        return orderBy;
    }

    public void setOrderBy(String orderBy) {
        this.orderBy = orderBy;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}