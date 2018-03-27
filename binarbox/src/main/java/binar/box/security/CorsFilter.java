package binar.box.security;


import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Timis Nicu Alexandru on 25-Mar-18.
 */
@Component
public class CorsFilter implements Filter {


    public void init(FilterConfig filterConfig) {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;

        String origin = httpServletRequest.getHeader("Origin");
        if (origin != null) {
            httpServletResponse.setHeader("Access-Control-Allow-Origin", origin);
        }
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, DELETE, OPTIONS");
        httpServletResponse.setHeader("Access-Control-Max-Age", "3600");
        httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", "Content-Type, X-Requested-With, X-Custom-Header, token");
        httpServletResponse.setHeader("Access-Control-Expose-Headers", "X-PF");

        chain.doFilter(httpServletRequest, httpServletResponse);
    }

    public void destroy() {

    }

}
