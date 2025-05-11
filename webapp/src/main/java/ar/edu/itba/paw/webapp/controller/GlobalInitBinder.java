package ar.edu.itba.paw.webapp.controller;


import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

import java.beans.PropertyEditorSupport;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class GlobalInitBinder {

    public GlobalInitBinder() {
    }

    @InitBinder
    public void bindLocalTime(WebDataBinder binder) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        binder.registerCustomEditor(LocalTime.class, new PropertyEditorSupport() {
            @Override
            public String getAsText() {
                LocalTime value = (LocalTime) getValue();
                return value != null ? value.format(formatter) : "";
            }

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
        });
    }
}
