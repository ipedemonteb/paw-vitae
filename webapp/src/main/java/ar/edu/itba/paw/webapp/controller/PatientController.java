package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.ClientService;
import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.PatientForm;
import ar.edu.itba.paw.webapp.form.UpdatePatientForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class PatientController {

    private final ClientService cs;
    private final DoctorService ds;
    private final AppointmentService as;
    private final CoverageService covs;
    @Autowired
    public PatientController(ClientService cs, DoctorService ds, AppointmentService as,CoverageService covs) {
        this.cs = cs;
        this.ds = ds;
        this.as = as;
        this.covs = covs;
    }

    @RequestMapping(value = "/patient/dashboard")
    public ModelAndView getDoctorDashboard() {
        final ModelAndView mav = new ModelAndView("patient/dashboard");
        Client patient = loggedUser();
        List<Appointment> appointments = as.getByClientId(patient.getId()).orElse(new ArrayList<>());
        LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
        Map<Boolean, List<Appointment>> partitionedAppointments = appointments.stream()
                .collect(Collectors.partitioningBy(appointment -> appointment.getDate().isAfter(now)));
        List<Coverage> coverageList = covs.getAll().orElse(new ArrayList<>());
        UpdatePatientForm updatePatientForm = new UpdatePatientForm();
        updatePatientForm.setName(patient.getName());
        updatePatientForm.setLastName(patient.getLastName());
        updatePatientForm.setPhone(patient.getPhone());
        updatePatientForm.setCoverage(patient.getCoverage().getName());
        mav.addObject("patient", patient);
        mav.addObject("updatePatientForm", updatePatientForm);
        mav.addObject("upcomingAppointments", partitionedAppointments.get(true));
        mav.addObject("pastAppointments", partitionedAppointments.get(false));
        mav.addObject("coverageList", coverageList);
        return mav;
    }

    @RequestMapping(value = "/patient/dashboard/update", method = RequestMethod.POST)
    public ModelAndView updatePatient(@Valid @ModelAttribute("updatePatientForm") final UpdatePatientForm updatePatientForm,
                                      final BindingResult errors) {
        if (errors.hasErrors()) {
            final ModelAndView mav = new ModelAndView("patient/dashboard");
            Client patient = loggedUser();
            mav.addObject("patient", patient);
            return mav;
        }
        Client client = loggedUser();
        cs.updateClient(client,updatePatientForm.getName(), updatePatientForm.getLastName(), updatePatientForm.getPhone(), covs.findById(Long.parseLong(updatePatientForm.getCoverage())).orElse(null));
        return new ModelAndView("redirect:/patient/dashboard");
    }

    @ModelAttribute
    public Client loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return cs.getByEmail((String) auth.getName()).orElseThrow(UserNotFoundException::new);
    }
    @PostMapping(value = "/patient/dashboard/appointment/cancel", produces = "application/json")
    @ResponseBody
    public String cancelAppointment(@RequestParam("appointmentId") Long appointmentId) {
        try {
            Appointment appt = as.getById(appointmentId).orElse(null);
            if (appt == null) {
                return "{\"success\": false}";
            }
            Client patient = loggedUser();
            if (appt.getClient().getId() != patient.getId()){
                return "{\"success\": false}";
            }
            as.cancelAppointment(appointmentId);
            return "{\"success\": true}";
        } catch (Exception e) {
            return "{\"success\": false}";
        }
    }

}
