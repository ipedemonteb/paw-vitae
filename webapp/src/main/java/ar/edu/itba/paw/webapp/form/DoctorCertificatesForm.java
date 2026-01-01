package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.models.CertificateForm;
import ar.edu.itba.paw.models.ExperienceForm;
import ar.edu.itba.paw.webapp.validation.ValidCertificate;

import javax.validation.constraints.Size;
import java.util.List;

public class DoctorCertificatesForm {


    @ValidCertificate(message = "{certificates.invalid}")
    @Size(max = 8)
    private List<CertificateForm> certificates;

    public List<CertificateForm> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<CertificateForm> certificates) {
        this.certificates = certificates;
    }
}
