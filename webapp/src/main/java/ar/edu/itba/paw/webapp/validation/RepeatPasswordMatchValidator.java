package ar.edu.itba.paw.webapp.validation;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.reflect.Field;

public class RepeatPasswordMatchValidator implements ConstraintValidator<RepeatPasswordMatch, Object> {

    private String passwordField;
    private String repeatPasswordField;

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        try {
            Field passwordField = o.getClass().getDeclaredField(this.passwordField);
            Field repeatPasswordField = o.getClass().getDeclaredField(this.repeatPasswordField);
            passwordField.setAccessible(true);
            repeatPasswordField.setAccessible(true);
            String password = (String) passwordField.get(o);
            String repeatPassword = (String) repeatPasswordField.get(o);
            if (password == null || repeatPassword == null) {
                return true;
            }
            return password.equals(repeatPassword);

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void initialize(RepeatPasswordMatch constraintAnnotation) {
        this.passwordField = constraintAnnotation.password();
        this.repeatPasswordField = constraintAnnotation.repeatPassword();
    }
}
