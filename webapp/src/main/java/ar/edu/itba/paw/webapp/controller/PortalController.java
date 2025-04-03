package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.interfaceServices.SpecialtyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class PortalController {
    private final SpecialtyService ss;

    public PortalController(SpecialtyService ss) {
        this.ss = ss;
    }

    @RequestMapping(value = "/portal", method = RequestMethod.GET)
    public ModelAndView showPortal() {
        List<String> specialties = ss.getSpecialties();
        System.out.println(specialties);
        return new ModelAndView("portal/portal").addObject("specialties", specialties);
    }
}