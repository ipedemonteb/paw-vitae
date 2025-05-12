package ar.edu.itba.paw.webapp.paging;

import ar.edu.itba.paw.models.QueryParam;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class ParamsResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return QueryParam.class.isAssignableFrom(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {


        ParamCustomizer defaults = parameter.getParameterAnnotation(ParamCustomizer.class);
        String defaultName = (defaults != null && !defaults.paramName().isEmpty()) ? defaults.paramName() : ParamConstants.PAGE_PARAM_NAME;
        System.out.println("Default name: " + defaultName);
        long defaultValue;
        if (defaultName.equals(ParamConstants.PAGE_PARAM_NAME)) {
             defaultValue = (defaults != null && defaults.defaultValue() > 0) ? defaults.defaultValue() : ParamConstants.DEFAULT_PAGE;
        }else{
            defaultValue= defaults.defaultValue() >= 0 ? defaults.defaultValue() : 0;
        }
        long value = parsePositiveIntOrDefault(webRequest.getParameter(defaultName), defaultValue);
        return new QueryParam(value);
    }

    private long parsePositiveIntOrDefault(String param, long defaultValue) {
        if (param == null) {
            return defaultValue;
        }
        try {
         long value = Long.parseLong(param);
            if (value > 0) {
                return value;
            } else {
                return defaultValue;
            }
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
}
