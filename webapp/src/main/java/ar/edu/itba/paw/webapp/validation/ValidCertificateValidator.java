package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.CertificateForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;

public class ValidCertificateValidator implements ConstraintValidator<ValidCertificate, List<CertificateForm>> {


    @Override
    public boolean isValid(List<CertificateForm> certificateForms, ConstraintValidatorContext constraintValidatorContext) {
        if (certificateForms == null || certificateForms.isEmpty()) {
            return true;
        }

        for (CertificateForm certificate : certificateForms) {
            if (certificate.getCertificateName() == null || certificate.getCertificateName().isEmpty() ||
                certificate.getIssuingEntity() == null|| certificate.getIssuingEntity().isEmpty() ||
                certificate.getIssueDate() == null || certificate.getCertificateName().trim().isEmpty() || certificate.getIssuingEntity().trim().isEmpty()) {
                return false;
            }
            if (certificate.getIssueDate().isAfter(LocalDate.now())) {
                return false;
            }
            if(certificate.getCertificateName().length() > 100) return false;
            if(certificate.getIssuingEntity().length() > 100 ) return false;
        }
        return true;
    }
}
