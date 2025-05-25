package ar.edu.itba.paw.webapp.controller;

import ar.edu.itba.paw.models.exception.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;

import javax.mail.MessagingException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    // 500 – all other uncaught exceptions
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handle500(Exception ex) {
        LOGGER.error("Unhandled exception occurred", ex);
        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("exception", ex.getMessage());
        return mav;
    }
    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNumberFormatException(NumberFormatException e) {
        LOGGER.error("Number format exception occurred", e);
        return new ModelAndView("/error/404");
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        LOGGER.error("Method argument type mismatch exception occurred", e);
        return new ModelAndView("/error/404");
    }
    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)    public ModelAndView handleUserNotFoundException(UserNotFoundException e) {
        LOGGER.error("User not found exception occurred", e);
        ModelAndView mav = new ModelAndView("/error/genericNotFoundError");
        mav.addObject("message", "user.notfound");
        return mav;
    }

    @ExceptionHandler(AppointmentNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleAppointmentNotFoundException(AppointmentNotFoundException e) {
        LOGGER.error("Appointment not found exception occurred", e);
        ModelAndView mav = new ModelAndView("/error/genericNotFoundError");
        mav.addObject("message", "appointment.notfound");
        return mav;
    }

    @ExceptionHandler(SpecialtyNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleSpecialtyNotFoundException(SpecialtyNotFoundException e) {
        LOGGER.error("Specialty not found exception occurred", e);
        ModelAndView mav = new ModelAndView("/error/genericNotFoundError");
        mav.addObject("message", "specialty.notfound");
        return mav;
    }

    @ExceptionHandler(FileNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleFileNotFoundException(FileNotFoundException e) {
        LOGGER.error("File not found exception occurred", e);
        ModelAndView mav = new ModelAndView("/error/genericNotFoundError");
        mav.addObject("message", "file.notfound");
        return mav;
    }
    @ExceptionHandler(CoverageNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleCoverageNotFoundException(CoverageNotFoundException e) {
        LOGGER.error("Coverage not found exception occurred", e);
        ModelAndView mav = new ModelAndView("/error/genericNotFoundError");
        mav.addObject("message", "coverage.notfound");
        return mav;
    }
}
