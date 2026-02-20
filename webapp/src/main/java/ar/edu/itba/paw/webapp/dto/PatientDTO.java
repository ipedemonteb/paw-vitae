package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Patient;

import javax.ws.rs.core.UriInfo;
import java.net.URI;

public class PatientDTO {

    private String name;
    private String lastName;
    private String email;
    private String phone;
    private URI coverage;
    private URI neighborhood;
    private URI appointments;
    private URI self;


    public static PatientDTO fromPatient(Patient patient, UriInfo uriInfo) {
        PatientDTO res = new PatientDTO();
        res.name = patient.getName();
        res.lastName = patient.getLastName();
        res.email = patient.getEmail();
        res.phone = patient.getPhone();

        String patientId = String.valueOf(patient.getId());

        if(patient.getCoverage() != null)        {
            res.coverage = uriInfo.getBaseUriBuilder().path("api").path("coverages").path(String.valueOf(patient.getCoverage().getId())).build();
        } else {
            res.coverage = null;
        }

        res.neighborhood = uriInfo.getBaseUriBuilder().path("api").path("neighborhoods").path(String.valueOf(patient.getNeighborhood().getId())).build();
        res.appointments = uriInfo.getBaseUriBuilder().path("api").path("appointments").queryParam("userId", patient.getId()).build();
        res.self = uriInfo.getBaseUriBuilder().path("api").path("patients").path(patientId).build();
        return res;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public URI getCoverage() {
        return coverage;
    }

    public URI getNeighborhood() {
        return neighborhood;
    }

    public URI getAppointments() {
        return appointments;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCoverage(URI coverages) {
        this.coverage = coverages;
    }

    public void setNeighborhood(URI neighborhood) {
        this.neighborhood = neighborhood;
    }

    public void setAppointments(URI appointments) {
        this.appointments = appointments;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }
}
