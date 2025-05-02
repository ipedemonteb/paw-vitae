package ar.edu.itba.paw.webapp.controller;


import ar.edu.itba.paw.interfaceServices.CoverageService;
import ar.edu.itba.paw.models.Coverage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalInitBinder {

    private final CoverageService coverageService;

    @Autowired
    public GlobalInitBinder(CoverageService coverageService) {
        this.coverageService = coverageService;
    }

    @InitBinder
    public void bindLocalTime(WebDataBinder binder) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        binder.registerCustomEditor(LocalTime.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                if (text == null || text.trim().isEmpty()) {
                    setValue(null);
                    return;
                }
                try {
                    setValue(LocalTime.parse(text, formatter));
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("Invalid time format: " + text, e);
                }
            }

            @Override
            public String getAsText() {
                LocalTime value = (LocalTime) getValue();
                return value != null ? value.format(formatter) : "";
            }
        });
    }

    @InitBinder
    public void coverageBinder(WebDataBinder binder) {
        // For any target property of type Coverage, convert from its String ID
        binder.registerCustomEditor(Coverage.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                long id = Long.parseLong(text);
                Coverage cov = coverageService
                        .findById(id)
                        .orElseThrow(() -> new IllegalArgumentException("Invalid coverage id: " + id));
                setValue(cov);
            }
        });
    }
}
