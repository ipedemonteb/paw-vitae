package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Specialty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.swing.text.html.Option;
import java.util.*;
import java.util.stream.Collectors;


@Controller
public class LandingPageController {
    private final SpecialtyService ss;
    private final DoctorService ds;

    public LandingPageController(SpecialtyService ss, DoctorService ds) {
        this.ss = ss;
        this.ds = ds;
    }

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ModelAndView mav() {
        Optional<List<Specialty>> specialties = ss.getAll();
        return new ModelAndView("landingPage/home").addObject("specialties", specialties.orElse(new ArrayList<>()));
    }

}
