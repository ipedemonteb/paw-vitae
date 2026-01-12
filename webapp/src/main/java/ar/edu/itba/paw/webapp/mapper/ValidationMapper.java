package ar.edu.itba.paw.webapp.mapper;

import ar.edu.itba.paw.webapp.dto.ErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.stream.Collectors;

@Component
@Provider
public class ValidationMapper implements ExceptionMapper<ConstraintViolationException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidationMapper.class);

    @Autowired
    private MessageSource messageSource;

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        String messages = exception.getConstraintViolations().stream()
                .map(violation -> getI18nMessage(violation.getMessage()))
                .collect(Collectors.joining("; "));

        LOGGER.warn("Validation Exception caught by Custom Mapper: {}", messages);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorDto(messages))
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