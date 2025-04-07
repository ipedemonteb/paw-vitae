package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import ar.edu.itba.paw.models.Specialty;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
public class PortalController {
    private final SpecialtyService ss;

    public PortalController(SpecialtyService ss) {
        this.ss = ss;
    }

    @RequestMapping(value = "/portal", method = RequestMethod.GET)
    public ModelAndView showPortal() {
        Optional<List<Specialty>> specialties = ss.getAll();
        return new ModelAndView("portal/portal").addObject("specialties", specialties.orElse(new ArrayList<>()));
    }
}