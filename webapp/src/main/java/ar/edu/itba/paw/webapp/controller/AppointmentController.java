package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
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
    private AppointmentFileService afs;
    @Autowired
    public AppointmentController(AppointmentService as, PatientService ps, DoctorService ds, CoverageService cs, MailService ms, MessageSource messageSource, SpecialtyService ss,AppointmentFileService afs) {
        this.as = as;
        this.ps = ps;
        this.ds = ds;
        this.cs = cs;
        this.ms = ms;
        this.messageSource = messageSource;
        this.ss = ss;
        this.afs = afs;
    }

    // Add the following method to handle MessagingException
    @ExceptionHandler(MessagingException.class)
    public ModelAndView handleMessagingException(MessagingException e) {
        ModelAndView mav = new ModelAndView("error");
        mav.addObject("message", "There was an error sending the confirmation email. Please try again later.");
        return mav;
    }

    @ModelAttribute
    public Patient loggedUser() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return ps.getByEmail((String) auth.getName()).orElseThrow(UserNotFoundException::new);
    }

    @RequestMapping(value = "/appointment", method = RequestMethod.GET)
    public ModelAndView appointment(
            @ModelAttribute("appointmentForm") final AppointmentForm appointmentForm,
            @RequestParam(required = true) Long doctorId
    ) {
        ModelAndView mav = new ModelAndView("appointment/appointment");

        Optional<Doctor> doctor = ds.getByIdWithAppointments(doctorId);
        doctor.ifPresent(d -> mav.addObject("doctor", d));
        return mav;
    }

    @RequestMapping(value = "/appointment", method = RequestMethod.POST)
    public ModelAndView appointment(
            @Valid @ModelAttribute("appointmentForm") final AppointmentForm appointmentForm,
            final BindingResult errors,
            @RequestParam(required = true) Long doctorId,
            RedirectAttributes redirectAttributes
    ) {

        if (errors.hasErrors()) {
            return appointment(appointmentForm, doctorId);
        }

        Patient patient = loggedUser();

        Appointment appointment = as.create(patient.getId(), doctorId, appointmentForm.getAppointmentDate(), appointmentForm.getAppointmentHour(), appointmentForm.getReason(), appointmentForm.getSpecialtyId());
        afs.create( appointmentForm.getFiles(),"patient",appointment.getId());
        redirectAttributes.addFlashAttribute("appointment", appointment);

        return new ModelAndView("redirect:/appointment/confirmation");
    }

    @RequestMapping(value = "/appointment/confirmation")
    public ModelAndView appointmentConfirmation(Model model) {
        Appointment appointment = (Appointment) model.asMap().get("appointment");

        ModelAndView mav = new ModelAndView("appointment/confirmation");
        mav.addObject("patientFiles", afs.getByAppointmentId(appointment.getId()));
        mav.addObject("appointment", appointment);
        mav.addObject("specialty", appointment.getSpecialty());
        return mav;
    }
}