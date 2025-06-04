package ar.edu.itba.paw.webapp.controller;
import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.DoctorProfileForm;
import ar.edu.itba.paw.webapp.form.UpdateDoctorForm;
import ar.edu.itba.paw.webapp.paging.ParamCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.*;

@Controller
public class SearchController {

    private final DoctorService doctorService;
    private final SpecialtyService specialtyService;
    private final CoverageService coverageService;
    private final RatingService ratingService;
    private final DoctorProfileService doctorProfileService;
    private final DoctorCertificationService doctorCertificationService;
    private final DoctorExperienceService doctorExperienceService;
    private final DoctorOfficeService doctorOfficeService;

    @Autowired
    public SearchController(DoctorService doctorService, SpecialtyService specialtyService, CoverageService coverageService, RatingService ratingService, DoctorProfileService doctorProfileService, DoctorCertificationService doctorCertificationService, DoctorExperienceService doctorExperienceService, DoctorOfficeService doctorOfficeService) {
        this.doctorService = doctorService;
        this.specialtyService = specialtyService;
        this.coverageService = coverageService;
        this.ratingService = ratingService;
        this.doctorProfileService = doctorProfileService;
        this.doctorCertificationService = doctorCertificationService;
        this.doctorExperienceService = doctorExperienceService;
        this.doctorOfficeService = doctorOfficeService;
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

    @RequestMapping(value = "/search/doctors/{keyword}", method = RequestMethod.GET)
    @ResponseBody
    public String searchByKeyword(
            @PathVariable("keyword") String keyword ){
        return doctorService.search(keyword, 10);
    }

    @RequestMapping(value = "/search/{doctorId}", method = RequestMethod.GET)
    public ModelAndView searchByDoctorId(@PathVariable("doctorId") Long doctorId,
                                         @ModelAttribute("loggedUser") User loggedUser,
                                         @ModelAttribute("doctorProfileForm") final DoctorProfileForm doctorProfileForm) {
        Doctor doctor = doctorService.getById(doctorId).orElseThrow(UserNotFoundException::new);
        ModelAndView mav = new ModelAndView("search/doctor-public-profile");
        mav.addObject("doctor", doctor);
        mav.addObject("loggedUser", loggedUser);
        mav.addObject("doctorRatings", ratingService.getRatingsByDoctorId(doctorId));
        mav.addObject("offices", doctorOfficeService.getByDoctorId(doctorId));
        mav.addObject("userId", doctorId);
        return mav;
    }

    @RequestMapping(value = "/doctor/profile/update", method = RequestMethod.POST)
    public ModelAndView updateDoctor(@Valid @ModelAttribute("doctorProfileForm") final DoctorProfileForm doctorProfileForm,
                                     final BindingResult errors,
                                     @ModelAttribute("loggedUser") final Doctor doctor
    ) {
        if (errors.hasErrors()) {
            return searchByDoctorId(doctor.getId(), doctor, doctorProfileForm);
        }

        doctorProfileService.update(doctor, doctorProfileForm.getBiography(), doctorProfileForm.getDescription());
        doctorCertificationService.update(doctor, doctorProfileForm.getCertificates());
        doctorExperienceService.update(doctor, doctorProfileForm.getExperiences());


        return new ModelAndView("redirect:/search/" + doctor.getId() + "?updated=true");
    }
}
