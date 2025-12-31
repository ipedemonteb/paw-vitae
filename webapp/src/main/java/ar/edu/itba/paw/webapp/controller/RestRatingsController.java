package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaceServices.RatingService;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Rating;
import ar.edu.itba.paw.webapp.dto.RatingDTO;
import ar.edu.itba.paw.webapp.form.PatientRatingForm;
import ar.edu.itba.paw.webapp.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static ar.edu.itba.paw.webapp.utils.ResponseUtils.*;
import static javax.ws.rs.core.Response.Status.OK;

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
            @QueryParam("page")
            @DefaultValue("1")
            int page,

            @QueryParam("size" )
            @DefaultValue("9")
            int pageSize //TODO: control de tamaño o que no se pueda tocar. homogenizar en todos lados
    ) {
        Page<Rating> ratingPage = ratingService.getAllRatings(page, pageSize);
        return buildPaginationHeaders(Response.ok(new GenericEntity<>(RatingDTO.fromRating(ratingPage.getContent(), uriInfo)) {}), ratingPage, uriInfo);
    }


    @GET
    @Path("/{id:\\d+}")
    @Produces(value = MediaType.APPLICATION_JSON)
    public Response getRatingsById(@PathParam("id") final long id) {
        final Rating rating = this.ratingService.getRating(id).orElseThrow(NotFoundException::new);
        return Response.ok(new GenericEntity<>(RatingDTO.fromRating(rating, uriInfo)) {}).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createRating(@Valid @NotNull final PatientRatingForm form) {


        //TODO: IN FRONTEND PASS THE PATIENT ID
        final Rating createdRating = ratingService.create(
                form.getRating(),
                form.getDoctorId(),
                form.getPatientId(),
                form.getAppointmentId(),
                form.getComment()
        );

        return Response.created(uriInfo.getAbsolutePathBuilder().path(String.valueOf(createdRating.getId())).build()).build();




    }


}
