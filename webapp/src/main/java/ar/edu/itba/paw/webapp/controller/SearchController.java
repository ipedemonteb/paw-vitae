package ar.edu.itba.paw.webapp.controller;
import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.RatingService;
import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.paging.ParamCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
public class SearchController {

    private final DoctorService doctorService;
    private final SpecialtyService specialtyService;
    private final CoverageService coverageService;
    private final RatingService ratingService;

    @Autowired
    public SearchController(DoctorService doctorService, SpecialtyService specialtyService, CoverageService coverageService, RatingService ratingService) {
        this.doctorService = doctorService;
        this.specialtyService = specialtyService;
        this.coverageService = coverageService;
        this.ratingService = ratingService;
    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView searchBySpecialty(
            @ParamCustomizer(defaultValue = 0,paramName = "specialty") QueryParam specialtyId,
            @ParamCustomizer( defaultValue = 1) QueryParam page,
            @ParamCustomizer(defaultValue = 0,paramName = "coverage") QueryParam coverageId,
            @ParamCustomizer(paramName = "weekdays") List<QueryParam> weekdays,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "view", required = false, defaultValue = "grid") String view
    ) {
        List<Coverage> coverages = coverageService.getAll();
        Page<Doctor> doctorPage = doctorService.getWithFilters(specialtyId.getValue(), coverageId.getValue(), weekdays, orderBy, direction, (int) page.getValue(), 9);
        List<Specialty> allSpecialties = specialtyService.getAll();
        ModelAndView mav = new ModelAndView("search/search");
        mav.addObject("coverages", coverages);
        mav.addObject("doctors", doctorPage.getContent());
        mav.addObject("totalDoctors", doctorPage.getTotalElements());
        mav.addObject("allSpecialties", allSpecialties);
        mav.addObject("currentPage", page.getValue());
        mav.addObject("specialtyId", specialtyId.getValue());
        mav.addObject("coverageId", coverageId.getValue());
        mav.addObject("totalPages", doctorPage.getTotalPages());
        mav.addObject("view", view);
        return mav;
    }

    @RequestMapping(value = "/search/{doctorId}", method = RequestMethod.GET)
    public ModelAndView searchByDoctorId(@PathVariable("doctorId") Long doctorId, @ModelAttribute("loggedUser") User loggedUser) {
        Doctor doctor = doctorService.getById(doctorId).orElseThrow(UserNotFoundException::new);
        ModelAndView mav = new ModelAndView("search/doctor-public-profile");
        mav.addObject("doctor", doctor);
        mav.addObject("loggedUser", loggedUser);
        mav.addObject("doctorRatings", ratingService.getRatingsByDoctorId(doctorId));
        return mav;
    }

}
