package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.ImageService;
import ar.edu.itba.paw.models.Images;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path("/images")
@Component
public class RestImageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestImageController.class);

    private final ImageService imageService;


    @Autowired
    public RestImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GET
    @Path("/{id}")
    @Produces({"image/png", "image/jpeg", "image/jpg"})
    public Response getImage(@PathParam("id") final long id) {

        final Images image = imageService.findById(id).orElseThrow(NotFoundException::new);

        byte[] imageData = image.getImage();

        return Response.ok(imageData).build(); //TODO: SHOULD I CACHE?
    }

}
