package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.*;
import ar.edu.itba.paw.webapp.form.AppointmentForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
public class AppointmentController {

    private AppointmentService appointmentService;
    private ClientService clientService;
    private DoctorService doctorService;
    private CoverageService coverageService;
    private MailService mailService;
    private MessageSource messageSource;
    private SpecialtyService specialtyService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, ClientService clientService, DoctorService doctorService, CoverageService coverageService, MailService mailService, MessageSource messageSource, SpecialtyService specialtyService) {
        this.appointmentService = appointmentService;
        this.clientService = clientService;
        this.doctorService = doctorService;
        this.coverageService = coverageService;
        this.mailService = mailService;
        this.messageSource = messageSource;
        this.specialtyService = specialtyService;
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

        Appointment appointment = appointmentService.create(appointmentForm.getClientId(), appointmentForm.getDoctorId(), appointmentForm.getAppointmentDate(), appointmentForm.getAppointmentHour(), appointmentForm.getReason(), appointmentForm.getSpecialtyId());

        redirectAttributes.addFlashAttribute("appointment", appointment);

        return new ModelAndView("redirect:/appointment/confirmation");
    }

    @RequestMapping(value = "/appointment/confirmation")
    public ModelAndView appointmentConfirmation(Model model) {
        Appointment appointment = (Appointment) model.asMap().get("appointment");
        Optional<Doctor> doctor = doctorService.getById(appointment.getDoctorId());

        ModelAndView mav = new ModelAndView("appointment/confirmation");
        mav.addObject("appointment", appointment);
        mav.addObject("doctor", doctor.orElse(null));
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
        Optional<List<Coverage>> coverage = coverageService.getAll();
        mav.addObject("coverages", coverage.orElse(Collections.emptyList()));

        Optional<Doctor> doctor = doctorService.getByIdWithAppointments(doctorId);
        doctor.ifPresent(d -> mav.addObject("doctor", d));
        Optional<Specialty> specialty = specialtyService.getById(specialtyId);
        specialty.ifPresent(s -> mav.addObject("specialty", s));


        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        final Client client = clientService.getByEmail((String) auth.getName()).orElseThrow(RuntimeException::new);

        appointmentForm.setDoctorId(doctorId);
        appointmentForm.setClientId(client.getId());
        appointmentForm.setSpecialtyId(specialtyId);
        appointmentForm.setName(client.getName());
        appointmentForm.setLastName(client.getLastName());
        appointmentForm.setEmail(client.getEmail());
        appointmentForm.setPhone(client.getPhone());
        appointmentForm.setCoverageId(client.getCoverage().getId());


        return mav;
    }

}