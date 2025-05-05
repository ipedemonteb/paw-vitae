package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.exception.AppointmentNotFoundException;
import ar.edu.itba.paw.models.exception.SpecialtyNotFoundException;
import ar.edu.itba.paw.models.exception.UserNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.mail.MessagingException;
import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @RequestMapping("/error/403")
    public ModelAndView error403() {
        return new ModelAndView("error/403");
    }

    // 500 – all other uncaught exceptions
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handle500(Exception ex) {
        LOGGER.error("Unhandled exception occurred", ex);
        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("exception", ex.getMessage());
        return mav;
    }
    @ExceptionHandler(MessagingException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleMessagingException(MessagingException e) {
        ModelAndView mav = new ModelAndView("/error/genericError");
        mav.addObject("message", "There was an error sending the confirmation email. Please try again later.");
        return mav;
    }
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleUserNotFoundException(UserNotFoundException e) {
        ModelAndView mav = new ModelAndView("/error/genericError");
        mav.addObject("message", "User not found");
        return mav;
    }
    @ExceptionHandler(AppointmentNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleAppointmentNotFoundException(AppointmentNotFoundException e) {
        ModelAndView mav = new ModelAndView("/error/genericError");
        mav.addObject("message", "Appointment not found");
        return mav;
    }
    @ExceptionHandler(SpecialtyNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handleSpecialtyNotFoundException(SpecialtyNotFoundException e) {
        ModelAndView mav = new ModelAndView("/error/genericError");
        mav.addObject("message", "Specialty not found");
        return mav;
    }
}
