package binar.box.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Created by Timis Nicu Alexandru on 20-Mar-18.
 */
public class ChangePasswordDto {

    @NotEmpty
    @NotNull
    private String oldPassword;

    @NotEmpty
    @NotNull
    private String password;

    public ChangePasswordDto() {
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
