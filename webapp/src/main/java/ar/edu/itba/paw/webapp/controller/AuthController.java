package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.auth.AuthService;
import ar.edu.itba.paw.webapp.auth.AuthUserDetailsService;
import ar.edu.itba.paw.webapp.form.ChangePasswordForm;
import ar.edu.itba.paw.webapp.form.DoctorForm;
import ar.edu.itba.paw.webapp.form.PatientForm;
import ar.edu.itba.paw.webapp.form.RecoverPasswordForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class AuthController {

    private final DoctorService ds;
    private final CoverageService cs;
    private final ImageService is;
    private final SpecialtyService ss;
    private final PatientService ps;
    private final UserService us;
    private final MailService ms;

    private final AuthService authService;

    @Autowired
    public AuthController(DoctorService ds, CoverageService cs, ImageService is, SpecialtyService ss, PatientService patientService, UserService us, AuthService authService, MailService ms) {
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
        if (errors.hasErrors()) {
            return doctorForm(form);
        }
        ds.create(
                form.getName(), form.getLastName(), form.getEmail(), form.getPassword(),
                form.getPhone(), LocaleContextHolder.getLocale().getLanguage(),form.getImage(),form.getSpecialties(),
                form.getCoverages(), form.getAvailabilitySlots()
        );
        Optional<User> userOpt = us.getByEmail(form.getEmail()).map(user -> (User) user);
        if (userOpt.isEmpty()) {
            return new ModelAndView("redirect:/email-sent");
        }
        us.setVerificationToken(userOpt.get());
        return new ModelAndView("redirect:/email-sent");
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView doctorForm(@ModelAttribute("registerForm") final DoctorForm doctorForm) {
        List<Coverage> coverageList = cs.getAll();
        List<Specialty> specialtyList = ss.getAll();
        ModelAndView mav = new ModelAndView("auth/register");
        mav.addObject("coverageList", coverageList);
        mav.addObject("specialtyList", specialtyList);
        return mav;
    }

    @RequestMapping(value = "/register-patient", method = RequestMethod.GET)
    public ModelAndView patientForm(@ModelAttribute("patientForm") final PatientForm patientForm) {
        List<Coverage> coverageList = cs.getAll();
        ModelAndView mav = new ModelAndView("auth/register-patient");
        mav.addObject("coverageList", coverageList);
        return mav;
    }

    @RequestMapping(value = "/register-patient", method = RequestMethod.POST)
    public ModelAndView registerPatient(@Valid @ModelAttribute("patientForm") final PatientForm patientForm, final BindingResult errors)  {
        if(errors.hasErrors()) {
            return patientForm(patientForm);
        }
        ps.create(patientForm.getName(), patientForm.getLastName(), patientForm.getEmail(), patientForm.getPassword(),
                patientForm.getPhone(), LocaleContextHolder.getLocale().getLanguage(),patientForm.getCoverage() );
        Optional<User> userOpt = us.getByEmail(patientForm.getEmail()).map(user -> (User) user);
        if (userOpt.isEmpty()) {
            return new ModelAndView("redirect:/email-sent");
        }
        us.setVerificationToken(userOpt.get());
        return new ModelAndView("redirect:/email-sent");
    }

    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("auth/login");
    }

    @RequestMapping("/logout")
    public ModelAndView logout() {
        return new ModelAndView("redirect:/");
    }

    @RequestMapping(value="/recover-password", method = RequestMethod.GET)
    public ModelAndView recoverPassword(@ModelAttribute("recoverPasswordForm") final RecoverPasswordForm recoverPasswordForm) {
        return new ModelAndView("auth/recover-password");
    }

    @RequestMapping(value = "/recover-password", method = RequestMethod.POST)
    public ModelAndView recoverPassword(@Valid @ModelAttribute("recoverPasswordForm") RecoverPasswordForm form, BindingResult errors) {
        if (errors.hasErrors()) {
            return new ModelAndView("auth/recover-password");
        }
        Optional<User> userOpt = us.getByEmail(form.getEmail()).map(user -> (User) user);
        if (userOpt.isEmpty()) {
            return new ModelAndView("redirect:/recover-password?recover=sent");
        }

        us.setResetPasswordToken(userOpt.get());
        return new ModelAndView("redirect:/recover-password?recover=sent");
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.GET)
    public ModelAndView changePassword(@RequestParam(value = "token", required = false) String token, @ModelAttribute("ChangePasswordForm") ChangePasswordForm ChangePasswordForm) {
        if (token == null) {
            return new ModelAndView("redirect:/");
        }
        return new ModelAndView("auth/change-password").addObject("token", token);
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
    public ModelAndView changePassword(@RequestParam(value = "token", required = false) String token,
                                       @Valid @ModelAttribute("ChangePasswordForm") ChangePasswordForm form, BindingResult errors) {
        if (errors.hasErrors()) {
            return new ModelAndView("auth/change-password").addObject("token", token);
        }

        boolean success = us.changePassword(token, form.getPassword());

        if (success) {
            return new ModelAndView("redirect:/change-password-result?success=true");
        } else {
            return new ModelAndView("redirect:/change-password-result?success=false");
        }
    }

    @RequestMapping(value = "/change-password-result", method = RequestMethod.GET)
    public ModelAndView changePasswordResult() {
        return new ModelAndView("auth/change-password-result");
    }

    @RequestMapping("/email-sent")
    public ModelAndView emailSent() {
        return new ModelAndView("auth/email-sent");
    }

    @RequestMapping("/verify")
    public ModelAndView verifyAccount(@RequestParam(value = "token",required = false) String token) {
        if (token == null) {
            return new ModelAndView("auth/verify");
        }
        boolean success = authService.verifyAndLoginUser(token);
        if (success) {
            return new ModelAndView("redirect:/verify?success=true");
        } else {
            return new ModelAndView("redirect:/verify?success=false");
        }
    }
    @RequestMapping("/verify-result")
    public ModelAndView verifyResult() {
        return new ModelAndView("auth/verify");
    }


    @RequestMapping("/verify-confirmation")
    public ModelAndView verifyConfirmation(@RequestParam(value = "token", required = false) String token) {
        if (token == null) {
            return new ModelAndView("redirect:/");
        }
        ModelAndView mav = new ModelAndView("auth/verify-confirmation");
        mav.addObject("token", token);
        return mav;
    }
}
