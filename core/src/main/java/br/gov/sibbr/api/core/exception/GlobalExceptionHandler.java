package br.gov.sibbr.api.core.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;

public class GlobalExceptionHandler implements InterfaceExceptionHandler {

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity handleBadCredentials(final BadCredentialsException ex, HttpServletRequest http) {
        loggerError(ex);
        return unauthorized(message(401, 3000, http.getRequestURI(), ex.getMessage()));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity handleAccessDenied(AccessDeniedException ex, HttpServletRequest http) {
        loggerError(ex);
        return unauthorized(message(401, 3001, http.getRequestURI(), ex.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity handleAccessDenied(AuthenticationException ex, HttpServletRequest http) {
        loggerError(ex);
        return unauthorized(message(401, 3002, http.getRequestURI(), ex.getMessage()));
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity handleAccessUsernameNotFound(final UsernameNotFoundException ex, HttpServletRequest http) {
        loggerError(ex);
        return unauthorized(message(401, 3003, http.getRequestURI(), ex.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity handleBadInput(HttpMessageNotReadableException ex, HttpServletRequest http) {
        loggerError(ex);
        return badRequest(message(400, 3004, http.getRequestURI(), ex.getMessage()));
    }

    @ExceptionHandler(UnrecognizedPropertyException.class)
    public ResponseEntity handleJsonMapping(JsonMappingException ex, HttpServletRequest http) {
        loggerError(ex);
        return badRequest(message(400, 3005, http.getRequestURI(), ex.getMessage()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity handleDefaultException(final Exception ex, HttpServletRequest http) {
        loggerError(ex);
        return internalServerError(message(500, 8888, http.getRequestURI(), ex.getMessage()));
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity handleThrowable(final Throwable ex, HttpServletRequest http) {
        loggerError(ex);
        return internalServerError(message(500, 9999, http.getRequestURI(), ex.getMessage()));
    }
    }