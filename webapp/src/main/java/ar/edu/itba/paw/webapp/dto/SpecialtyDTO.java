package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Coverage;
import ar.edu.itba.paw.models.Specialty;
import org.springframework.context.MessageSource;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class SpecialtyDTO {
    private String name;
    private URI self;


    public static SpecialtyDTO fromSpecialty(Specialty specialty, UriInfo uriInfo, MessageSource messageSource, Locale locale) {
        SpecialtyDTO res = new SpecialtyDTO();
        System.out.println("Locale: " + locale);
        System.out.println("Specialty key: " + specialty.getKey());
        System.out.println("Message: " + messageSource.getMessage(specialty.getKey(), null, specialty.getKey(), locale));
        res.name = messageSource.getMessage(specialty.getKey(), null, specialty.getKey(), locale);
        res.self = uriInfo.getBaseUriBuilder().path("api").path("specialties").path(String.valueOf(specialty.getId())).build();
        return res;
    }

    public static List<SpecialtyDTO> fromSpecialty(List<Specialty> specialties, UriInfo uriInfo, MessageSource messageSource, Locale locale) {
        return specialties.stream()
                .map(s -> fromSpecialty(s, uriInfo, messageSource, locale))
                .collect(Collectors.toList());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }
}