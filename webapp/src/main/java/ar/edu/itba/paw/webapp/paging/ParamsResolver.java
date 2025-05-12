package ar.edu.itba.paw.webapp.paging;

import ar.edu.itba.paw.models.QueryParam;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.ArrayList;
import java.util.List;

public class ParamsResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        if (QueryParam.class.isAssignableFrom(parameter.getParameterType())) {
            return true;
        }
        if (List.class.isAssignableFrom(parameter.getParameterType())) {
            String typeName = parameter.getGenericParameterType().getTypeName();
            return typeName.contains("QueryParam");
        }

        return false;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {

        ParamCustomizer defaults = parameter.getParameterAnnotation(ParamCustomizer.class);
        String paramName = (defaults != null && !defaults.paramName().isEmpty()) ? defaults.paramName() : ParamConstants.PAGE_PARAM_NAME;

        if (List.class.isAssignableFrom(parameter.getParameterType())
                && parameter.getGenericParameterType().getTypeName().contains("QueryParam")) {

            String[] values = webRequest.getParameterValues(paramName);
            List<QueryParam> result = new ArrayList<>();
            if (values != null) {
                for (String val : values) {
                    long parsed = parsePositiveIntOrDefault(val, 0);
                    result.add(new QueryParam(parsed));
                }
            }
            return result;
        }

        long defaultValue;
        if (paramName.equals(ParamConstants.PAGE_PARAM_NAME)) {
            defaultValue = (defaults != null && defaults.defaultValue() > 0) ? defaults.defaultValue() : ParamConstants.DEFAULT_PAGE;
        } else {
            defaultValue =  defaults.defaultValue() >= 0 ? defaults.defaultValue() : 0;
        }

        long value = parsePositiveIntOrDefault(webRequest.getParameter(paramName), defaultValue);
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
