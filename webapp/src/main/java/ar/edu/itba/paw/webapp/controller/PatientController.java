package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.ClientService;
import ar.edu.itba.paw.models.Client;
import ar.edu.itba.paw.models.Doctor;
import ar.edu.itba.paw.webapp.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class PatientController {
    private final ClientService cs;

    @Autowired
    public PatientController(ClientService cs) {
        this.cs = cs;
    }
    @RequestMapping(value = "/patient/dashboard")
    public ModelAndView getDoctorDashboard() {
        final ModelAndView mav = new ModelAndView("patient/dashboard");
        Client patient = loggedUser();
        mav.addObject("patient", patient);
        return mav;
    }

//    @RequestMapping(value = "/client/update", method = RequestMethod.POST)
//    public ModelAndView updateClient(@Valid @ModelAttribute("clientForm") final ClientForm clientForm, final BindingResult errors) {
//        if (errors.hasErrors()) {
//            return new ModelAndView("client/edit");
//        }
//
//        Client client = loggedUser();
//        cs.updateClient(client.getId(), clientForm.getName(), clientForm.getLastName(), clientForm.getEmail(), clientForm.getPhone());
//
//        return new ModelAndView("redirect:/client/dashboard");
//    }

    @ModelAttribute
    public Client loggedUser() {
        final Authentication auth =  SecurityContextHolder.getContext().getAuthentication();
        return cs.getByEmail((String) auth.getName()).orElseThrow(UserNotFoundException::new);
    }

}
