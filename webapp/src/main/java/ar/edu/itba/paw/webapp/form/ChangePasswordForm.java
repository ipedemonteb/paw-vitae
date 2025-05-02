package ar.edu.itba.paw.webapp.form;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class ChangePasswordForm {

    @NotEmpty
    @Size(min = 6, max = 100)
    private String password;

    @NotEmpty
    @Size(min = 6, max = 100)
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
