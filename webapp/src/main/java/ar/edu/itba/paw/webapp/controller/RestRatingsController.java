package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaceServices.RatingService;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Rating;
import ar.edu.itba.paw.webapp.dto.RatingDTO;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.webapp.utils.ResponseUtils.buildPaginationHeaders;

@Path("/ratings")
@Component
public class RestRatingsController {

    private final RatingService ratingService;

    @Context
    private UriInfo uriInfo;

    @Autowired
    public RestRatingsController(RatingService ratingService){
        this.ratingService = ratingService;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll(
            @QueryParam("doctorId")
            final Integer doctorId,

            @QueryParam("page")
            @DefaultValue("1")
            int page,

            @QueryParam("size" )
            @DefaultValue("9")
            int pageSize //TODO: control de tamaño o que no se pueda tocar. homogenizar en todos lados
    ) {
        Page<Rating> ratingPage = ratingService.getAllRatings(page, pageSize);

        if (ratingPage.getContent().isEmpty()) {
            return Response.noContent().build();
        }

        final List<RatingDTO> dtos = ratingPage.getContent().stream()
                .map(r -> RatingDTO.fromRating(r, uriInfo))
                .collect(Collectors.toList());

        Response.ResponseBuilder rb = Response.ok(new GenericEntity<List<RatingDTO>>(dtos){});


        return buildPaginationHeaders(rb, ratingPage, uriInfo);
    }


    @GET
    @Path("/{id:\\d+}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getRatingsById(@PathParam("id") final long id) {
        final Rating rating = this.ratingService.getRating(id).orElseThrow(NotFoundException::new);

        return Response.ok(new GenericEntity<>(RatingDTO.fromRating(rating, uriInfo)){}).build();

    }



}
