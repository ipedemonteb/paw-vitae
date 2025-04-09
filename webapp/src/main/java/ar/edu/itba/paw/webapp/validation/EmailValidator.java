package ar.edu.itba.paw.webapp.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraints.Email;

import ar.edu.itba.paw.interfaceServices.DoctorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

public class EmailValidator implements ConstraintValidator<EmailExistance, String> {

    @Autowired
    private DoctorService ds;

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return ds.getByEmail(s).isEmpty();
    }
}
