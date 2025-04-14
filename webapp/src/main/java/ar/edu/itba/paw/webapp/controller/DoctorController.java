package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.User;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.DoctorForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import ar.edu.itba.paw.webapp.controller.AuthController.*;

import javax.print.Doc;
import javax.validation.Valid;

@Controller
public class DoctorController {

    private final DoctorService ds;

    @Autowired
    public DoctorController(DoctorService ds) {
        this.ds = ds;
    }

    @RequestMapping(value = "/{id:\\d+}", method = {RequestMethod.GET})
    public ModelAndView getUser(@PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("doctor/doctor");
        mav.addObject("doctor", ds.getById(id).orElseThrow(UserNotFoundException::new));
        return mav;
    }


    @RequestMapping(value = "/doctor/dashboard")
    public ModelAndView getDoctorDashboard() {
        final ModelAndView mav = new ModelAndView("doctor/dashboard");
        Doctor doctor = loggedUser();
        mav.addObject("doctor", doctor);
        return mav;
    }

//    @RequestMapping(value = "/doctor/update", method = RequestMethod.POST)
//    public ModelAndView updateDoctor(@Valid @ModelAttribute("doctorForm") final DoctorForm doctorForm, final BindingResult errors) {
//        if (errors.hasErrors()) {
//            return new ModelAndView("doctor/edit");
//        }
//
//        Doctor doctor = loggedUser();
//        ds.updateDoctor(doctor.getId(), doctorForm.getName(), doctorForm.getLastName(), doctorForm.getEmail(), doctorForm.getPhone(), doctorForm.getSpecialties(), doctorForm.getCoverages());
//
//        return new ModelAndView("redirect:/doctor/dashboard");
//    }

    @ModelAttribute
    public Doctor loggedUser() {
        final Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        return ds.getByEmail((String) auth.getName()).orElseThrow(UserNotFoundException::new);
    }

}
