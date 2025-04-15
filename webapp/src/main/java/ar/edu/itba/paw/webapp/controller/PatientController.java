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
        Map<Appointment, Doctor> appointments = as.getForClient(patient.getId());
        List<Coverage> coverageList = covs.getAll().orElse(new ArrayList<>());
        // Crear un nuevo objeto de formulario para actualización
        UpdatePatientForm updatePatientForm = new UpdatePatientForm();
        updatePatientForm.setName(patient.getName());
        updatePatientForm.setLastName(patient.getLastName());
        updatePatientForm.setPhone(patient.getPhone());
        updatePatientForm.setCoverage(patient.getCoverage().getName());

        mav.addObject("patient", patient);
        mav.addObject("updatePatientForm", updatePatientForm);
        mav.addObject("upcomingAppointments", appointments.entrySet().stream().filter(appointment -> appointment.getKey().getDate().isAfter(LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")))).toList());
        mav.addObject("pastAppointments", appointments.entrySet().stream().filter(appointment -> appointment.getKey().getDate().isBefore(LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")))).toList());
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
        cs.updateClient(client.getId(),
                updatePatientForm.getName(),
                updatePatientForm.getLastName(),
                updatePatientForm.getPhone(),
                updatePatientForm.getCoverage());

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
                return "{\"success\": false, \"error\": \"Turno no encontrado\"}";
            }
            as.cancelAppointment(appointmentId);
            return "{\"success\": true}";
        } catch (Exception e) {
            e.printStackTrace();
            return "{\"success\": false, \"error\": \"Error inesperado del servidor\"}";
        }
    }

}
