package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Coverage;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

public class CoverageDTO {
    private String name;
    private URI self;


    public static CoverageDTO fromCoverage(Coverage coverage, UriInfo uriInfo) {
        CoverageDTO res = new CoverageDTO();
        res.name = coverage.getName();
        res.self = uriInfo.getBaseUriBuilder().path("api").path("coverages").path(String.valueOf(coverage.getId())).build();
        return res;
    }

    public static List<CoverageDTO> fromCoverage(List<Coverage> coverages, UriInfo uriInfo) {
        return coverages.stream().map(c -> fromCoverage(c, uriInfo)).toList();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }
}
