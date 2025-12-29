package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.models.Page;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.function.BiFunction;

public class ResponseUtils {

    public static <T, Q> Response buildPaginatedResponse(Page<T> paginatedItems, UriInfo uriInfo, final BiFunction<T, UriInfo, Q> mapper, Response.Status status) {
        List<T> items = paginatedItems.getContent();
        List<Q> itemsDTO = items.stream().map(i -> mapper.apply(i, uriInfo)).toList();
        return buildPaginationHeaders(Response.status(status).entity(new GenericEntity<>(itemsDTO) {}), paginatedItems, uriInfo);
    }

    public static <T, Q> Response buildResponse(T item, UriInfo uriInfo, final BiFunction<T, UriInfo, Q> mapper, Response.Status status) {
        Q itemDTO = mapper.apply(item, uriInfo);
        return Response.status(status).entity(new GenericEntity<>(itemDTO) {}).build();
    }
    public static <T, Q> Response buildResponse(List<T> items, UriInfo uriInfo, final BiFunction<T, UriInfo, Q> mapper, Response.Status status) {
        List<Q> itemsDTO = items.stream().map(i -> mapper.apply(i, uriInfo)).toList();
        return Response.status(status).entity(new GenericEntity<>(itemsDTO) {}).build();
    }

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
