package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.ClientService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class PatientController {

    private final ClientService cs;
    private final DoctorService ds;
    private final AppointmentService as;

    @Autowired
    public PatientController(ClientService cs, DoctorService ds, AppointmentService as) {
        this.cs = cs;
        this.ds = ds;
        this.as = as;
    }
    @RequestMapping(value = "/patient/dashboard")
    public ModelAndView getDoctorDashboard() {
        final ModelAndView mav = new ModelAndView("patient/dashboard");
        Client patient = loggedUser();
        mav.addObject("patient", patient);
        Map<Long, Doctor> doctorMap = ds.getAll().stream()
                .collect(Collectors.toMap(Doctor::getId, doctor -> doctor));
        mav.addObject("doctors", doctorMap);

        //todo: make a seach function that searches only appointment doctors


        return mav;
    }

//    @RequestMapping(value = "/client/update", method = RequestMethod.POST)
//    public ModelAndView updateClient(@Valid @ModelAttribute("clientForm") final ClientForm clientForm, final BindingResult errors) {
//        if (errors.hasErrors()) {
//            return new ModelAndView("client/edit");
//        }
//
//        Client client = loggedUser();
//        cs.updateClient(client.getId(), clientForm.getName(), clientForm.getLastName(), clientForm.getEmail(), clientForm.getPhone());
//
//        return new ModelAndView("redirect:/client/dashboard");
//    }

    @ModelAttribute
    public Client loggedUser() {
        final Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        return cs.getByEmail((String) auth.getName()).orElseThrow(UserNotFoundException::new);
    }

}
