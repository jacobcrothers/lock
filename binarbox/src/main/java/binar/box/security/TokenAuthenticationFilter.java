package binar.box.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import binar.box.service.TokenService;
import binar.box.util.Constants;
import binar.box.util.LockBridgesException;

/**
 * Created by Timis Nicu Alexandru on 23-Mar-18.
 */
public class TokenAuthenticationFilter extends OncePerRequestFilter {

	private TokenService tokenService;

	TokenAuthenticationFilter(TokenService tokenService) {
		this.tokenService = tokenService;

	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String token = request.getHeader(Constants.TOKEN_HEADER_REQUEST);
		if (token == null) {
			throw new LockBridgesException(Constants.AUTHENTICATION_TOKEN_NOT_FOUND);
		}
		boolean userToken = tokenService.checkFacebookToken(token);
		if (userToken) {

		} else {
			SecurityContextHolder.clearContext();
			throw new LockBridgesException(Constants.INVALID_TOKEN);
		}
	}
}
