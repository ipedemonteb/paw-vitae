package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.*;
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
    private final DoctorService doctorService;
    private final PatientService patientService;

    @Autowired
    public LandingPageController(SpecialtyService specialtyService, UserService userService, RatingService ratingService, DoctorService doctorService, PatientService patientService) {
        this.specialtyService = specialtyService;
        this.userService = userService;
        this.ratingService = ratingService;
        this.doctorService = doctorService;
        this.patientService = patientService;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView mav(@ModelAttribute("loggedUser") final User user) {
        List<Specialty> specialties = specialtyService.getAll();
        return new ModelAndView("landingPage/home").addObject("userId", user.getId()).addObject("specialties", specialties).addObject("imageId", userService.getImageId(user)).addObject("ratings", ratingService.getFiveTopRatings()).addObject("doctorCount", doctorService.getAllDoctorsDisplayCount()).addObject("patientsCount", patientService.getAllPatientsDisplayCount());
    }

    @RequestMapping(value = "/about-us")
    public ModelAndView aboutUs(@ModelAttribute("loggedUser") final User user) {
        return new ModelAndView("landingPage/about-us").addObject("userId", user.getId()).addObject("imageId", userService.getImageId(user));
    }

}
