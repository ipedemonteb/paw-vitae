package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.auth.AuthUserDetailsService;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.DoctorForm;
import ar.edu.itba.paw.webapp.form.UpdateAvailabilityForm;
import ar.edu.itba.paw.webapp.form.UpdateDoctorForm;
import ar.edu.itba.paw.webapp.form.UpdatePatientForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ar.edu.itba.paw.webapp.controller.AuthController.*;

import javax.print.Doc;
import javax.validation.Valid;
import java.beans.PropertyEditorSupport;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class DoctorController {

    private static final Logger LOGGER = LoggerFactory.getLogger(DoctorController.class);

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
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                try {
                    setValue(LocalTime.parse(text, DateTimeFormatter.ofPattern("HH:mm")));
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid time format. Expected HH:mm", e);
                }
            }
            @Override
            public String getAsText() {
                LocalTime value = (LocalTime) getValue();
                return (value != null ? value.format(DateTimeFormatter.ofPattern("HH:mm")) : "");
            }
        });
    }

    @RequestMapping(value = "/{id:\\d+}", method = {RequestMethod.GET})
    public ModelAndView getUser(@PathVariable("id") long id) {
        final ModelAndView mav = new ModelAndView("doctor/doctor");
        mav.addObject("doctor", ds.getById(id).orElseThrow(UserNotFoundException::new));
        LOGGER.debug("Fetching doctor profile with ID: {}", id);
        return mav;
    }


    @RequestMapping(value = "/doctor/dashboard")
    public ModelAndView getDoctorDashboard() {
        return new ModelAndView("redirect:/doctor/dashboard/upcoming");
    }

    @RequestMapping(value = "/doctor/dashboard/upcoming")
    public ModelAndView getUpcomingAppointments() {
        final ModelAndView mav = new ModelAndView("doctor/dashboard-upcoming");
        Doctor doctor = loggedUser();
        mav.addObject("doctor", doctor);
        mav.addObject("upcomingAppointments",as.getFutureDoctorAppointments(doctor.getId()));
        mav.addObject("activeTab", "upcoming");
        LOGGER.debug("Loading dashboard and upcoming appointments for doctor ID: {}", doctor.getId());
        return mav;
    }

    @RequestMapping(value = "/doctor/dashboard/history")
    public ModelAndView getAppointmentHistory() {
        final ModelAndView mav = new ModelAndView("doctor/dashboard-history");
        Doctor doctor = loggedUser();
        mav.addObject("doctor", doctor);
        mav.addObject("pastAppointments", as.getPastDoctorAppointments(doctor.getId()));
        mav.addObject("activeTab", "history");
        return mav;
    }

    @RequestMapping(value = "/doctor/dashboard/profile")
    public ModelAndView getProfile(@ModelAttribute("updateDoctorForm") final UpdateDoctorForm updateDoctorForm) {
        final ModelAndView mav = new ModelAndView("doctor/dashboard-profile");
        Doctor doctor = loggedUser();
        updateDoctorForm.setForm(doctor);
        mav.addObject("doctor", doctor);
        mav.addObject("coverageList", cs.getAll().orElse(new ArrayList<>()) );
        mav.addObject("specialtyList", ss.getAll().orElse(new ArrayList<>()) );
        mav.addObject("activeTab", "profile");
        mav.addObject("display","none");
        return mav;
    }

    @RequestMapping(value = "/doctor/dashboard/availability")
    public ModelAndView getAvailability(@ModelAttribute("updateAvailabilityForm") UpdateAvailabilityForm updateAvailabilityForm) {
        final ModelAndView mav = new ModelAndView("doctor/dashboard-availability");
        Doctor doctor = loggedUser();
        updateAvailabilityForm.setForm(doctor.getAvailabilitySlots());
        mav.addObject("doctor", doctor);
        mav.addObject("activeTab", "availability");
        return mav;
    }

    @RequestMapping(value = "/doctor/dashboard/update", method = RequestMethod.POST)
    public ModelAndView updateDoctor(@Valid @ModelAttribute("updateDoctorForm") final UpdateDoctorForm updateDoctorForm,
                                     final BindingResult errors) {
        if (errors.hasErrors()) {
            ModelAndView mav= getProfile(updateDoctorForm);
            mav.addObject("display","block");
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
        return new ModelAndView("redirect:/doctor/dashboard/profile");
    }

    @ModelAttribute
    public Doctor loggedUser() {
        final Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        return ds.getByEmail((String) auth.getName()).orElseThrow(UserNotFoundException::new);
    }

    @PostMapping(value = "/doctor/dashboard/appointment/cancel", produces = "application/json")
    @ResponseBody
    public String cancelAppointment(@RequestParam("appointmentId") Long appointmentId){
        boolean result = as.cancelAppointment(appointmentId, loggedUser().getId());
        return "{\"success\": " + result + "}";
    }

    @RequestMapping(value = "/doctor/dashboard/availability/update", method = RequestMethod.POST)
    public ModelAndView updateAvailability(
            @Valid @ModelAttribute("updateAvailabilityForm") UpdateAvailabilityForm form,
            BindingResult errors,
            @RequestParam(value = "deletedSlots", required = false) String[] deletedSlots) {
        if (errors.hasErrors()) {
            return getAvailability(form);
        }
        form.setAvailabilitySlots(form.getAvailabilitySlots());
        ds.updateDoctorAvailability(loggedUser().getId(), form.getAvailabilitySlots());
        return new ModelAndView("redirect:/doctor/dashboard/availability");
    }


    @PostMapping(value = "/doctor/dashboard/appointment/accept", produces = "application/json")
    @ResponseBody
    public String acceptAppointment(@RequestParam("appointmentId") Long appointmentId){
        boolean result = as.acceptAppointment(appointmentId, loggedUser().getId());
        return "{\"success\": " + result + "}";
    }
}
