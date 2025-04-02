package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

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
        mav.addObject("doctor", ds.findById(id).orElseThrow(UserNotFoundException::new));
        return mav;
    }
}
