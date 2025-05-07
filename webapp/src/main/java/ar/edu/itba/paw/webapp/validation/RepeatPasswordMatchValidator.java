package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class RepeatPasswordMatchValidator implements ConstraintValidator<RepeatPasswordMatch, Object> {

    private String passwordField;
    private String repeatPasswordField;

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        return passwordField.equals(repeatPasswordField);
    }

    @Override
    public void initialize(RepeatPasswordMatch constraintAnnotation) {
        this.passwordField = constraintAnnotation.password();
        this.repeatPasswordField = constraintAnnotation.repeatPassword();
    }
}
