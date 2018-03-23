package binar.box.security;

import binar.box.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * Created by Timis Nicu Alexandru on 23-Mar-18.
 */
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
        http.addFilterBefore(new TokenAuthenticationFiler(tokenService), UsernamePasswordAuthenticationFilter.class);

    }


}
