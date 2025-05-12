package ar.edu.itba.paw.webapp.paging;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
public @interface ParamCustomizer {

    long defaultValue() default ParamConstants.DEFAULT_PAGE;
    String paramName() default ParamConstants.PAGE_PARAM_NAME;

}