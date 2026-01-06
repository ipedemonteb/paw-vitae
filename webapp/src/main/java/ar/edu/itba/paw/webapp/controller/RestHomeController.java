package ar.edu.itba.paw.webapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/")
@Component
public class RestHomeController {

    @Context
    private  UriInfo uriInfo;

    @GET
    public String home() {
        return Response.ok("Welcome to the API").build().getEntity().toString();
    }

}
