package binar.box.security;

import binar.box.service.TokenService;
import binar.box.util.Constants;
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

/**
 * Created by Timis Nicu Alexandru on 23-Mar-18.
 */
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
        http.authorizeRequests().antMatchers(HttpMethod.POST, Constants.API + Constants.LOCK_ENDPOINT + Constants.LOCK_TYPE_ENDPOINT).hasAuthority(Constants.ADMIN_AUTHORITY);
        http.authorizeRequests().antMatchers(HttpMethod.POST, Constants.API + Constants.LOCK_ENDPOINT + Constants.LOCK_TYPE_FILE_ENDPOINT).hasAuthority(Constants.ADMIN_AUTHORITY);

    }

    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers(HttpMethod.OPTIONS, Constants.CORS_URL_PATTERN)
                .antMatchers(Constants.SWAGGER_RESOURCES)
                .antMatchers(Constants.SWAGGER_V2_API_DOCS)
                .antMatchers(Constants.SWAGGER_UI_HTML)
                .antMatchers(Constants.SWAGGER_WEBJARS)
                .antMatchers(HttpMethod.POST, Constants.API + Constants.AUTHENTICATION_ENDPOINT + Constants.REGISTER_ENDPOINT)
                .antMatchers(HttpMethod.POST, Constants.API + Constants.AUTHENTICATION_ENDPOINT + Constants.LOGIN_ENDPOINT)
                .antMatchers(HttpMethod.POST, Constants.API + Constants.USER_ENDPOINT + Constants.REQUEST_RESET_PASSWORD_EDNPOINT)
                .antMatchers(HttpMethod.POST, Constants.API + Constants.USER_ENDPOINT + Constants.RESET_PASSWORD_ENDPOINT)
                .antMatchers(HttpMethod.POST, Constants.API + Constants.USER_ENDPOINT + Constants.CONFIRM_EMAIL_ENDPOINT)
                .and()
                .debug(true);
    }
}
