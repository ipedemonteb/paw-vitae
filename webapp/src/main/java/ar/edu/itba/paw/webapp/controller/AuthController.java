package ar.edu.itba.paw.webapp.controller;



import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.form.DoctorForm;
import ar.edu.itba.paw.webapp.form.PatientForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class AuthController {

    private final DoctorService ds;
    private final CoverageService cs;
    private final ImageService is;
    private final SpecialtyService ss;
    private final ClientService cls;
    private final UserService us;


    @Autowired
    public AuthController(DoctorService ds, CoverageService cs, ImageService is, SpecialtyService ss, ClientService clientService, UserService us) {
        this.ds = ds;
        this.cs = cs;
        this.is=is;
        this.ss = ss;
        this.cls = clientService;
        this.us = us;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(@Valid @ModelAttribute("registerForm") final DoctorForm doctorForm, final BindingResult errors)  {

        if(errors.hasErrors()) {
            return doctorForm(doctorForm);
        }

        final Doctor doctor = ds.create(doctorForm.getName(), doctorForm.getLastName(), doctorForm.getEmail(), doctorForm.getPassword(), doctorForm.getPhone(), doctorForm.getSpecialties(), doctorForm.getCoverages());

        try {
            is.create(doctor.getId(), doctorForm.getImage());
        } catch (IOException e) {
            errors.reject("image.upload.error", "Failed to upload image");
            return doctorForm(doctorForm);
        }

        return new ModelAndView("redirect:/" + doctor.getId());
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView doctorForm(@ModelAttribute("registerForm") final DoctorForm doctorForm) {
        List<Coverage> coverageList = cs.getAll().orElse(new ArrayList<>());
        List<Specialty> specialtyList = ss.getAll().orElse(new ArrayList<>());
        ModelAndView mav = new ModelAndView("auth/register");
        mav.addObject("coverageList", coverageList);
        mav.addObject("specialtyList", specialtyList);
        return mav;
    }


    @RequestMapping(value = "/register-patient", method = RequestMethod.GET)
    public ModelAndView patientForm(@ModelAttribute("patientForm") final PatientForm pacientForm) {
        List<Coverage> coverageList = cs.getAll().orElse(new ArrayList<>());
        ModelAndView mav = new ModelAndView("auth/register-patient");
        mav.addObject("coverageList", coverageList);
        return mav;
    }

    @RequestMapping(value = "/register-patient", method = RequestMethod.POST)
    public ModelAndView registerPatient(@Valid @ModelAttribute("patientForm") final PatientForm patientForm, final BindingResult errors)  {

        if(errors.hasErrors()) {
            return patientForm(patientForm);
        }

        final Client client = cls.create(patientForm.getName(), patientForm.getLastName(), patientForm.getEmail(), patientForm.getPassword(), patientForm.getPhone(), patientForm.getCoverage());

        try {
            is.create(client.getId(), patientForm.getImage());
        } catch (IOException e) {
            errors.reject("image.upload.error", "Failed to upload image");
            return patientForm(patientForm);
        }

        return new ModelAndView("redirect:/" + client.getId());
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("auth/login");
    }

    @RequestMapping("/logout")
    public ModelAndView logout() {
        return new ModelAndView("redirect:/");
    }

}

