package ar.edu.itba.paw.webapp.mapper;

import ar.edu.itba.paw.models.exception.CustomRuntimeException;
import ar.edu.itba.paw.webapp.dto.ErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Component
@Provider
public class BusinessExceptionMapper implements ExceptionMapper<CustomRuntimeException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(BusinessExceptionMapper.class);

    @Autowired
    private MessageSource messageSource;

    @Override
    public Response toResponse(CustomRuntimeException exception) {
        String message = getI18nMessage(exception.getMessageKey());

        LOGGER.warn("Business Exception: {} - {}", exception.getClass().getSimpleName(), message);

        return Response.status(exception.getStatus())
                .entity(new ErrorDto(message))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }

    private String getI18nMessage(String key) {
        try {
            return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
        } catch (Exception e) {
            return key;
        }
    }
}