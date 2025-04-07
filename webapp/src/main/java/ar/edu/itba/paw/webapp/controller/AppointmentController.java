package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.*;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.models.Specialty;
import ar.edu.itba.paw.webapp.form.AppointmentForm;

import org.hibernate.validator.internal.util.logging.Messages;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

        Optional<Specialty> specialty = specialtyService.findById(appointmentForm.getSpecialtyId());

        Appointment appointment = appointmentService.create(
                1, // For now, client ID is still hardcoded - this could be the logged-in user
                appointmentForm.getDoctorId(),
                LocalDateTime.of(appointmentForm.getAppointmentDate().getYear(),
                        appointmentForm.getAppointmentDate().getMonthValue(),
                        appointmentForm.getAppointmentDate().getDayOfMonth(),
                        appointmentForm.getAppointmentHour(),
                        0,
                        0
                ),
                appointmentForm.getReason(),
                specialty.orElse(null) // This should be the specialty of the doctor
        );


        Optional<Doctor> doctor = doctorService.findById(appointmentForm.getDoctorId());
        if (doctor.isPresent()) {
            Map<String, Object> doctorTemplateModel = new HashMap<>();
            doctorTemplateModel.put("doctorName", doctor.get().getName());
            doctorTemplateModel.put("patientName", appointmentForm.getName() + " " + appointmentForm.getLastName());
            doctorTemplateModel.put("appointmentDate", appointmentForm.getAppointmentDate().toString());
            doctorTemplateModel.put("appointmentTime", appointmentForm.getAppointmentHour().toString());
            doctorTemplateModel.put("reason", appointment.getReason() != null ? appointment.getReason() : messageSource.getMessage("email.emptyReason", null, LocaleContextHolder.getLocale()));
            try {
                mailService.sendEmail(doctor.get().getEmail(), messageSource.getMessage("emil.newAppointment", null, LocaleContextHolder.getLocale()), doctorTemplateModel);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }

        redirectAttributes.addFlashAttribute("appointment", appointment);

        return new ModelAndView("redirect:/appointment/confirmation");
    }

    @RequestMapping(value = "/appointment/confirmation")
    public ModelAndView appointmentConfirmation(Model model) {
        Appointment appointment = (Appointment) model.asMap().get("appointment");
        Optional<Doctor> doctor = doctorService.findById(appointment.getDoctorId());

        ModelAndView mav = new ModelAndView("appointment/confirmation");
        mav.addObject("appointment", appointment);
        mav.addObject("doctor", doctor.orElse(null));
        return mav;
    }

    @RequestMapping(value = "/appointment", method = RequestMethod.GET)
    public ModelAndView appointment(
            @ModelAttribute("appointmentForm") final AppointmentForm appointmentForm,
            @RequestParam(required = true) Integer doctorId,
            @RequestParam(required = true) Integer specialtyId
    ) {
        ModelAndView mav = new ModelAndView("appointment/appointment");
        Optional<List<Coverage>> coverage = coverageService.getAll();
        mav.addObject("coverages", coverage.orElse(Collections.emptyList()));

        // If we have a doctorId, let's fetch the doctor and add it to the model

        Optional<Doctor> doctor = doctorService.findById(doctorId);
        doctor.ifPresent(d -> mav.addObject("doctor", d));
        Optional<Specialty> specialty = specialtyService.findById(specialtyId);
        specialty.ifPresent(s -> mav.addObject("specialty", s));

        // Set the doctor ID in the form
        appointmentForm.setDoctorId(doctorId);
        appointmentForm.setSpecialtyId(specialtyId);

        return mav;
    }
}