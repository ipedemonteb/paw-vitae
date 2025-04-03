package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.ClientService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.models.Doctor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class SearchController {

    private DoctorService doctorService;

    @Autowired
    public SearchController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView searchBySpecialty(@RequestParam("specialty") String specialty) {
        List<Doctor> doctors = doctorService.getBySpecialty(specialty);
        return new ModelAndView("search/search").addObject("doctors", doctors).addObject("specialty", specialty);
    }
}
