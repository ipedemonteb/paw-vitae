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
    public ModelAndView getDoctorDashboard() {
        return new ModelAndView("redirect:/patient/dashboard/upcoming");
    }

    @RequestMapping(value = "/patient/dashboard/upcoming")
    public ModelAndView getUpcomingAppointments() {
        final ModelAndView mav = new ModelAndView("patient/dashboard-upcoming");
        Patient patient = loggedUser();
        mav.addObject("patient", patient);
        mav.addObject("upcomingAppointments", as.getFuturePatientAppointments(patient.getId()));
        mav.addObject("activeTab", "upcoming");
        return mav;
    }

    @RequestMapping(value = "/patient/dashboard/history")
    public ModelAndView getAppointmentHistory() {
        final ModelAndView mav = new ModelAndView("patient/dashboard-history");
        Patient patient = loggedUser();
        mav.addObject("patient", patient);
        mav.addObject("pastAppointments", as.getPastPatientAppointments(patient.getId()));
        mav.addObject("activeTab", "history");
        return mav;
    }
    @RequestMapping(value = "/patient/dashboard/profile")
    public ModelAndView getProfile(@ModelAttribute("updatePatientForm") final UpdatePatientForm updatePatientForm) {
        final ModelAndView mav = new ModelAndView("patient/dashboard-profile");
        Patient patient = loggedUser();
        updatePatientForm.setForm(patient);
        mav.addObject("patient", patient);
        mav.addObject("coverageList", covs.getAll().orElse(new ArrayList<>()));
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
        return new ModelAndView("redirect:/patient/dashboard/profile");
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
}
