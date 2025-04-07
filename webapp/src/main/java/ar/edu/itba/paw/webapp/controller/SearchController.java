package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.ClientService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Specialty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Optional;

@Controller
public class SearchController {

    private DoctorService doctorService;
    private SpecialtyService specialtyService;

    @Autowired
    public SearchController(DoctorService doctorService, SpecialtyService specialtyService) {
        this.doctorService = doctorService;
        this.specialtyService = specialtyService;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView searchBySpecialty(@RequestParam("specialty") long specialty) {
        Optional<Specialty> specialtyObj = specialtyService.findById(specialty);
        List<Doctor> doctors = doctorService.getBySpecialty(specialtyObj.map(Specialty::getKey).orElse(null));
        return new ModelAndView("search/search").addObject("doctors", doctors).addObject("specialty", specialtyObj.orElse(null));
    }
}
