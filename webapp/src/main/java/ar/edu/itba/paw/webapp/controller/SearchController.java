package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import ar.edu.itba.paw.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.*;

@Controller
public class SearchController {

    private final DoctorService doctorService;
    private final SpecialtyService specialtyService;
    private final CoverageService coverageService;

    @Autowired
    public SearchController(DoctorService doctorService, SpecialtyService specialtyService, CoverageService coverageService) {
        this.doctorService = doctorService;
        this.specialtyService = specialtyService;
        this.coverageService = coverageService;
    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView searchBySpecialty(
            @RequestParam(value = "specialty", required = false, defaultValue = "0") Long specialtyId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "coverage", required = false, defaultValue = "0") Long coverageId,
            @RequestParam(value = "weekdays", required = false) List<Integer> weekdays,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction,
            @RequestParam(value = "view", required = false, defaultValue = "grid") String view
    ) {
        Optional<Specialty> specialtyObj = specialtyService.getById(specialtyId);
        List<Coverage> coverages = coverageService.getAll();
        Page<Doctor> doctorPage = doctorService.getWithFilters(specialtyId, coverageId, weekdays, orderBy, direction, page, 9);
        List<Specialty> allSpecialties = specialtyService.getAll();
        ModelAndView mav = new ModelAndView("search/search");
        mav.addObject("coverages", coverages);
        mav.addObject("doctors", doctorPage.getContent());
        mav.addObject("totalDoctors", doctorPage.getTotalElements());
        mav.addObject("specialty", specialtyObj.orElse(null));
        mav.addObject("allSpecialties", allSpecialties);
        mav.addObject("currentPage", page);
        mav.addObject("totalPages", doctorPage.getTotalPages());
        mav.addObject("view", view);
        return mav;
    }
}
