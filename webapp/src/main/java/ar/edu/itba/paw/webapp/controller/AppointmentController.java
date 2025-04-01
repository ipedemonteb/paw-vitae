package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.webapp.form.AppointmentForm;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class AppointmentController {

    @RequestMapping(value = "/appointment", method = RequestMethod.POST)
    public ModelAndView appointment(
            @Valid @ModelAttribute("appointmentForm") final AppointmentForm appointmentForm,
            final BindingResult errors
    ){
        if(errors.hasErrors()) {
            return appointment(appointmentForm);
        }
        //TODO: llamada al service para reservar
        //final User user = us.createUser(userForm.getEmail(), userForm.getPassword());

        //TODO: retorno de pagina de confirmación
        //return new ModelAndView("redirect:/" + user.getId());
        return null;
    }

    @RequestMapping(value = "/appointment", method = RequestMethod.GET)
    public ModelAndView appointment(@ModelAttribute("appointmentForm") final AppointmentForm appointmentForm) {
        return new ModelAndView("appointment/appointment");
    }
}
