package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.RatingService;
import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import ar.edu.itba.paw.interfaceServices.UserService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.print.Doc;
import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;


@Controller
public class LandingPageController {

    private final SpecialtyService ss;
    private final UserService us;
    private final RatingService rs;

    @Autowired
    public LandingPageController(SpecialtyService ss, UserService us, RatingService rs) {
        this.ss = ss;
        this.us = us;
        this.rs = rs;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView mav(@ModelAttribute("loggedUser") final User user) {
        List<Specialty> specialties = ss.getAll();
        return new ModelAndView("landingPage/home").addObject("specialties", specialties).addObject("imageId", us.getImageId(user)).addObject("ratings", rs.getFiveTopRatings());
    }

    @RequestMapping(value = "/about-us")
    public ModelAndView aboutUs() {
        return new ModelAndView("landingPage/about-us");
    }

}
