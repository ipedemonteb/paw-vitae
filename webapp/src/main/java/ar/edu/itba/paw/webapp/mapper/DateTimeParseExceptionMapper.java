package ar.edu.itba.paw.webapp.mapper;

import ar.edu.itba.paw.webapp.dto.ErrorDto;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.time.format.DateTimeParseException;

@Component
@Provider
public class DateTimeParseExceptionMapper implements ExceptionMapper<DateTimeParseException> {

    @Override
    public Response toResponse(DateTimeParseException exception) {
        String invalidInput = exception.getParsedString();

        String userMessage = String.format("The value '%s' is not a valid date. The required format is YYYY-MM-DD.", invalidInput);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorDto(userMessage))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}