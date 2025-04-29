package ar.edu.itba.paw.webapp.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class AppointmentFileValidator implements ConstraintValidator<AppointmentFileValid, MultipartFile[]> {

    @Override
    public boolean isValid(MultipartFile[] multipartFiles, ConstraintValidatorContext context) {
        if (multipartFiles == null || multipartFiles.length == 0) {
            return true;
        }
        for (MultipartFile file : multipartFiles) {
            if (file == null || file.isEmpty()) {
                continue;
            }
            if (!"application/pdf".equals(file.getContentType())) {
                return false;
            }
        }
        return true;
    }
}
