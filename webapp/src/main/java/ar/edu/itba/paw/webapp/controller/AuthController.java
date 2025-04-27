package ar.edu.itba.paw.webapp.controller;



import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.auth.AuthUserDetailsService;
import ar.edu.itba.paw.webapp.form.DoctorForm;
import ar.edu.itba.paw.webapp.form.PatientForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
@Controller
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);


    private final DoctorService ds;
    private final CoverageService cs;
    private final ImageService is;
    private final SpecialtyService ss;
    private final PatientService ps;
    private final UserService us;

    private final AuthUserDetailsService authService;

    @Autowired
    public AuthController(DoctorService ds, CoverageService cs, ImageService is, SpecialtyService ss, PatientService patientService, UserService us, AuthUserDetailsService authService) {
        this.ds = ds;
        this.cs = cs;
        this.is=is;
        this.ss = ss;
        this.ps = patientService;
        this.us = us;
        this.authService = authService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(@Valid @ModelAttribute("registerForm") DoctorForm form, BindingResult errors, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
            return new ModelAndView("redirect:/");
        }
        if (errors.hasErrors()) {
            LOGGER.debug("Errors found registering: {}", errors.getAllErrors());
            return doctorForm(form, authentication);
        }
        authService.registerDoctor(form);

    LOGGER.debug("Registering doctor with email: {}", form.getEmail());

        return new ModelAndView("redirect:/doctor/dashboard");
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView doctorForm(@ModelAttribute("registerForm") final DoctorForm doctorForm, Authentication authentication) {
        if (authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            // remember‑me or fully authenticated ↦ bounce home
            return new ModelAndView("redirect:/");
        }
        List<Coverage> coverageList = cs.getAll().orElse(new ArrayList<>());
        List<Specialty> specialtyList = ss.getAll().orElse(new ArrayList<>());
        ModelAndView mav = new ModelAndView("auth/register");
        mav.addObject("coverageList", coverageList);
        mav.addObject("specialtyList", specialtyList);
        return mav;
    }


    @RequestMapping(value = "/register-patient", method = RequestMethod.GET)
    public ModelAndView patientForm(@ModelAttribute("patientForm") final PatientForm pacientForm, Authentication authentication) {
        if (authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            // remember‑me or fully authenticated ↦ bounce home
            return new ModelAndView("redirect:/");
        }
        List<Coverage> coverageList = cs.getAll().orElse(new ArrayList<>());
        ModelAndView mav = new ModelAndView("auth/register-patient");
        mav.addObject("coverageList", coverageList);
        return mav;
    }

    @RequestMapping(value = "/register-patient", method = RequestMethod.POST)
    public ModelAndView registerPatient(@Valid @ModelAttribute("patientForm") final PatientForm patientForm, final BindingResult errors,Authentication authentication)  {
        if (authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            // remember‑me or fully authenticated ↦ bounce home
            return new ModelAndView("redirect:/");
        }
        if(errors.hasErrors()) {
            LOGGER.debug("Errors found registering patient: {}", errors.getAllErrors());
            return patientForm(patientForm, authentication);
        }
        authService.registerPatient(patientForm);
        return new ModelAndView("redirect:/patient/dashboard");
    }

    @RequestMapping("/login")
    public ModelAndView login(Authentication authentication) {
        if (authentication != null
                && authentication.isAuthenticated()
                && !(authentication instanceof AnonymousAuthenticationToken)) {
            // remember‑me or fully authenticated ↦ bounce home
            return new ModelAndView("redirect:/");
        }
        return new ModelAndView("auth/login");
    }

    @RequestMapping("/logout")
    public ModelAndView logout() {
        return new ModelAndView("redirect:/");
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                if (text == null || text.isEmpty()) {
                    setValue(null);
                } else {
                    // Parse the time string (format: HH:mm)
                    setValue(LocalTime.parse(text));
                }
            }
        });
    }
}



