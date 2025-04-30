package ar.edu.itba.paw.webapp.controller;



import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.auth.AuthUserDetailsService;
import ar.edu.itba.paw.webapp.form.DoctorForm;
import ar.edu.itba.paw.webapp.form.PatientForm;
import ar.edu.itba.paw.webapp.form.RecoverPasswordForm;
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
import java.util.Optional;

@Controller
public class AuthController {

    private static final Logger LOGGER = LoggerFactory.getLogger(AuthController.class);


    private final DoctorService ds;
    private final CoverageService cs;
    private final ImageService is;
    private final SpecialtyService ss;
    private final PatientService ps;
    private final UserService us;
    private final MailService ms;

    private final AuthUserDetailsService authService;

    @Autowired
    public AuthController(DoctorService ds, CoverageService cs, ImageService is, SpecialtyService ss, PatientService patientService, UserService us, AuthUserDetailsService authService, MailService ms) {
        this.ds = ds;
        this.cs = cs;
        this.is = is;
        this.ss = ss;
        this.ps = patientService;
        this.us = us;
        this.authService = authService;
        this.ms = ms;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(@Valid @ModelAttribute("registerForm") DoctorForm form, BindingResult errors) {
//        if (authentication != null && authentication.isAuthenticated() && !(authentication instanceof AnonymousAuthenticationToken)) {
//            return new ModelAndView("redirect:/");
//        }
        if (errors.hasErrors()) {
            LOGGER.debug("Errors found registering: {}", errors.getAllErrors());
            return doctorForm(form);
        }
        authService.registerDoctor(form);
        LOGGER.debug("Registering doctor with email: {}", form.getEmail());
        Optional<User> userOpt = us.getByEmail(form.getEmail()).map(user -> (User) user);
        if (userOpt.isEmpty()) {
            return new ModelAndView("redirect:/register?register=sent");
        }

        User user = userOpt.get();

        String token = "TOKEN"; //TODO: replace with actual token generation
        String link = "https://yourapp.com/reset-password?token=" + token;

        ms.sendVerificationRegisterEmail(user, link);

        return new ModelAndView("redirect:/register?register=sent");
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView doctorForm(@ModelAttribute("registerForm") final DoctorForm doctorForm) {
//        if (authentication != null
//                && authentication.isAuthenticated()
//                && !(authentication instanceof AnonymousAuthenticationToken)) {
//            // remember‑me or fully authenticated ↦ bounce home
//            return new ModelAndView("redirect:/");
//        }
        List<Coverage> coverageList = cs.getAll();
        List<Specialty> specialtyList = ss.getAll();
        ModelAndView mav = new ModelAndView("auth/register");
        mav.addObject("coverageList", coverageList);
        mav.addObject("specialtyList", specialtyList);
        return mav;
    }


    @RequestMapping(value = "/register-patient", method = RequestMethod.GET)
    public ModelAndView patientForm(@ModelAttribute("patientForm") final PatientForm pacientForm) {
//        if (authentication != null
//                && authentication.isAuthenticated()
//                && !(authentication instanceof AnonymousAuthenticationToken)) {
//            // remember‑me or fully authenticated ↦ bounce home
//            return new ModelAndView("redirect:/");
//        }
        List<Coverage> coverageList = cs.getAll();
        ModelAndView mav = new ModelAndView("auth/register-patient");
        mav.addObject("coverageList", coverageList);
        return mav;
    }

    @RequestMapping(value = "/register-patient", method = RequestMethod.POST)
    public ModelAndView registerPatient(@Valid @ModelAttribute("patientForm") final PatientForm patientForm, final BindingResult errors)  {
//        if (authentication != null
//                && authentication.isAuthenticated()
//                && !(authentication instanceof AnonymousAuthenticationToken)) {
//            // remember‑me or fully authenticated ↦ bounce home
//            return new ModelAndView("redirect:/");
//        }
        if(errors.hasErrors()) {
            LOGGER.debug("Errors found registering patient: {}", errors.getAllErrors());
            return patientForm(patientForm);
        }
        authService.registerPatient(patientForm);
        Optional<User> userOpt = us.getByEmail(patientForm.getEmail()).map(user -> (User) user);
        if (userOpt.isEmpty()) {
            return new ModelAndView("redirect:/register-patient?register=sent");
        }
        User user = userOpt.get();
        String token = "TOKEN"; //TODO: Replace with actual token generation
        String link = "https://yourapp.com/?token=" + token;
        ms.sendVerificationRegisterEmail(user, link);
        return new ModelAndView("redirect:/register-patient?register=sent"); //TODO: redirect to a confirmation page
    }

    @RequestMapping("/login")
    public ModelAndView login() {
//        if (authentication != null
//                && authentication.isAuthenticated()
//                && !(authentication instanceof AnonymousAuthenticationToken)) {
//            // remember‑me or fully authenticated ↦ bounce home
//            return new ModelAndView("redirect:/");
//        }
        return new ModelAndView("auth/login");
    }

    @RequestMapping("/logout")
    public ModelAndView logout() {
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value="/recover-password", method = RequestMethod.GET)
    public ModelAndView recoverPassword(@ModelAttribute("recoverPasswordForm") final RecoverPasswordForm recoverPasswordForm) {
//        if (authentication != null
//                && authentication.isAuthenticated()
//                && !(authentication instanceof AnonymousAuthenticationToken)) {
//            // remember‑me or fully authenticated ↦ bounce home
//            return new ModelAndView("redirect:/");
//        }
        return new ModelAndView("auth/recover-password");
    }

    @RequestMapping(value = "/recover-password", method = RequestMethod.POST)
    public ModelAndView recoverPassword(@Valid @ModelAttribute("recoverPasswordForm") RecoverPasswordForm form, BindingResult errors) {
        if (errors.hasErrors()) {
            LOGGER.debug("Errors found recovering password: {}", errors.getAllErrors());
            return new ModelAndView("auth/recover-password");
        }
        Optional<User> userOpt = us.getByEmail(form.getEmail()).map(user -> (User) user);
        if (userOpt.isEmpty()) {
            return new ModelAndView("redirect:/recover-password?recover=sent");
        }

        User user = userOpt.get();
        String token = "TOKEN";
        String link = "https://yourapp.com/reset-password?token=" + token;
        ms.sendRecoverPasswordEmail(user, link);
        return new ModelAndView("redirect:/recover-password?recover=sent");
    }

}



