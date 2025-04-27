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

    private static final Logger LOGGER = LoggerFactory.getLogger(SearchController.class);

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
            @RequestParam("specialty") long specialty,
            @RequestParam(value = "page", defaultValue = "1") int page) {

        // Get specialty and all doctors with that specialty
        Optional<Specialty> specialtyObj = specialtyService.getById(specialty);
        Page<Doctor> doctorPage = doctorService.getBySpecialtyWithAppointments(specialty, page, 9); //TODO MAGIC PAGE NUMBER NOT GOOD.
        List<Doctor> paginatedDoctors = doctorPage.getContent();
        List<Specialty> allSpecialties = specialtyService.getAll().orElse(new ArrayList<>());

//        Map<Long, List<Appointment>> futureAppointmentsMap = appointmentService.getAllFutureAppointments(paginatedDoctors).orElse(new HashMap<>());

        ModelAndView mav = new ModelAndView("search/search");
        mav.addObject("doctors", paginatedDoctors); // All doctors (for reference if needed)
        mav.addObject("paginatedDoctors", paginatedDoctors); // Doctors for current page
//        mav.addObject("futureAppointmentsMap", futureAppointmentsMap);
        mav.addObject("specialty", specialtyObj.orElse(null));
        mav.addObject("allSpecialties", allSpecialties);
        mav.addObject("currentPage", page);
        mav.addObject("totalPages", doctorPage.getTotalPages());

        LOGGER.debug("Search Specialty: {}", specialtyObj.orElse(null));

        return mav;
    }
}
