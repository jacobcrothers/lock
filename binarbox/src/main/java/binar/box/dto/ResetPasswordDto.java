package binar.box.dto;

import javax.validation.constraints.NotNull;

/**
 * Created by Timis Nicu Alexandru on 27-Mar-18.
 */
public class ResetPasswordDto {

    @NotNull
    @NotNull
    private String password;

    public ResetPasswordDto() {
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
