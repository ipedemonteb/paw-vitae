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

    @Autowired
    public AuthController(DoctorService ds, CoverageService cs) {
        this.ds = ds;
        this.cs = cs;
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
            System.out.println("Errors: " + errors.getAllErrors());
            return doctorForm(doctorForm);
        }

        String coverageName = doctorForm.getCoverages().getFirst();

        if(cs.findByName(coverageName).isEmpty()) {
            return doctorForm(doctorForm);
        }
        Coverage coverage = cs.findByName(coverageName).get();

        System.out.println("Coverage: " + coverage.getName());

        final Doctor doctor = ds.create(doctorForm.getName(), doctorForm.getLastName(), doctorForm.getEmail(), doctorForm.getPassword(), doctorForm.getPhone(), doctorForm.getSpecialtyAsString(), List.of(coverage));
        return new ModelAndView("redirect:/" + doctor.getId());
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView doctorForm(@ModelAttribute("registerForm") final DoctorForm doctorForm) {
        List<Coverage> coverageList = cs.getAll().orElse(new ArrayList<>());
        System.out.println("CoverageList: " + coverageList);
        List<String> specialtyList = List.of("General Medicine", "Cardiology", "Dermatology","Endocrinology", "Gastroenterology", "Hematology", "Infectious Disease", "Nephrology", "Neurology", "Oncology", "Pulmonology", "Rheumatology", "Urology");
        ModelAndView mav = new ModelAndView("auth/register");
        mav.addObject("coverageList", coverageList);
        mav.addObject("specialtyList", specialtyList);
        return mav;
    }

}

