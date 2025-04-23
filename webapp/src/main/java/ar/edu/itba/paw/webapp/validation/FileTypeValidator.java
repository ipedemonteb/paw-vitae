package ar.edu.itba.paw.webapp.validation;

import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

public class FileTypeValidator implements ConstraintValidator<FileType, MultipartFile> {

    private List<String> allowedTypes;

    @Override
    public void initialize(FileType constraintAnnotation) {
        this.allowedTypes = Arrays.asList(constraintAnnotation.types());
    }

    @Override
    public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
        if (file == null || file.isEmpty()) {
            return true; // Skip validation if no file is uploaded
        }
        String contentType = file.getContentType();
        return contentType != null && allowedTypes.contains(contentType);
    }
}