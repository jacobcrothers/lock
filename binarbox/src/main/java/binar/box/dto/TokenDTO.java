package binar.box.dto;

import binar.box.domain.Token;

import java.util.Date;

/**
 * Created by Timis Nicu Alexandru on 20-Mar-18.
 */
public class TokenDTO {
    private String token;
    private Date expirationTime;

    public TokenDTO() {
    }

    public TokenDTO(Token token) {
        this.token = token.getToken();
        this.expirationTime = token.getExpirationTime();
    }

    public Date getExpirationTime() {
        return expirationTime;
    }

    public void setExpirationTime(Date expirationTime) {
        this.expirationTime = expirationTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
