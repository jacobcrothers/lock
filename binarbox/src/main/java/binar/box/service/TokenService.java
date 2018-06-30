package binar.box.service;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import binar.box.domain.Token;
import binar.box.domain.User;
import binar.box.dto.TokenDTO;
import binar.box.repository.TokenRepository;
import binar.box.util.Constants;
import binar.box.util.LockBridgesException;

/**
 * Created by Timis Nicu Alexandru on 21-Mar-18.
 */
@Service
@Transactional
public class TokenService {

	@Autowired
	private TokenRepository tokenRepository;

	TokenDTO createUserToken(User user, int days) {
		Token token = new Token();
		Algorithm algorithmHS;
		try {
			algorithmHS = Algorithm.HMAC256(Constants.RANDOM_SHA_256_SIGNATURE);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			throw new LockBridgesException(Constants.UNSUPORTED_ENCODING_EXCEPTION + e.getMessage());
		}
		token.setToken(JWT.create().withClaim(Constants.JWT_PAYLOAD_CLAIM_USER, user.getId()).withIssuedAt(new Date())
				.sign(algorithmHS));
		token.setUser(user);
		token.setCreatedDate(new Date());
		token.setLastModifiedDate(new Date());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.DATE, days);
		token.setExpirationTime(calendar.getTime());
		tokenRepository.save(token);
		return new TokenDTO(token);
	}

	public Token findOneByToken(String token) {
		return tokenRepository.findOneByToken(token)
				.orElseThrow(() -> new LockBridgesException(Constants.TOKEN_NOT_FOUND));
	}

	public DecodedJWT decodeJwtToken(Token userToken) {
		return JWT.decode(userToken.getToken());
	}
}
