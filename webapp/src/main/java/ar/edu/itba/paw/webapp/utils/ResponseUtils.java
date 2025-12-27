package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.models.Page;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

public class ResponseUtils {
    public static Response buildPaginationHeaders(Response.ResponseBuilder rb, Page<?> items, UriInfo uriInfo) {

        UriBuilder base = uriInfo.getBaseUriBuilder();

        rb.link(base.replaceQueryParam("page", 1)
                .replaceQueryParam("pageSize", items.getPageSize())
                .build(),
                "first"
        );

        rb.link(base.replaceQueryParam("page", items.getTotalPages())
                .replaceQueryParam("pageSize", items.getPageSize())
                .build(),
                "last"
        );

        rb.link(base.replaceQueryParam("page", items.getPageNumber())
                        .replaceQueryParam("pageSize", items.getPageSize())
                        .build(),
                "self"
        );

        if (items.getPageNumber() > 1) {
            rb.link(base.replaceQueryParam("page", items.getPageNumber() - 1)
                            .replaceQueryParam("pageSize", items.getPageSize())
                            .build(),
                    "prev"
            );
        }

        if (items.getPageNumber() < items.getTotalPages()) {
            rb.link(base.replaceQueryParam("page", items.getPageNumber() + 1)
                            .replaceQueryParam("pageSize", items.getPageSize())
                            .build(),
                    "next"
            );
        }

        rb.header("X-Total-Count", items.getTotalElements()); //TODO: looks good, is valid?

        return rb.build();
    }
}
