package ar.edu.itba.paw.webapp.mapper;

import ar.edu.itba.paw.webapp.dto.ErrorDto;
import org.glassfish.jersey.server.ParamException;
import org.springframework.stereotype.Component;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Component
@Provider
public class ParamExceptionMapper implements ExceptionMapper<ParamException> {

    @Override
    public Response toResponse(ParamException exception) {
        String paramName = exception.getParameterName();
        String badValue = exception.getDefaultStringValue();
        String userMessage = String.format("The parameter '%s' contains an invalid value: '%s'. Please verify the data type.", paramName, badValue);

        return Response.status(Response.Status.BAD_REQUEST)
                .entity(new ErrorDto(userMessage))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}