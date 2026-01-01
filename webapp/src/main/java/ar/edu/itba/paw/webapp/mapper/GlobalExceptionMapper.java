package ar.edu.itba.paw.webapp.mapper;

import ar.edu.itba.paw.models.exception.CustomRuntimeException;
import ar.edu.itba.paw.webapp.dto.ErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolationException;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.stream.Collectors;

@Component
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionMapper.class);

    @Autowired
    private MessageSource messageSource;

    @Override
    public Response toResponse(Throwable exception) {

        if (exception instanceof WebApplicationException) {
            Response response = ((WebApplicationException) exception).getResponse();
            return Response.status(response.getStatus())
                    .entity(new ErrorDto(exception.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        if (exception instanceof ConstraintViolationException) {
            ConstraintViolationException validationEx = (ConstraintViolationException) exception;
            String messages = validationEx.getConstraintViolations().stream()
                    .map(violation -> getI18nMessage(violation.getMessage()))
                    .collect(Collectors.joining("; "));

            LOGGER.warn("Validation Exception: {}", messages);

            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(new ErrorDto(messages))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
        if (exception instanceof CustomRuntimeException) {
            CustomRuntimeException customEx = (CustomRuntimeException) exception;
            String message = getI18nMessage(customEx.getMessageKey());

            LOGGER.warn("Business Exception: {} - {}", customEx.getClass().getSimpleName(), message);

            return Response.status(customEx.getStatus())
                    .entity(new ErrorDto(message))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }


        LOGGER.error("Uncaught Critical Exception", exception);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorDto("Internal Server Error"))
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