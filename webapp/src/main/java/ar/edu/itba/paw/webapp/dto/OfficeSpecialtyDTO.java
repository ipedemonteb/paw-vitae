package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.DoctorOfficeSpecialty;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

public class OfficeSpecialtyDTO {

    private URI specialty;
    private URI office;

    public static OfficeSpecialtyDTO fromDoctorOfficeSpecialty(DoctorOfficeSpecialty specialty, UriInfo uriInfo) {
        OfficeSpecialtyDTO dto = new OfficeSpecialtyDTO();

        dto.specialty = uriInfo.getBaseUriBuilder().path("specialties").path(String.valueOf(specialty.getSpecialty().getId())).build();
        dto.office = uriInfo.getBaseUriBuilder().path("doctors").path(String.valueOf(specialty.getOffice().getDoctor().getId())).path("offices").path(String.valueOf(specialty.getOffice().getId())).build();

        return dto;
    }

    public static List<OfficeSpecialtyDTO> fromDoctorOfficeSpecialty(List<DoctorOfficeSpecialty> specialties, UriInfo uriInfo) {
        return specialties.stream().map(s -> fromDoctorOfficeSpecialty(s, uriInfo)).toList();
    }

    public URI getSpecialty() {
        return specialty;
    }

    public void setSpecialty(URI specialty) {
        this.specialty = specialty;
    }

    public URI getOffice() {
        return office;
    }

    public void setOffice(URI office) {
        this.office = office;
    }
}
