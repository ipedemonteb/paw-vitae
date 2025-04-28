package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Page;
import ar.edu.itba.paw.models.Specialty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.*;

@Controller
public class SearchController {

    private final DoctorService doctorService;
    private final SpecialtyService specialtyService;
    private final AppointmentService appointmentService;

    @Autowired
    public SearchController(DoctorService doctorService, SpecialtyService specialtyService, AppointmentService appointmentService) {
        this.doctorService = doctorService;
        this.specialtyService = specialtyService;
        this.appointmentService = appointmentService;
    }


    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView searchBySpecialty(
            @RequestParam(value = "specialty", required = false, defaultValue = "0") Long specialtyId,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "coverage", required = false, defaultValue = "0") Long coverageId,
            @RequestParam(value = "weekdays", required = false) List<Integer> weekdays,
            @RequestParam(value = "orderBy", defaultValue = "name") String orderBy,
            @RequestParam(value = "direction", defaultValue = "asc") String direction
    ) {
        Optional<Specialty> specialtyObj = specialtyService.getById(specialtyId);
//        Page<Doctor> doctorPage = doctorService.getBySpecialtyWithAppointments(specialtyId, page, 9); //TODO MAGIC PAGE NUMBER NOT GOOD.
        Page<Doctor> doctorPage = doctorService.getWithFilters(specialtyId, coverageId, weekdays, orderBy, direction, page, 9);
        List<Specialty> allSpecialties = specialtyService.getAll().orElse(new ArrayList<>());
        ModelAndView mav = new ModelAndView("search/search");
        mav.addObject("doctors", doctorPage.getContent());
        mav.addObject("totalDoctors", doctorPage.getTotalElements());
        mav.addObject("specialty", specialtyObj.orElse(null));
        mav.addObject("allSpecialties", allSpecialties);
        mav.addObject("currentPage", page);
        mav.addObject("totalPages", doctorPage.getTotalPages());
        return mav;
    }
}
