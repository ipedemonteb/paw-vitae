package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Coverage;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

public class CoverageRefDTO {

    private URI self;

    public static CoverageRefDTO fromCoverage(Coverage coverage, UriInfo uriInfo) {
        CoverageRefDTO dto = new CoverageRefDTO();
        dto.self = uriInfo.getBaseUriBuilder()
                .path("api")
                .path("coverages")
                .path(String.valueOf(coverage.getId()))
                .build();
        return dto;
    }

    public static List<CoverageRefDTO> fromCoverageList(List<Coverage> coverages, UriInfo uriInfo) {
        return coverages.stream()
                .map(c -> fromCoverage(c, uriInfo))
                .collect(Collectors.toList());
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }
}