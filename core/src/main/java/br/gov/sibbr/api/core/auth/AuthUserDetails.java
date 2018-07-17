package br.gov.sibbr.api.core.auth;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * Manages user information (UserDetails) and Authentication (spring Authentication) of all request/response http
 */
public class AuthUserDetails<U extends UserDetails> implements Authentication {

    private U userDetails;
    private boolean authenticated = true;

    AuthUserDetails(U userDetails) {
        this.userDetails = userDetails;
    }

    @JsonIgnore
    @Override
    public U getDetails() {
        return this.userDetails;
    }

    @JsonIgnore
    @Override
    public Object getPrincipal() {
        return this.userDetails.getUsername();
    }

    @Override
    public String getName() {
        return getDetails().getUsername();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return getDetails().getAuthorities();
    }

    @JsonIgnore
    @Override
    public Object getCredentials() {
        return getDetails().getPassword();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

}