package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.form.AppointmentForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.util.*;

@Controller
public class AppointmentController {

    private AppointmentService as;
    private PatientService ps;
    private DoctorService ds;
    private CoverageService cs;
    private MailService ms;
    private MessageSource messageSource;
    private SpecialtyService ss;

    @Autowired
    public AppointmentController(AppointmentService as, PatientService ps, DoctorService ds, CoverageService cs, MailService ms, MessageSource messageSource, SpecialtyService ss) {
        this.as = as;
        this.ps = ps;
        this.ds = ds;
        this.cs = cs;
        this.ms = ms;
        this.messageSource = messageSource;
        this.ss = ss;
    }

    // Add the following method to handle MessagingException
    @ExceptionHandler(MessagingException.class)
    public ModelAndView handleMessagingException(MessagingException e) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", "There was an error sending the confirmation email. Please try again later.");
        return mav;
    }

    @RequestMapping(value = "/appointment", method = RequestMethod.POST)
    public ModelAndView appointment(
            @Valid @ModelAttribute("appointmentForm") final AppointmentForm appointmentForm,
            final BindingResult errors,
            RedirectAttributes redirectAttributes
    ) {
        if (errors.hasErrors()) {
            return appointment(appointmentForm, appointmentForm.getDoctorId(), appointmentForm.getSpecialtyId());
        }

        Appointment appointment = as.create(appointmentForm.getPatientId(), appointmentForm.getDoctorId(), appointmentForm.getAppointmentDate(), appointmentForm.getAppointmentHour(), appointmentForm.getReason(), appointmentForm.getSpecialtyId());

        redirectAttributes.addFlashAttribute("appointment", appointment);

        return new ModelAndView("redirect:/appointment/confirmation");
    }

    @RequestMapping(value = "/appointment/confirmation")
    public ModelAndView appointmentConfirmation(Model model) {
        Appointment appointment = (Appointment) model.asMap().get("appointment");

        ModelAndView mav = new ModelAndView("appointment/confirmation");
        mav.addObject("appointment", appointment);
        mav.addObject("specialty", appointment.getSpecialty());
        return mav;
    }

    @RequestMapping(value = "/appointment", method = RequestMethod.GET)
    public ModelAndView appointment(
            @ModelAttribute("appointmentForm") final AppointmentForm appointmentForm,
            @RequestParam(required = true) Long doctorId,
            @RequestParam(required = true) Long specialtyId
    ) {
        ModelAndView mav = new ModelAndView("appointment/appointment");
        Optional<List<Coverage>> coverage = cs.getAll();
        mav.addObject("coverages", coverage.orElse(Collections.emptyList()));

        Optional<Doctor> doctor = ds.getByIdWithAppointments(doctorId);
        doctor.ifPresent(d -> mav.addObject("doctor", d));
        Optional<Specialty> specialty = ss.getById(specialtyId);
        specialty.ifPresent(s -> mav.addObject("specialty", s));


        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final Patient patient = ps.getByEmail((String) auth.getName()).orElseThrow(RuntimeException::new);

        appointmentForm.setDoctorId(doctorId);
        appointmentForm.setPatientId(patient.getId());
        appointmentForm.setSpecialtyId(specialtyId);
        appointmentForm.setName(patient.getName());
        appointmentForm.setLastName(patient.getLastName());
        appointmentForm.setEmail(patient.getEmail());
        appointmentForm.setPhone(patient.getPhone());
        appointmentForm.setCoverageId(patient.getCoverage().getId());


        return mav;
    }

}