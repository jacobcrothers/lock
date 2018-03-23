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

/**
 * Created by Timis Nicu Alexandru on 21-Mar-18.
 */
@Service
@Transactional
public class TokenService {


    @Autowired
    private TokenRepository tokenRepository;

    public TokenDto createUserToken(User user) {
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
                .sign(algorithmHS));
        token.setUser(user);
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
