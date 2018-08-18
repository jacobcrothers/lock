package binar.box.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import binar.box.domain.User;
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
		User user = tokenService.checkFacebookToken(token);
		Authentication authentication = new UsernamePasswordAuthenticationToken(user.getId(), user.getEmail());
		SecurityContext securityContext = SecurityContextHolder.getContext();
		securityContext.setAuthentication(authentication);
		filterChain.doFilter(request, response);
	}
}
