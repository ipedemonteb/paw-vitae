package ar.edu.itba.paw.webapp.mapper;

import ar.edu.itba.paw.webapp.dto.ErrorDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class WebApplicationExceptionMapper implements ExceptionMapper<WebApplicationException> {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebApplicationExceptionMapper.class);

    @Override
    public Response toResponse(WebApplicationException exception) {
        LOGGER.error("WebApplicationException: {} - Message: {} - Status code: {}",
                exception.getClass().getName(),
                exception.getMessage(),
                exception.getResponse().getStatus());

        return Response.status(exception.getResponse().getStatus())
                .entity(new ErrorDto(exception.getMessage()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}

