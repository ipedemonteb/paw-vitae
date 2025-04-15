package ar.edu.itba.paw.webapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class ErrorController {

    @RequestMapping("/error/403")
    @ResponseStatus(code = HttpStatus.FORBIDDEN)
    public ModelAndView forbidden() {
        return new ModelAndView("error/403");
    }

    @RequestMapping("/error/404")
    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    public ModelAndView notFound() {
        return new ModelAndView("error/404");
    }

    @RequestMapping("/error/500")
    @ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView serverError() {
        return new ModelAndView("error/500");
    }
    @RequestMapping("/error/400")
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ModelAndView badRequest() {
        return new ModelAndView("error/400");
    }


}