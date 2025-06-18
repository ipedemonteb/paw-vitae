package ar.edu.itba.paw.models;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "doctor_certifications")
public class DoctorCertification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @Column(name = "certificate_name", nullable = false)
    private String certificateName;

    @Column(name = "issuing_entity", nullable = false)
    private String issuingEntity;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    public DoctorCertification() {}

    public DoctorCertification(Doctor doctor, String certificateName, String issuingEntity, LocalDate issueDate) {
        this.doctor = doctor;
        this.certificateName = certificateName;
        this.issuingEntity = issuingEntity;
        this.issueDate = issueDate;
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

    public String getCertificateName() {
        return certificateName;
    }

    public void setCertificateName(String certificateName) {
        this.certificateName = certificateName;
    }

    public String getIssuingEntity() {
        return issuingEntity;
    }

    public void setIssuingEntity(String issuingEntity) {
        this.issuingEntity = issuingEntity;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }
}
