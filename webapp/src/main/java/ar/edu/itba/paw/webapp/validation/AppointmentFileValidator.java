package ar.edu.itba.paw.webapp.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class AppointmentFileValidator implements ConstraintValidator<AppointmentFileValid, MultipartFile[]>{

        @Override
        public boolean isValid(MultipartFile[] multipartFiles, ConstraintValidatorContext constraintValidatorContext) {
            if (multipartFiles == null) {
                return true;
            }

            for (MultipartFile file : multipartFiles) {
                if (!Objects.equals(file.getContentType(), "application/pdf")) {
                    return false;
                }
            }
            return true;
        }
}
