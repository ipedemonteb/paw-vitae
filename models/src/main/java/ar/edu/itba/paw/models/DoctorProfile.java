package ar.edu.itba.paw.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "doctor_profile")
public class DoctorProfile {

    @Id
    @Column(name = "doctor_id")
    private Long doctorId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "doctor_id")
    @JsonBackReference
    private Doctor doctor;

    @Column(name = "bio")
    private String bio;

    @Column(name = "description")
    private String description;

    public DoctorProfile() {
    }

    public DoctorProfile(Doctor doctor, String bio, String description) {
        this.doctor = doctor;
        this.bio = bio;
        this.description = description;
    }

    public Long getDoctorId() {
        return doctorId;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int hashCode() {
        return Objects.hash(doctor.hashCode(), bio, description);
    }
}
