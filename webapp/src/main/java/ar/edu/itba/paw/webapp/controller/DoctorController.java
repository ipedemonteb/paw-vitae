package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Client;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
public class DoctorController {

    private final DoctorService ds;
    private final AppointmentService as;

    @Autowired
    public DoctorController(DoctorService ds, AppointmentService as) {
        this.ds = ds;
        this.as = as;
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
        Map<Appointment, Client> appointments = as.getForDoctor(doctor.getId());
        mav.addObject("doctor", doctor);
        mav.addObject("upcomingAppointments", appointments.entrySet().stream().filter(appointment -> appointment.getKey().getDate().isAfter(LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")))).toList());
        mav.addObject("pastAppointments", appointments.entrySet().stream().filter(appointment -> appointment.getKey().getDate().isBefore(LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")))).toList());
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
