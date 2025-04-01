package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.AppointmentService;
import ar.edu.itba.paw.interfaceServices.ClientService;
import ar.edu.itba.paw.interfaceServices.DoctorService;
import ar.edu.itba.paw.models.Appointment;
import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.webapp.form.AppointmentForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class AppointmentController {

    private AppointmentService appointmentService;

    private ClientService clientService;

    private DoctorService doctorService;

    @Autowired
    public AppointmentController(AppointmentService appointmentService, ClientService clientService, DoctorService doctorService) {
        this.appointmentService = appointmentService;
        this.clientService = clientService;
        this.doctorService = doctorService;
    }


    @RequestMapping(value = "/appointment", method = RequestMethod.POST)
    public ModelAndView appointment(
            @Valid @ModelAttribute("appointmentForm") final AppointmentForm appointmentForm,
            final BindingResult errors,
            RedirectAttributes redirectAttributes
    ){
        if(errors.hasErrors()) {
            return appointment(appointmentForm);
        }

        Appointment appointment = appointmentService.create(
                0,
                1,
                appointmentForm.getAppointmentDateTime(),
                "pendiente",
               appointmentForm.getReason()
        );

        redirectAttributes.addFlashAttribute("appointment", appointment);

        return new ModelAndView("redirect:/appointment/appointment/confirmation");
    }

    @RequestMapping(value = "/appointment/confirmation", method = RequestMethod.GET)
    public ModelAndView appointmentConfirmation(@ModelAttribute("appointmentForm") final AppointmentForm appointmentForm) {
        return new ModelAndView("appointment/appointment/confirmation");
    }

    @RequestMapping(value = "/appointment", method = RequestMethod.GET)
    public ModelAndView appointment(@ModelAttribute("appointmentForm") final AppointmentForm appointmentForm) {
        return new ModelAndView("appointment/appointment");
    }
}
