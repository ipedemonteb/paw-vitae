package ar.edu.itba.paw.models;

import java.time.LocalDate;

public class CertificateForm {

    private String certificateName;
    private String issuingEntity;
    private LocalDate issueDate;

    public CertificateForm() {
    }

    public CertificateForm(String certificateName, String issuingEntity, LocalDate issueDate) {
        this.certificateName = certificateName;
        this.issuingEntity = issuingEntity;
        this.issueDate = issueDate;
    }

    public CertificateForm fromEntity(DoctorCertification certification) {
        return new CertificateForm(
                certification.getCertificateName(),
                certification.getIssuingEntity(),
                certification.getIssueDate()
        );
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
