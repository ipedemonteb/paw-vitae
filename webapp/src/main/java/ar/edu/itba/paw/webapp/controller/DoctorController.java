package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import ar.edu.itba.paw.webapp.form.DoctorForm;
import ar.edu.itba.paw.webapp.form.UpdateAvailabilityForm;
import ar.edu.itba.paw.webapp.form.UpdateDoctorForm;
import ar.edu.itba.paw.webapp.form.UpdatePatientForm;
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

    // Add this method to register a custom property editor for LocalTime
    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(LocalTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) throws IllegalArgumentException {
                try {
                    // Parse the time string in format "HH:mm"
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
        List<Appointment> appointments = as.getByDoctorId(doctor.getId()).orElse(new ArrayList<>());
        LocalDateTime now = LocalDateTime.now(ZoneId.of("America/Argentina/Buenos_Aires"));
        Map<Boolean, List<Appointment>> partitionedAppointments = appointments.stream()
                .collect(Collectors.partitioningBy(appointment -> appointment.getDate().isAfter(now)));
        mav.addObject("upcomingAppointments", partitionedAppointments.get(true));
        mav.addObject("pastAppointments", partitionedAppointments.get(false));
        UpdateDoctorForm updateDoctorForm = new UpdateDoctorForm();
        updateDoctorForm.setName(doctor.getName());
        updateDoctorForm.setLastName(doctor.getLastName());
        updateDoctorForm.setPhone(doctor.getPhone());
        updateDoctorForm.setSpecialties(doctor.getSpecialtyList().stream().map(Specialty::getKey).toList());
        updateDoctorForm.setCoverages(doctor.getCoverageList().stream().map(Coverage::getName).toList());
        mav.addObject("updateDoctorForm", updateDoctorForm);

        // Add the UpdateAvailabilityForm to the model
        UpdateAvailabilityForm updateAvailabilityForm = new UpdateAvailabilityForm();
        updateAvailabilityForm.setAvailabilitySlots(doctor.getAvailabilitySlots());
        mav.addObject("updateAvailabilityForm", updateAvailabilityForm);

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

    @RequestMapping(value = "/doctor/dashboard/availability", method = RequestMethod.POST)
    public ModelAndView updateAvailability(@Valid @ModelAttribute("updateAvailabilityForm") UpdateAvailabilityForm form,
                                           @RequestParam(value = "deletedSlots", required = false) String[] deletedSlots,
                                           BindingResult errors) {
        if (errors.hasErrors()) {
            // Return to dashboard with errors displayed
            ModelAndView mav = getDoctorDashboard();
            mav.addObject("updateAvailabilityForm", form);
            return mav;
        }

        Doctor doctor = loggedUser();

        // Filter out any null slots that might have been created when removing rows
        List<AvailabilitySlot> validSlots = form.getAvailabilitySlots().stream()
                .filter(slot -> slot != null && slot.getStartTime() != null && slot.getEndTime() != null)
                .collect(Collectors.toList());

        form.setAvailabilitySlots(validSlots);

        ds.updateDoctorAvailability(doctor.getId(), form.getAvailabilitySlots());

        return new ModelAndView("redirect:/doctor/dashboard");
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
