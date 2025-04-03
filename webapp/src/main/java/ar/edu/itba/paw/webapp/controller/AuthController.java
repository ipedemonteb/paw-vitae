package ar.edu.itba.paw.webapp.controller;



import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.ImageService;
import ar.edu.itba.paw.interfaceServices.SpecialtyService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class AuthController {

    private final DoctorService ds;
    private final CoverageService cs;
    private final ImageService is;
    private final SpecialtyService ss;
    @Autowired
    public AuthController(DoctorService ds, CoverageService cs, ImageService is, SpecialtyService ss) {
        this.ds = ds;
        this.cs = cs;
        this.is=is;
        this.ss = ss;
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
    public ModelAndView register(@Valid @ModelAttribute("registerForm") final DoctorForm doctorForm, final BindingResult errors)  {

        if(errors.hasErrors()) {
            System.out.println("Errors: " + errors.getAllErrors());
            return doctorForm(doctorForm);
        }
        List<Coverage> coverages = new ArrayList<>();
        doctorForm.getCoverages().forEach(coverage -> coverages.add(cs.findById(Long.parseLong(coverage)).orElse(null)));
        final Doctor doctor = ds.create(doctorForm.getName(), doctorForm.getLastName(), doctorForm.getEmail(), doctorForm.getPassword(), doctorForm.getPhone(), doctorForm.getSpecialtyAsString(),coverages);
        try {
            is.create(doctor.getId(), doctorForm.getImage().getBytes());
        } catch (IOException e) {
            errors.reject("image.upload.error", "Failed to upload image");
            return doctorForm(doctorForm);
        };
        return new ModelAndView("redirect:/" + doctor.getId());
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public ModelAndView doctorForm(@ModelAttribute("registerForm") final DoctorForm doctorForm) {
        List<Coverage> coverageList = cs.getAll().orElse(new ArrayList<>());
        ModelAndView mav = new ModelAndView("auth/register");
        mav.addObject("coverageList", coverageList);
        mav.addObject("specialtyList", ss.getSpecialties());
        return mav;
    }

}

