package br.gov.sibbr.api.core.auth;

/**
 * Map POST request data made to the /login resource. The request data must contain the user name and password
 * for authentication
 */
public class AuthCredentials {

    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
