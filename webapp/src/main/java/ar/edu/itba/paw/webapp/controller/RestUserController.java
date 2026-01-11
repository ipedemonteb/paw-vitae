package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.UserService;
import ar.edu.itba.paw.webapp.CustomMediaType;
import ar.edu.itba.paw.webapp.form.RecoverPasswordForm;
import ar.edu.itba.paw.webapp.utils.UriUtils;
import org.glassfish.jersey.internal.inject.Custom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

@Path(UriUtils.USERS)
@Component
public class RestUserController {

    private final UserService userService;
    @Context
    private UriInfo uriInfo;

    @Autowired
    public RestUserController(UserService userService) {
        this.userService = userService;
    }
    @POST
    @Consumes(CustomMediaType.APPLICATION_USER_PASSWORD)
    public Response createPasswordResetToken(@Valid @NotNull RecoverPasswordForm recoverPasswordForm){
        userService.setResetPasswordToken(recoverPasswordForm.getEmail());
        return Response.noContent().build();
    }
}
