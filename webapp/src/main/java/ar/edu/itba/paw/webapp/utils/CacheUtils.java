package ar.edu.itba.paw.webapp.utils;

import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Response.ResponseBuilder;


public class CacheUtils {

    public static final int UNCONDITIONAL_MAX_AGE = 31536000;
    public static final int IMAGE_MAX_AGE = 25920000;
    public static final int FILE_MAX_AGE = 31536000;


    public static ResponseBuilder unconditionalCache(ResponseBuilder responseBuilder, int maxAge) {
        final CacheControl cacheControl = new CacheControl();
        cacheControl.setMaxAge(maxAge);
        return responseBuilder.cacheControl(cacheControl);
    }



}
