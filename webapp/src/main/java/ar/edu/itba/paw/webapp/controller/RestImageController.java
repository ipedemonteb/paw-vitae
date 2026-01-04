package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.ImageService;
import ar.edu.itba.paw.models.Images;
import ar.edu.itba.paw.webapp.utils.FileUtils;
import org.glassfish.jersey.media.multipart.FormDataMultiPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.Set;

@Path("/images")
@Component
public class RestImageController {

    private static final Logger LOGGER = LoggerFactory.getLogger(RestImageController.class);
    private static final Set<String> ALLOWED_IMAGE_TYPES = Set.of("image/png", "image/jpeg", "image/jpg");

    private final ImageService imageService;
    @Context
    private UriInfo uriInfo;


    @Autowired
    public RestImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GET
    @Path("/{id:\\d+}")
    @Produces({"image/png", "image/jpeg", "image/jpg"})
    public Response getImage(@PathParam("id") final long id) {
        final Images image = imageService.findById(id).orElseThrow(NotFoundException::new);
        byte[] imageData = image.getImage();
        return Response.ok(imageData).build(); //TODO: SHOULD I CACHE?
    }

    @POST
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public Response uploadImage(final FormDataMultiPart multipart) {
        MultipartFile file = FileUtils.requireSingleImage(multipart, "file");
        Images image = imageService.create(file);
        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(image.getId())).build()).build();
    }
}
