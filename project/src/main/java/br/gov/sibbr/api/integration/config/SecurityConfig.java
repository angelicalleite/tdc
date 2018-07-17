package br.gov.sibbr.api.integration.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Configuration properties security aplication
 */
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Config security of aplication and mapper router and permissions
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.headers().cacheControl().disable();

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.GET).permitAll()
                .antMatchers(HttpMethod.POST).hasAuthority("APP")
                .antMatchers(HttpMethod.PUT).hasAuthority("APP")
                .antMatchers(HttpMethod.DELETE).hasAuthority("APP")
                .anyRequest().authenticated()
                .and().httpBasic();
    }

    /**
     * Configure authentication verification for request resource fix in memory
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("admin").password("admin").authorities("APP");
    }

}