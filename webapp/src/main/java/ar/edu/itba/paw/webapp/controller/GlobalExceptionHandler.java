package ar.edu.itba.paw.webapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Objects;

@ControllerAdvice
public class GlobalExceptionHandler {

    // 404 – no handler matched
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handle404(NoHandlerFoundException ex) {
        ModelAndView mav = new ModelAndView("error/404");
        mav.addObject("path", ex.getRequestURL());
        return mav;
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ModelAndView handle403(AccessDeniedException ex) {
        ModelAndView mav = new ModelAndView("error/403");
        mav.addObject("message", "You don’t have permission to do that.");
        return mav;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handle400(MissingServletRequestParameterException ex) {
        ModelAndView mav = new ModelAndView("error/400");
        mav.addObject("param", ex.getParameterName());
        return mav;
    }

    // catch type‐mismatch (e.g. ?id=foo to Long)
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handle400(MethodArgumentTypeMismatchException ex) {
        ModelAndView mav = new ModelAndView("error/400");
        mav.addObject("value", ex.getValue());
        mav.addObject("requiredType", Objects.requireNonNull(ex.getRequiredType()).getSimpleName());
        return mav;
    }

    // catch JSON parse errors
    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView handle400(HttpMessageNotReadableException ex) {
        ModelAndView mav = new ModelAndView("error/400");
        mav.addObject("error", "Malformed request body");
        return mav;
    }

    // 500 – all other uncaught exceptions
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handle500(Exception ex) {
        ModelAndView mav = new ModelAndView("error/500");
        mav.addObject("exception", ex.getMessage());
        return mav;
    }
}
