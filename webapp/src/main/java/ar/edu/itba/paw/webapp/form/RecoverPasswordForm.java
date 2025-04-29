package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class RecoverPasswordForm {

    @NotBlank(message = "{recover.password.email.required}")
    @Email(message = "{recover.password.email.invalid}")
    private String email;

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "RecoverPasswordForm{" +
                "email='" + email + '\'' +
                '}';
    }
}