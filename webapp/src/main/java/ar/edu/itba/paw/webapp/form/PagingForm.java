package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.utils.ResponseUtils;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.QueryParam;

public class PagingForm extends BasePagingForm {

    @QueryParam("pageSize")
    @DefaultValue("10")
    @Min(1)
    @Max(ResponseUtils.MAX_PAGINATION_PAGE_SIZE)
    private int pageSize;

    public int getPageSize() { return pageSize; }
    public void setPageSize(int pageSize) { this.pageSize = pageSize; }
}