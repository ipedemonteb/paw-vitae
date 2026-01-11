package ar.edu.itba.paw.webapp.controller;

import org.springframework.stereotype.Component;

import javax.ws.rs.HEAD;
import javax.ws.rs.Path;

@Path("/")
@Component
public class RestUserController {


    //TODO: SEE IF THERES A BETTER WAY FOR THIS TO WORK WHEN LOGING IN
    @HEAD
    public void head() {

    }
}
