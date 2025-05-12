package ar.edu.itba.paw.webapp.form;

import ar.edu.itba.paw.webapp.validation.RepeatPasswordMatch;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@RepeatPasswordMatch(password = "password", repeatPassword = "repeatPassword", message = "{register.passwordsDoNotMatch}")
public class ChangePasswordForm {

    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$", message = "{password.invalid}")
    @Size(min = 8, max = 50)
    private String password;

    @NotEmpty
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d).{8,}$", message = "{password.invalid}")
    @Size(min = 8, max = 50)
    private String repeatPassword;

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public void setRepeatPassword(String repeatPassword) {
        this.repeatPassword = repeatPassword;
    }
    public String getRepeatPassword(){
        return repeatPassword;
    }

}
