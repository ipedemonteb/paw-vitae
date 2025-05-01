package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaceServices.UserService;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.security.Principal;
import java.util.Optional;

@ControllerAdvice
public class GlobalModelAttributes {

    private final UserService userService;

    @Autowired
    public GlobalModelAttributes(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute("loggedUser")
    public Optional<? extends User> loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return userService.getByEmail((String) auth.getName());
    }

}
