package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.webapp.dto.DoctorDTO;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class ResponseUtils {

    public static Response buildPaginationHeaders(Response.ResponseBuilder rb, Page<?> items, UriInfo uriInfo) {

        UriBuilder uri = uriInfo.getAbsolutePathBuilder();

        rb.link(uri.replaceQueryParam("page", 1)
                .replaceQueryParam("pageSize", items.getPageSize())
                .build(),
                "first"
        );

        rb.link(uri.replaceQueryParam("page", items.getTotalPages())
                .replaceQueryParam("pageSize", items.getPageSize())
                .build(),
                "last"
        );

        rb.link(uri.replaceQueryParam("page", items.getPageNumber())
                        .replaceQueryParam("pageSize", items.getPageSize())
                        .build(),
                "self"
        );

        if (items.getPageNumber() > 1) {
            rb.link(uri.replaceQueryParam("page", items.getPageNumber() - 1)
                            .replaceQueryParam("pageSize", items.getPageSize())
                            .build(),
                    "prev"
            );
        }

        if (items.getPageNumber() < items.getTotalPages()) {
            rb.link(uri.replaceQueryParam("page", items.getPageNumber() + 1)
                            .replaceQueryParam("pageSize", items.getPageSize())
                            .build(),
                    "next"
            );
        }

        rb.header("X-Total-Count", items.getTotalElements()); //TODO: looks good, is valid?

        return rb.build();
    }
}
