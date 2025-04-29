package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.PatientService;
import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Patient;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.UpdatePatientForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.*;

@Controller
public class PatientController {

    private static final Logger LOGGER = LoggerFactory.getLogger(PatientController.class);

    private final PatientService ps;
    private final AppointmentService as;
    private final CoverageService covs;
    @Autowired
    public PatientController(PatientService ps, AppointmentService as, CoverageService covs) {

        this.ps = ps;
        this.as = as;
        this.covs = covs;
    }

    @RequestMapping(value = "/patient/dashboard")
    public ModelAndView getPatientDashboard() {
        return new ModelAndView("redirect:/patient/dashboard/upcoming");
    }

    @RequestMapping(value = "/patient/dashboard/upcoming")
    public ModelAndView getUpcomingAppointments(@RequestParam(value = "page", defaultValue = "1") int page) {
        final ModelAndView mav = new ModelAndView("patient/dashboard-upcoming");
        Patient patient = loggedUser();
        mav.addObject("patient", patient);
        var appointmentsPage = as.getFuturePatientAppointments(patient.getId(), page, 5);
        mav.addObject("upcomingAppointments", appointmentsPage.getContent());
        mav.addObject("activeTab", "upcoming");
        mav.addObject("currentPage", page);
        mav.addObject("totalPages", appointmentsPage.getTotalPages());
        mav.addObject("hasMore", page < appointmentsPage.getTotalPages());
        LOGGER.debug("Loading dashboard and upcoming appointments for patient ID: {}", patient.getId());
        return mav;
    }

    @RequestMapping(value = "/patient/dashboard/history")
    public ModelAndView getAppointmentHistory(@RequestParam(value = "page", defaultValue = "1") int page) {
        final ModelAndView mav = new ModelAndView("patient/dashboard-history");
        Patient patient = loggedUser();
        mav.addObject("patient", patient);
        var appointmentsPage = as.getPastPatientAppointments(patient.getId(), page, 5);
        mav.addObject("pastAppointments", appointmentsPage.getContent());
        System.out.println("acaaaa" + appointmentsPage.getContent());
        mav.addObject("activeTab", "history");
        mav.addObject("currentPage", page);
        mav.addObject("totalPages", appointmentsPage.getTotalPages());
        mav.addObject("hasMore", page < appointmentsPage.getTotalPages());
        return mav;
    }

    @RequestMapping(value = "/patient/dashboard/profile")
    public ModelAndView getProfile(@ModelAttribute("updatePatientForm") final UpdatePatientForm updatePatientForm) {
        final ModelAndView mav = new ModelAndView("patient/dashboard-profile");
        Patient patient = loggedUser();
        updatePatientForm.setForm(patient);
        mav.addObject("patient", patient);
        mav.addObject("coverageList", covs.getAll());
        mav.addObject("activeTab", "profile");
        mav.addObject("display", "none");
        return mav;
    }

    @RequestMapping(value = "/patient/dashboard/update", method = RequestMethod.POST)
    public ModelAndView updatePatient(@Valid @ModelAttribute("updatePatientForm") final UpdatePatientForm updatePatientForm,
                                      final BindingResult errors) {
        if (errors.hasErrors()) {
            ModelAndView mav = getProfile(updatePatientForm);
            mav.addObject("display", "block");
            return mav;
        }
        Patient patient = loggedUser();
        ps.updatePatient(patient, updatePatientForm.getName(), updatePatientForm.getLastName(), updatePatientForm.getPhone(), covs.findById(Long.parseLong(updatePatientForm.getCoverage())).orElse(null));
        return new ModelAndView("redirect:/patient/dashboard/profile?updated=true");
    }

    @ModelAttribute
    public Patient loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ps.getByEmail((String) auth.getName()).orElseThrow(UserNotFoundException::new);
    }

    @PostMapping(value = "/patient/dashboard/appointment/cancel", produces = "application/json")
    @ResponseBody
    public String cancelAppointment(@RequestParam("appointmentId") Long appointmentId) {
        boolean result = as.cancelAppointment(appointmentId, loggedUser().getId());
        return "{\"success\": " + result + "}";
    }

//        @RequestMapping(value = "patient/dashboard/appointment/{id}", method = RequestMethod.GET)
//    public ModelAndView patientAppointmentDetails(
//            @ModelAttribute("patientRatingForm") final PatientRatingForm patientRatingForm,
//            @PathVariable("id") Long id) {
//        ModelAndView mav = new ModelAndView("patient/details");
//        Appointment appointment = as.getById(id).orElseThrow(() ->
//                new IllegalArgumentException("Invalid appointment Id:" + id));
//        mav.addObject("appointment", appointment);
//        mav.addObject("patientFiles", afs.getByAppointmentId(appointment.getId()));
//        return mav;
//    }
}