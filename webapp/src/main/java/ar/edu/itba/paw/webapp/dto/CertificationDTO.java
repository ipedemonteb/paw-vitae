package ar.edu.itba.paw.webapp.dto;

import ar.edu.itba.paw.models.DoctorCertification;

import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDate;
import java.util.List;

public class CertificationDTO {
    private String certificateName;
    private String issuingEntity;
    private LocalDate issueDate;

    private URI doctor;

    public static CertificationDTO fromDoctorCertification(DoctorCertification certification, UriInfo uriInfo) {
        CertificationDTO dto = new CertificationDTO();

        dto.certificateName = certification.getCertificateName();
        dto.issuingEntity = certification.getIssuingEntity();
        dto.issueDate = certification.getIssueDate();

        String doctorId = String.valueOf(certification.getDoctor().getId());

        dto.doctor = uriInfo.getBaseUriBuilder().path("doctors").path(doctorId).build();

        return dto;
    }

    public static List<CertificationDTO> fromDoctorCertification(List<DoctorCertification> certifications, UriInfo uriInfo) {
        return certifications.stream().map(c -> fromDoctorCertification(c, uriInfo)).toList();
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

    public URI getDoctor() {
        return doctor;
    }

    public void setDoctor(URI doctor) {
        this.doctor = doctor;
    }
}
