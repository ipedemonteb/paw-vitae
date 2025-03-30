package ar.edu.itba.paw.webapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class LandingPageController {
    @RequestMapping("/")
    public ModelAndView home() {
        final ModelAndView mav = new ModelAndView("landingPage/home");
        mav.addObject("greeting", "PAW");
        return mav;
    }
}
