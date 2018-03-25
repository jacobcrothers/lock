package binar.box.dto;

import binar.box.domain.Token;

/**
 * Created by Timis Nicu Alexandru on 20-Mar-18.
 */
public class TokenDto {
    private String token;

    public TokenDto() {
    }

    public TokenDto(Token token) {
        this.token = token.getToken();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
