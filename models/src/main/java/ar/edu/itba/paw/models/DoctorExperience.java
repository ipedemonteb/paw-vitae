package ar.edu.itba.paw.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "doctor_experience")
public class DoctorExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "doctor_experience_id_seq")
    @SequenceGenerator(name = "doctor_experience_id_seq", sequenceName = "doctor_experience_id_seq", allocationSize = 1)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "position_title", nullable = false)
    private String positionTitle;

    @Column(name = "organization_name", nullable = false)
    private String organizationName;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;


    public DoctorExperience() {}

    public DoctorExperience(Doctor doctor, String positionTitle, String organizationName, LocalDate startDate, LocalDate endDate) {
        this.doctor = doctor;
        this.positionTitle = positionTitle;
        this.organizationName = organizationName;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getId() {
        return id;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public String getPositionTitle() {
        return positionTitle;
    }

    public void setPositionTitle(String positionTitle) {
        this.positionTitle = positionTitle;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(positionTitle, organizationName, startDate, endDate);
    }
}
