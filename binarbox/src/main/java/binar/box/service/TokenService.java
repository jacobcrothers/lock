package binar.box.service;

import binar.box.domain.Token;
import binar.box.domain.User;
import binar.box.dto.TokenDto;
import binar.box.repository.TokenRepository;
import binar.box.util.Constants;
import binar.box.util.LockBridgesException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Timis Nicu Alexandru on 21-Mar-18.
 */
@Service
@Transactional
public class TokenService {


    @Autowired
    private TokenRepository tokenRepository;

    public TokenDto createUserToken(User user, boolean rememberMe) {
        Token token = new Token();
        Algorithm algorithmHS;
        try {
            algorithmHS = Algorithm.HMAC256(Constants.RANDOM_SHA_256_SIGNATURE);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new LockBridgesException(Constants.UNSUPORTED_ENCODING_EXCEPTION + e.getMessage());
        }
        token.setToken(JWT.create()
                .withClaim(Constants.JWT_PAYLOAD_CLAIM_USER, user.getId())
                .withIssuedAt(new Date())
                .sign(algorithmHS));
        token.setUser(user);
        token.setCreatedDate(new Date());
        token.setLastModifiedDate(new Date());
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if (rememberMe) {
            calendar.add(Calendar.DATE, 7);
        } else {
            calendar.add(Calendar.HOUR, 1);
        }
        token.setExpirationTime(calendar.getTime());
        tokenRepository.save(token);
        return new TokenDto(token);
    }

    public Token findOneByToken(String token) {
        return tokenRepository.findOneByToken(token).orElseThrow(() -> new LockBridgesException(Constants.TOKEN_NOT_FOUND));
    }

    public DecodedJWT decodeJwtToken(Token userToken) {
        return JWT.decode(userToken.getToken());
    }
}
