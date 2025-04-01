package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.form.DoctorForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthController {

    private final DoctorService ds;
    private final CoverageService cs;
    private final CoverageService coverageService;

    @Autowired
    public AuthController(DoctorService ds, CoverageService cs, CoverageService coverageService) {
        this.ds = ds;
        this.cs = cs;
        this.coverageService = coverageService;
    }

    //    @RequestMapping("/login")
//    public ModelAndView login() {
//        return new ModelAndView("auth/login");
//    }
//    @RequestMapping("/register")
//    public ModelAndView register() {
//        return new ModelAndView("auth/register");
//    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(@Valid @ModelAttribute("registerForm") final DoctorForm doctorForm, final BindingResult errors) {

        if(errors.hasErrors()) {
            return doctorForm(doctorForm);
        }

       String coverageName = doctorForm.getCoverages().getFirst().getName();

        if(cs.findByName(coverageName).isEmpty()) {
            return doctorForm(doctorForm);
        }

        Coverage coverage = cs.findByName(coverageName).get();

        final Doctor doctor = ds.create(doctorForm.getName(), doctorForm.getLastName(), doctorForm.getEmail(), doctorForm.getPassword(), doctorForm.getPhone(), doctorForm.getSpecialty(), List.of(coverage));
        return new ModelAndView("redirect:/" + doctor.getId());
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView doctorForm(@ModelAttribute("registerForm") final DoctorForm doctorForm) {
        List<Coverage> coverageList = cs.getAll().orElse(new ArrayList<>());
        ModelAndView mav = new ModelAndView("/auth/register");
        mav.addObject("coverageList", coverageList);
        return mav;
    }

}
