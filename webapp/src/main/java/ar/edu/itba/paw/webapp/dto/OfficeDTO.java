package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.DoctorOffice;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

public class OfficeDTO {
    private String name;
    private String status;

    private URI doctor;
    private URI neighborhood;
    private URI officeAvailability;
    private URI officeSpecialties;
    private URI self;


    public static OfficeDTO fromDoctorOffice(DoctorOffice office, UriInfo uriInfo) {
        OfficeDTO dto = new OfficeDTO();

        dto.name = office.getOfficeName();
        dto.status = office.isActive() ? "active" : "inactive"; //TODO this good?

        String doctorId = String.valueOf(office.getDoctor().getId());
        String officeId = String.valueOf(office.getId());
        String neighborhoodId = String.valueOf(office.getNeighborhood().getId());
        dto.doctor = uriInfo.getBaseUriBuilder().path("doctors").path(doctorId).build();
        dto.neighborhood = uriInfo.getBaseUriBuilder().path("neighborhoods").path(neighborhoodId).build();
        dto.officeAvailability = uriInfo.getBaseUriBuilder().path("doctors").path(doctorId).path("offices").path(officeId).path("availability").build();
        dto.officeSpecialties = uriInfo.getBaseUriBuilder().path("doctors").path(doctorId).path("offices").path(officeId).path("specialties").build();
        dto.self = uriInfo.getBaseUriBuilder().path("doctors").path(doctorId).path("offices").path(officeId).build();

        return dto;
    }

    public static List<OfficeDTO> fromDoctorOffice(List<DoctorOffice> offices, UriInfo uriInfo) {
        return offices.stream().map(o -> fromDoctorOffice(o, uriInfo)).toList();
    }


    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
