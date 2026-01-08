package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.CustomMediaType;
import ar.edu.itba.paw.webapp.dto.HomeDTO;
import ar.edu.itba.paw.webapp.utils.UriUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.GenericEntity;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path(UriUtils.API_BASE_URL)
@Component
public class RestHomeController {

    @Context
    private  UriInfo uriInfo;

    @GET
    @Produces(CustomMediaType.APPLICATION_HOME)
    public Response home() {
        return Response.ok(new GenericEntity<>(HomeDTO.fromUriInfo(uriInfo)) {}).build();
    }

}
