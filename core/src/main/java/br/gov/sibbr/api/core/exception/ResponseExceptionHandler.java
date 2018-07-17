package br.gov.sibbr.api.core.exception;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

public class ResponseExceptionHandler extends DatabaseExceptionHandler {

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity handleNoHandlerFoundException(NoHandlerFoundException ex, HttpServletRequest http) {
        loggerError(ex);

        return badRequest(message(400, 1001, http.getRequestURI(), ex.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity handleMethodArgumentNotValid(MethodArgumentNotValidException ex, final HttpServletRequest http) {
        loggerError(ex);

        List<String> errors = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> errors.add(error.getField() + ": " + error.getDefaultMessage()));
        ex.getBindingResult().getGlobalErrors().forEach(error -> errors.add(error.getObjectName() + ": " + error.getDefaultMessage()));

        return badRequest(message(400, 1002, http.getRequestURI(), ex.getMessage(), errors));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity handleMissingServletRequestParameter(MissingServletRequestParameterException ex, HttpServletRequest http) {
        loggerError(ex);
        return badRequest(message(400, 1003, http.getRequestURI(), ex.getMessage(), ex.getParameterName() + " param not informed"));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpServletRequest http) {
        loggerError(ex);
        return methodNotAllowed(message(405, 1004, http.getRequestURI(), ex.getMessage(), ex.getMethod() + " method not supported for this request"));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity handleHttpMediaTypeNotSupported(HttpMediaTypeNotSupportedException ex, HttpServletRequest http) {
        loggerError(ex);

        StringBuilder error = new StringBuilder().append(ex.getContentType()).append(" type media not support. Media support is: ");
        ex.getSupportedMediaTypes().forEach(e -> error.append(e).append(", "));

        return unsupportedMediaType(message(415, 1005, http.getRequestURI(), ex.getMessage(), error.substring(0, error.length() - 2)));
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity handleHttpMediaTypeNotAcceptableException(HttpMediaTypeNotAcceptableException ex, HttpServletRequest http) {
        loggerError(ex);
        return notFound(message(406, 1009, http.getRequestURI(), ex.getMessage()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity handleConstraintViolation(ConstraintViolationException ex, HttpServletRequest http) {
        loggerError(ex);

        List<String> errors = new ArrayList<>();
        ex.getConstraintViolations().forEach(e -> errors.add(e.getRootBeanClass().getName() + " " + e.getPropertyPath() + ": " + e.getMessage()));

        return badRequest(message(400, 1006, http.getRequestURI(), ex.getMessage(), errors));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex, HttpServletRequest http) {
        loggerError(ex);

        String error = ex.getName() + " deve ser do tipo " + ex.getRequiredType().getName();

        return badRequest(message(400, 1007, http.getRequestURI(), ex.getMessage(), error));
    }

    @ExceptionHandler(ExceptionInInitializerError.class)
    public ResponseEntity handleExceptionInternal(ExceptionInInitializerError ex, HttpServletRequest http) {
        loggerError(ex);
        return internalServerError(message(500, 1008, http.getRequestURI(), ex.getMessage()));
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public ResponseEntity handleExpiredJwtException(ExpiredJwtException ex, HttpServletRequest http) {
        loggerError(ex);
        return internalServerError(message(500, 1009, http.getRequestURI(), ex.getMessage()));
    }

    @ExceptionHandler(SignatureException.class)
    public ResponseEntity handleSignatureException(SignatureException ex, HttpServletRequest http) {
        loggerError(ex);
        return internalServerError(message(500, 1010, http.getRequestURI(), ex.getMessage()));
    }

    @ExceptionHandler(MultipartException.class)
    ResponseEntity<?> handleMultipartException(MultipartException ex, HttpServletRequest http) {
        loggerError(ex);
        return payloadTooLarge(message(500, 1221, http.getRequestURI(), ex.getMessage()));
    }
}
