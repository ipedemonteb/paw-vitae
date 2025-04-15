package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.DoctorForm;
import ar.edu.itba.paw.webapp.form.UpdateDoctorForm;
import ar.edu.itba.paw.webapp.form.UpdatePatientForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
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
    private final CoverageService cs;
    private final SpecialtyService ss;

    @Autowired
    public DoctorController(DoctorService ds, AppointmentService as, CoverageService cs, SpecialtyService ss) {
        this.ds = ds;
        this.as = as;
        this.cs = cs;
        this.ss = ss;
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
        List<Coverage> coverageList = cs.getAll().orElse(new ArrayList<>());
        List<Specialty> specialtyList = ss.getAll().orElse(new ArrayList<>());
        mav.addObject("coverageList", coverageList);
        mav.addObject("specialtyList", specialtyList);
        Map<Appointment, Client> appointments = as.getForDoctor(doctor.getId());
        mav.addObject("doctor", doctor);
        mav.addObject("upcomingAppointments", appointments.entrySet().stream().filter(appointment -> appointment.getKey().getDate().isAfter(LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")))).toList());
        mav.addObject("pastAppointments", appointments.entrySet().stream().filter(appointment -> appointment.getKey().getDate().isBefore(LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires")))).toList());

        UpdateDoctorForm updateDoctorForm = new UpdateDoctorForm();
        updateDoctorForm.setName(doctor.getName());
        updateDoctorForm.setLastName(doctor.getLastName());
        updateDoctorForm.setPhone(doctor.getPhone());
        updateDoctorForm.setSpecialties(doctor.getSpecialtyList().stream().map(Specialty::getKey).toList());
        updateDoctorForm.setCoverages(doctor.getCoverageList().stream().map(Coverage::getName).toList());
        mav.addObject("updateDoctorForm", updateDoctorForm);
        return mav;
    }

    @RequestMapping(value = "/doctor/dashboard/update", method = RequestMethod.POST)
    public ModelAndView updateDoctor(@Valid @ModelAttribute("updateDoctorForm") final UpdateDoctorForm updateDoctorForm,
                                      final BindingResult errors) {
        if (errors.hasErrors()) {
            final ModelAndView mav = new ModelAndView("patient/dashboard");
            Doctor doctor = loggedUser();

            //ver si es legal esto

            mav.addObject("doctor", doctor);
            return mav;
        }
        Doctor doctor = loggedUser();
        ds.updateDoctor(doctor.getId(),
                updateDoctorForm.getName(),
                updateDoctorForm.getLastName(),
                updateDoctorForm.getPhone(),
                updateDoctorForm.getSpecialties(),
                updateDoctorForm.getCoverages(),
                null);

        return new ModelAndView("redirect:/doctor/dashboard");
    }

    @ModelAttribute
    public Doctor loggedUser() {
        final Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        return ds.getByEmail((String) auth.getName()).orElseThrow(UserNotFoundException::new);
    }
    @PostMapping(value = "/doctor/dashboard/appointment/cancel", produces = "application/json")
    @ResponseBody
    public String cancelAppointment(@RequestParam("appointmentId") Long appointmentId){
        try {
            Appointment appt = as.getById(appointmentId).orElse(null);
            if (appt == null) {
                return "{\"success\": false}";
            }
            as.cancelAppointment(appointmentId);
            return "{\"success\": true}";
        } catch (Exception e) {
            return "{\"success\": false}";
        }
    }

    @PostMapping(value = "/doctor/dashboard/appointment/accept", produces = "application/json")
    @ResponseBody
    public String acceptAppointment(@RequestParam("appointmentId") Long appointmentId){
        try {
            Appointment appt = as.getById(appointmentId).orElse(null);
            if (appt == null) {
                return "{\"success\": false}";
            }
            as.acceptAppointment(appointmentId);
            return "{\"success\": true}";
        } catch (Exception e) {
            return "{\"success\": false}";
        }
    }
}
