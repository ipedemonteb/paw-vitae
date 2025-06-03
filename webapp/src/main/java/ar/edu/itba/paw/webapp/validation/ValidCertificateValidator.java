package ar.edu.itba.paw.webapp.validation;

import ar.edu.itba.paw.models.CertificateForm;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
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
                certificate.getIssueDate() == null) {
                return false;
            }
        }
        return true;
    }
}
