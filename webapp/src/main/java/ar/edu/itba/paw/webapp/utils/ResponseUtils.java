package ar.edu.itba.paw.webapp.utils;

import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.webapp.dto.DoctorDTO;
import org.springframework.context.i18n.LocaleContextHolder;

import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.Locale;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public final class ResponseUtils {
    public static final int MAX_PAGINATION_PAGE_SIZE = 100;
    public static Response buildPaginationHeaders(Response.ResponseBuilder rb, Page<?> items, UriInfo uriInfo) {

        UriBuilder uri = uriInfo.getRequestUriBuilder().clone();

        rb.link(uri.clone().replaceQueryParam("page", 1)
                .replaceQueryParam("pageSize", items.getPageSize())
                .build(),
                "first"
        );

        rb.link(uri.clone().replaceQueryParam("page", items.getTotalPages())
                .replaceQueryParam("pageSize", items.getPageSize())
                .build(),
                "last"
        );

        rb.link(uri.clone().replaceQueryParam("page", items.getPageNumber())
                        .replaceQueryParam("pageSize", items.getPageSize())
                        .build(),
                "self"
        );

        if (items.getPageNumber() > 1) {
            rb.link(uri.clone().replaceQueryParam("page", items.getPageNumber() - 1)
                            .replaceQueryParam("pageSize", items.getPageSize())
                            .build(),
                    "prev"
            );
        }

        if (items.getPageNumber() < items.getTotalPages()) {
            rb.link(uri.clone().replaceQueryParam("page", items.getPageNumber() + 1)
                            .replaceQueryParam("pageSize", items.getPageSize())
                            .build(),
                    "next"
            );
        }

        rb.header("X-Total-Count", items.getTotalElements());

        rb.header("Access-Control-Expose-Headers", "X-Total-Count, Link");

        return rb.build();
    }
    public static Locale getLocaleFromHeaders(List<Locale> acceptableLanguages) {
        System.out.println("Acceptable languages: " + acceptableLanguages);
        System.out.println("Default locale: " + LocaleContextHolder.getLocale());
        return acceptableLanguages.isEmpty()
                ? LocaleContextHolder.getLocale()
                : acceptableLanguages.get(0);
    }
}
