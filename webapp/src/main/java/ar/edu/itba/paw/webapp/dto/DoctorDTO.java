package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.Doctor;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.util.List;

public class DoctorDTO {
    private String name;
    private String lastName;
    private String email;
    private String phone;
    private double rating;
    private int ratingCount;

    private URI specialties;
    private URI coverages;
    private URI offices;
    private URI ratings;
    private URI profile;
    private URI experiences;
    private URI certifications;
    private URI appointments;
    private URI self;

    public static DoctorDTO fromDoctor(Doctor doctor, UriInfo uriInfo) {
        DoctorDTO dto = new DoctorDTO();

        dto.name = doctor.getName();
        dto.lastName = doctor.getLastName();
        dto.email = doctor.getEmail();
        dto.phone = doctor.getPhone();
        dto.rating = doctor.getRating();
        dto.ratingCount = doctor.getRatingCount();

        String doctorId = String.valueOf(doctor.getId());

        dto.specialties = uriInfo.getBaseUriBuilder().path("doctors").path(doctorId).path("specialties").build();
        dto.coverages = uriInfo.getBaseUriBuilder().path("doctors").path(doctorId).path("coverages").build();
        dto.offices = uriInfo.getBaseUriBuilder().path("doctors").path(doctorId).path("offices").build();
        dto.profile = uriInfo.getBaseUriBuilder().path("doctors").path(doctorId).path("biography").build();
        dto.experiences = uriInfo.getBaseUriBuilder().path("doctors").path(doctorId).path("experiences").build();
        dto.certifications = uriInfo.getBaseUriBuilder().path("doctors").path(doctorId).path("certifications").build();
        dto.ratings = uriInfo.getBaseUriBuilder().path("ratings").queryParam("doctorId", doctorId).build();
        dto.appointments = uriInfo.getBaseUriBuilder().path("appointments").queryParam("doctorId", doctorId).build();
        dto.self = uriInfo.getBaseUriBuilder().path("doctors").path(doctorId).build();

        return dto;
    }

    public static List<DoctorDTO> fromDoctor(List<Doctor> doctors, UriInfo uriInfo) {
        return doctors.stream().map(d -> fromDoctor(d, uriInfo)).toList();
    }

    public URI getAppointments() {
        return appointments;
    }

    public void setAppointments(URI appointments) {
        this.appointments = appointments;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getRatingCount() {
        return ratingCount;
    }

    public void setRatingCount(int ratingCount) {
        this.ratingCount = ratingCount;
    }

    public URI getSpecialties() {
        return specialties;
    }

    public void setSpecialties(URI specialties) {
        this.specialties = specialties;
    }

    public URI getCoverages() {
        return coverages;
    }

    public void setCoverages(URI coverages) {
        this.coverages = coverages;
    }

    public URI getOffices() {
        return offices;
    }

    public void setOffices(URI offices) {
        this.offices = offices;
    }

    public URI getRatings() {
        return ratings;
    }

    public void setRatings(URI ratings) {
        this.ratings = ratings;
    }

    public URI getProfile() {
        return profile;
    }

    public void setProfile(URI profile) {
        this.profile = profile;
    }

    public URI getExperiences() {
        return experiences;
    }

    public void setExperiences(URI experiences) {
        this.experiences = experiences;
    }

    public URI getCertifications() {
        return certifications;
    }

    public void setCertifications(URI certifications) {
        this.certifications = certifications;
    }

    public URI getSelf() {
        return self;
    }

    public void setSelf(URI self) {
        this.self = self;
    }
}
