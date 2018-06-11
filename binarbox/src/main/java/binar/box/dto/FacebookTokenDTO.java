package binar.box.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * Created by Timis Nicu Alexandru on 19-Apr-18.
 */
public class FacebookTokenDTO {

    @NotEmpty
    @NotNull
    private String token;

    public FacebookTokenDTO() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
