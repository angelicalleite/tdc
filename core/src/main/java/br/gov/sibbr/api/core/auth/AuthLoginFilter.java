package br.gov.sibbr.api.core.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * It will intercept POST requests in the "/login" login feature and
 * will try to authenticate the user
 */
public class AuthLoginFilter extends AbstractAuthenticationProcessingFilter {

    private AuthJwtProvider authJwtProvider;

    public AuthLoginFilter(String url, AuthenticationManager authenticationManager, AuthUserDetailsService authUserDetailsService) {
        super(new AntPathRequestMatcher(url));

        setAuthenticationManager(authenticationManager);
        authJwtProvider = new AuthJwtProvider(authUserDetailsService);
    }

    /**
     * During the authentication attempt, the user name and password of the request are retrieved,
     * then AuthenticationManager is used to verify that these details match an existing user.
     * If it occurs as expected the successfulAuthentication method method will be called.
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException {
        try {
            AuthCredentials credentials = new ObjectMapper().readValue(request.getInputStream(), AuthCredentials.class);
            UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());

            return this.getAuthenticationManager().authenticate(token);
        } catch (AuthenticationException ex) {
            throw new BadCredentialsException("Credential invalid");
        }
    }

    /**
     * In this method, we look for the authenticated user name and pass it to the TokenAuthenticationService,
     * which will add a JWT to the response
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) {
        authJwtProvider.authenticationHeader(response, authentication.getName());
    }

}
