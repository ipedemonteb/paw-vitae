package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.DoctorOffice;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class OfficeDTO {
    private String name;

    private URI doctor;
    private URI neighborhood;
    private URI officeAvailability;
    private URI officeSpecialties;


    public static OfficeDTO fromDoctorOffice(DoctorOffice office, UriInfo uriInfo) {
        OfficeDTO dto = new OfficeDTO();

        dto.name = office.getOfficeName();

        String doctorId = String.valueOf(office.getDoctor().getId());

        dto.doctor = uriInfo.getBaseUriBuilder().path("doctors").path(doctorId).build();
        dto.neighborhood = uriInfo.getBaseUriBuilder().path("neighborhood").queryParam("id", office.getNeighborhood().getId()).build();
        dto.officeAvailability = uriInfo.getBaseUriBuilder().path("doctors").path(doctorId).path("offices").path("availability").build();
        dto.officeSpecialties = uriInfo.getBaseUriBuilder().path("doctors").path(doctorId).path("offices").path("specialties").build();

        return dto;
    }

    public URI getOfficeAvailability() {
        return officeAvailability;
    }

    public void setOfficeAvailability(URI officeAvailability) {
        this.officeAvailability = officeAvailability;
    }

    public URI getOfficeSpecialties() {
        return officeSpecialties;
    }

    public void setOfficeSpecialties(URI officeSpecialties) {
        this.officeSpecialties = officeSpecialties;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public URI getDoctor() {
        return doctor;
    }

    public void setDoctor(URI doctor) {
        this.doctor = doctor;
    }

    public URI getNeighborhood() {
        return neighborhood;
    }

    public void setNeighborhood(URI neighborhood) {
        this.neighborhood = neighborhood;
    }
}
