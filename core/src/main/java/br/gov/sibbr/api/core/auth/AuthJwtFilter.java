package br.gov.sibbr.api.core.auth;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * Intercepts all requests made to the service to validate the presence of the JWT token. This validation is
 * done with the help of the TokenAuthenticationService class.
 */
public class AuthJwtFilter extends GenericFilterBean {

    private AuthUserDetailsService userDetailsService;

    public AuthJwtFilter(AuthUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        Authentication authentication = new AuthJwtProvider(userDetailsService).authenticate((HttpServletRequest) request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        filterChain.doFilter(request, response);
    }

}
