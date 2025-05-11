package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.RatingService;
import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import ar.edu.itba.paw.interfaceServices.UserService;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;


@Controller
public class LandingPageController {

    private final SpecialtyService specialtyService;
    private final UserService userService;
    private final RatingService ratingService;

    @Autowired
    public LandingPageController(SpecialtyService specialtyService, UserService userService, RatingService ratingService) {
        this.specialtyService = specialtyService;
        this.userService = userService;
        this.ratingService = ratingService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView mav(@ModelAttribute("loggedUser") final User user) {
        List<Specialty> specialties = specialtyService.getAll();
        return new ModelAndView("landingPage/home").addObject("specialties", specialties).addObject("imageId", userService.getImageId(user)).addObject("ratings", ratingService.getFiveTopRatings());
    }

    @RequestMapping(value = "/about-us")
    public ModelAndView aboutUs(@ModelAttribute("loggedUser") final User user) {
        return new ModelAndView("landingPage/about-us").addObject("imageId", userService.getImageId(user));
    }

}
