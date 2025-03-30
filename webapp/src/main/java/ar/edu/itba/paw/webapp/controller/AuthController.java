package ar.edu.itba.paw.webapp.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AuthController {
    @RequestMapping("/login")
    public ModelAndView login() {
        return new ModelAndView("auth/login");
    }
    @RequestMapping("/register")
    public ModelAndView register() {
        return new ModelAndView("auth/register");
    }

}
