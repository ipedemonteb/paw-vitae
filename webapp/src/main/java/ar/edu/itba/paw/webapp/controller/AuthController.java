package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.auth.AuthService;
import ar.edu.itba.paw.webapp.form.ChangePasswordForm;
import ar.edu.itba.paw.webapp.form.DoctorForm;
import ar.edu.itba.paw.webapp.form.PatientForm;
import ar.edu.itba.paw.webapp.form.RecoverPasswordForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
public class AuthController {

    private final DoctorService doctorService;
    private final CoverageService coverageService;
    private final SpecialtyService specialtyService;
    private final NeighborhoodService neighborhoodService;
    private final PatientService patientService;
    private final UserService userService;
    private final AuthService authService;

    @Autowired
    public AuthController(DoctorService doctorService, CoverageService coverageService, SpecialtyService specialtyService, PatientService patientService, UserService userService, AuthService authService, NeighborhoodService neighborhoodService) {
        this.doctorService = doctorService;
        this.coverageService = coverageService;
        this.specialtyService = specialtyService;
        this.patientService = patientService;
        this.userService = userService;
        this.authService = authService;
        this.neighborhoodService = neighborhoodService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(@Valid @ModelAttribute("registerForm") DoctorForm form, BindingResult errors) {
        if (errors.hasErrors()) {
            return doctorForm(form);
        }
//        doctorService.create(
//                form.getName(), form.getLastName(), form.getEmail(), form.getPassword(),
//                form.getPhone(), LocaleContextHolder.getLocale(), form.getImage(), form.getSpecialties(),
//                form.getCoverages(), form.getDoctorOfficeForm()
//        );
//        Optional<User> userOpt = userService.getByEmail(form.getEmail()).map(user -> (User) user);
//        if (userOpt.isEmpty()) {
//            return new ModelAndView("redirect:/email-sent");
//        }
//        userService.setVerificationToken(form.getEmail());
        return new ModelAndView("redirect:/email-sent");
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView doctorForm(@ModelAttribute("registerForm") final DoctorForm doctorForm) {
        List<Coverage> coverageList = coverageService.getAll();
        List<Specialty> specialtyList = specialtyService.getAll();
        ModelAndView mav = new ModelAndView("auth/register");
        mav.addObject("coverageList", coverageList);
        mav.addObject("specialtyList", specialtyList);
        mav.addObject("neighborhoodList", neighborhoodService.getAll());
        return mav;
    }

    @RequestMapping(value = "/register-patient", method = RequestMethod.GET)
    public ModelAndView patientForm(@ModelAttribute("patientForm") final PatientForm patientForm) {
        List<Coverage> coverageList = coverageService.getAll();
        ModelAndView mav = new ModelAndView("auth/register-patient");
        mav.addObject("coverageList", coverageList);
        mav.addObject("neighborhoodList", neighborhoodService.getAll());
        return mav;
    }

    @RequestMapping(value = "/register-patient", method = RequestMethod.POST)
    public ModelAndView registerPatient(@Valid @ModelAttribute("patientForm") final PatientForm patientForm, final BindingResult errors) {
        if (errors.hasErrors()) {
            return patientForm(patientForm);
        }
//        patientService.create(patientForm.getName(), patientForm.getLastName(), patientForm.getEmail(), patientForm.getPassword(),
//                patientForm.getPhone(), LocaleContextHolder.getLocale().getLanguage(), patientForm.getCoverage(), patientForm.getNeighborhoodId());
//        userService.setVerificationToken(patientForm.getEmail());
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

    @RequestMapping(value = "/recover-password", method = RequestMethod.GET)
    public ModelAndView recoverPassword(@ModelAttribute("recoverPasswordForm") final RecoverPasswordForm recoverPasswordForm) {
        return new ModelAndView("auth/recover-password");
    }

    @RequestMapping(value = "/recover-password", method = RequestMethod.POST)
    public ModelAndView recoverPassword(@Valid @ModelAttribute("recoverPasswordForm") RecoverPasswordForm form, BindingResult errors) {
        if (errors.hasErrors()) {
            return new ModelAndView("auth/recover-password");
        }
        userService.setResetPasswordToken(form.getEmail());
        return new ModelAndView("redirect:/recover-password?recover=sent");
    }

    @RequestMapping(value = "/change-password", method = RequestMethod.GET)
    public ModelAndView changePassword(@RequestParam(value = "token", required = false) String token, @ModelAttribute("ChangePasswordForm") ChangePasswordForm ChangePasswordForm) {
        if (token == null ){
            return new ModelAndView("redirect:/");
        }
        if(!userService.verifyRecoveryToken(token)){
            return new ModelAndView("redirect:/change-password-result?success=false");
        }
        return new ModelAndView("auth/change-password").addObject("token", token);
    }

//    @RequestMapping(value = "/change-password", method = RequestMethod.POST)
//    public ModelAndView changePassword(@RequestParam(value = "token", required = false) String token,
//                                       @Valid @ModelAttribute("ChangePasswordForm") ChangePasswordForm form, BindingResult errors) {
//        if (errors.hasErrors()) {
//            return new ModelAndView("auth/change-password").addObject("token", token);
//        }
//        boolean success = userService.changePassword(token, form.getPassword());
//        String value = String.valueOf(success);
//            return new ModelAndView("redirect:/change-password-result?success="+value);
//
//    }

    @RequestMapping(value = "/change-password-result", method = RequestMethod.GET)
    public ModelAndView changePasswordResult() {
        return new ModelAndView("auth/change-password-result");
    }

    @RequestMapping("/email-sent")
    public ModelAndView emailSent() {
        return new ModelAndView("auth/email-sent");
    }

    @RequestMapping("/verify")
    public ModelAndView verifyAccount(@ModelAttribute("loggedUser") User user) {
            return new ModelAndView("auth/verify").addObject("imageId", userService.getImageId(user).orElse(null));
    }

    @RequestMapping("/verify-confirmation")
    public ModelAndView verifyConfirmation(@RequestParam(value = "token", required = false) String token) {
        if (token == null) {
            return new ModelAndView("redirect:/");
        }
        boolean success = authService.verifyAndLoginUser(token);
        String value = String.valueOf(success);
        return new ModelAndView("redirect:/verify?success="+value);
    }
}
