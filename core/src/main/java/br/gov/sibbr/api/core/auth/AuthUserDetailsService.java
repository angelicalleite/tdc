package br.gov.sibbr.api.core.auth;

import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Date;

public interface AuthUserDetailsService extends UserDetailsService {

    Date tokenExpired(String username);
}
