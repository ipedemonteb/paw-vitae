package ar.edu.itba.paw.webapp.mapper;

import ar.edu.itba.paw.webapp.dto.ErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Component
@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Throwable> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionMapper.class);

    @Override
    public Response toResponse(Throwable exception) {

        if (exception instanceof WebApplicationException) {
            Response response = ((WebApplicationException) exception).getResponse();
            return Response.status(response.getStatus())
                    .entity(new ErrorDto(exception.getMessage()))
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        LOGGER.error("Uncaught Critical Exception", exception);
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity(new ErrorDto("Internal Server Error"))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}