package br.gov.sibbr.api.core.auth;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Service to create and get jwt tokens in the http request / response made to the service.
 */
public class AuthJwtProvider {

    private final Long experiration = 1000L * 60L * 60 * 24; //1day
    private final String secret = "ThisIsASecret";
    private final String authorization = "Authorization";

    private AuthUserDetailsService userDetailsService;

    AuthJwtProvider(AuthUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Query username informs that it represents a unique user identifier and mount the authentication token jwt
     * through the entered user data, and insert the token in the requests made to the service.
     */
    void authenticationHeader(HttpServletResponse response, String username) {
        AuthUserDetails<UserDetails> auth = new AuthUserDetails<>(userDetailsService.loadUserByUsername(username));
        response.addHeader(authorization, "Bearer " + generateToken(auth, username));
    }

    /**
     * Get jwt token from the request header and extract authentication data.
     * Teste: Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().get("user");
     */
    Authentication authenticate(HttpServletRequest request) {
        try {
            String token = request.getHeader(authorization);
            String user = token == null ? null : Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();

            return user == null ? null : new AuthUserDetails<>(userDetailsService.loadUserByUsername(user));
        } catch (ExpiredJwtException ex) {
            throw new BadCredentialsException("Token expired");
        }
    }

    /**
     * Genered Token JWT with informtion
     */
    private String generateToken(AuthUserDetails<UserDetails> auth, String username) {
        Date expired = userDetailsService.tokenExpired(username);

        return Jwts.builder()
                .setSubject(username)
                .claim("user", auth)
                .setHeaderParam("typ", "JWT")
                .signWith(SignatureAlgorithm.HS256, secret)
                .setExpiration(expired != null ? expired : new Date(System.currentTimeMillis() + experiration))
                .compact();
    }

}
