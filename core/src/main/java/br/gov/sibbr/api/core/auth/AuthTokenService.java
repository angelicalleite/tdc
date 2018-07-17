package br.gov.sibbr.api.core.auth;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
 * Service to create and get jwt tokens in the http request / response made to the service.
 */
public class AuthTokenService {

    private UserDetailsService userDetailsService;

    private final Long experiration = 1000L * 60L * 15L; // 15min : time for expiration token (1000 * 60 * 60 * 24 * 1 = 1day)
    private final String secret = "ThisIsASecret"; //
    private final String authorization = "Authorization";

    AuthTokenService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Query username informs that it represents a unique user identifier and mount the authentication token jwt
     * through the entered user data, and insert the token in the requests made to the service.
     */
    void addAuthenticationHeader(HttpServletResponse response, String username) {
        AuthUserDetails<UserDetails> auth = new AuthUserDetails<>(userDetailsService.loadUserByUsername(username));

        String token = Jwts.builder()
                .setSubject(username)
                .claim("user", auth)
                .setHeaderParam("typ", "JWT")
                .signWith(SignatureAlgorithm.HS256, secret)
                .setExpiration(new Date(System.currentTimeMillis() + experiration))
                .compact();

        response.addHeader(authorization, "Bearer " + token);
    }

    /**
     * Get jwt token from the request header and extract authentication data.
     */
    Authentication getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(authorization);

        // System.out.println(Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().get("user"));
        String user = token == null ? null : Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
        return user == null ? null : new AuthUserDetails<>(userDetailsService.loadUserByUsername(user));
    }
}
