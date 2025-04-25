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
    private final DoctorService ds;
    private final AppointmentService as;
    private final CoverageService covs;
    @Autowired
    public PatientController(PatientService ps, DoctorService ds, AppointmentService as, CoverageService covs) {
        this.ps = ps;
        this.ds = ds;
        this.as = as;
        this.covs = covs;
    }

    @RequestMapping(value = "/patient/dashboard")
    public ModelAndView getDoctorDashboard() {
        final ModelAndView mav = new ModelAndView("patient/dashboard");
        Patient patient = loggedUser();
        Map <Boolean,List<Appointment>> partitionedAppointments = as.getByPatientIdPartitionedByDate(patient.getId());
        List<Coverage> coverageList = covs.getAll().orElse(new ArrayList<>());
        UpdatePatientForm updatePatientForm = new UpdatePatientForm(patient);
        mav.addObject("patient", patient);
        mav.addObject("updatePatientForm", updatePatientForm);
        mav.addObject("upcomingAppointments", partitionedAppointments.get(false));
        mav.addObject("pastAppointments", partitionedAppointments.get(true));
        mav.addObject("coverageList", coverageList);
        return mav;
    }

    @RequestMapping(value = "/patient/dashboard/update", method = RequestMethod.POST)
    public ModelAndView updatePatient(@Valid @ModelAttribute("updatePatientForm") final UpdatePatientForm updatePatientForm,
                                      final BindingResult errors) {
        if (errors.hasErrors()) {
            final ModelAndView mav = new ModelAndView("patient/dashboard");
            Patient patient = loggedUser();
            mav.addObject("patient", patient);
            return mav;
        }
        Patient patient = loggedUser();
        ps.updatePatient(patient,updatePatientForm.getName(), updatePatientForm.getLastName(), updatePatientForm.getPhone(), covs.findById(Long.parseLong(updatePatientForm.getCoverage())).orElse(null));
        return new ModelAndView("redirect:/patient/dashboard");
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
