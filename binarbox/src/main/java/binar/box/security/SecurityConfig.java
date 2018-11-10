package binar.box.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import binar.box.service.TokenService;
import binar.box.util.Constants;

@Configuration
@EnableAutoConfiguration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private TokenService tokenService;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		http.requestCache().disable();
		http.csrf().disable();
		http.anonymous().disable();
		http.cors().disable();
		http.addFilterBefore(new TokenAuthenticationFilter(tokenService), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(HttpMethod.OPTIONS, Constants.CORS_URL_PATTERN)
				.antMatchers(Constants.SWAGGER_RESOURCES).antMatchers(Constants.SWAGGER_V2_API_DOCS)
				.antMatchers(Constants.SWAGGER_UI_HTML).antMatchers(Constants.SWAGGER_WEBJARS)
				.antMatchers(HttpMethod.POST,
						Constants.API + Constants.AUTHENTICATION_ENDPOINT + Constants.FACEBOOK_ENDPOINT)
				.and().debug(true);
	}
}
