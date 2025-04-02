package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.ClientService;
import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.webapp.form.AppointmentForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.text.html.Option;
import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class AppointmentController {

    private AppointmentService appointmentService;

    private ClientService clientService;

    private DoctorService doctorService;

    private CoverageService coverageService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, ClientService clientService, DoctorService doctorService, CoverageService coverageService) {
        this.appointmentService = appointmentService;
        this.clientService = clientService;
        this.doctorService = doctorService;
        this.coverageService = coverageService;
    }


    @RequestMapping(value = "/appointment", method = RequestMethod.POST)
    public ModelAndView appointment(
            @Valid @ModelAttribute("appointmentForm") final AppointmentForm appointmentForm,
            final BindingResult errors
           ,RedirectAttributes redirectAttributes
    ){
        if(errors.hasErrors()) {
            return appointment(appointmentForm);
        }

        Appointment appointment = appointmentService.create(
                1,
                2,
                LocalDateTime.of(appointmentForm.getAppointmentDate().getYear(),
                        appointmentForm.getAppointmentDate().getMonthValue(),
                        appointmentForm.getAppointmentDate().getDayOfMonth(),
                        appointmentForm.getAppointmentHour(),
                        0,
                        0
                ),
                "pendiente",
               appointmentForm.getReason()
        );

        redirectAttributes.addFlashAttribute("appointment", appointment);

        return new ModelAndView("redirect:/confirmation");

    }

    @RequestMapping(value = "/confirmation")                        //TODO find out why it cannot handle concatenated paths e.g. /appointment/confirmation
    public ModelAndView appointmentConfirmation(Model model) {
        Appointment appointment = (Appointment) model.asMap().get("appointment");
       Optional<Doctor> doctor = doctorService.findById(2);
        ModelAndView mav = new ModelAndView("appointment/confirmation");
        mav.addObject("appointment", appointment);
        mav.addObject("doctor", doctor.orElse(null));
        return mav;
    }

    @RequestMapping(value = "/appointment", method = RequestMethod.GET)
    public ModelAndView appointment(@ModelAttribute("appointmentForm") final AppointmentForm appointmentForm) {
        ModelAndView mav = new ModelAndView("appointment/appointment");
        Optional<List<Coverage>> coverage = coverageService.getAll();
        mav.addObject("coverages", coverage.orElse(Collections.emptyList()));
        return mav;
    }
}
