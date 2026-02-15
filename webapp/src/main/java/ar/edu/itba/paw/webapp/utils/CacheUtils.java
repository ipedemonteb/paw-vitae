package ar.edu.itba.paw.webapp.utils;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response.ResponseBuilder;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;


public class CacheUtils {

    public static final int UNCONDITIONAL_MAX_AGE = 31536000;
    public static final int IMAGE_MAX_AGE = 25920000;
    public static final int FILE_MAX_AGE = 31536000;


    public static ResponseBuilder unconditionalCache(ResponseBuilder responseBuilder, int maxAge) {
        final CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(maxAge);
        return responseBuilder.cacheControl(cacheControl);
    }

    public static ResponseBuilder conditionalCacheLastModified(ResponseBuilder responseBuilder, Request request, LocalDateTime lastModifiedLDT) {
        final CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);

        Date lastModified = null;
        if (lastModifiedLDT != null) {
            lastModified = Date.from(lastModifiedLDT.truncatedTo(ChronoUnit.SECONDS).atZone(ZoneId.systemDefault()).toInstant());
        }

        final ResponseBuilder rb = request.evaluatePreconditions(lastModified);
        if (rb != null) {
            return rb.cacheControl(cacheControl).lastModified(lastModified);
        }
        return responseBuilder.cacheControl(cacheControl).lastModified(lastModified);
    }

    public static ResponseBuilder conditionalCacheETag(ResponseBuilder responseBuilder, Request request, int hashCode) {
        final CacheControl cacheControl = new CacheControl();
        cacheControl.setNoCache(true);

        final EntityTag eTag = new EntityTag(Integer.toString(hashCode));
        final ResponseBuilder rb = request.evaluatePreconditions(eTag);
        if (rb != null) {
            return rb.cacheControl(cacheControl).tag(eTag);
        }
        return responseBuilder.cacheControl(cacheControl).tag(eTag);
    }
}
