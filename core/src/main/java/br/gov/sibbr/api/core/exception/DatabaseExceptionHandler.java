package br.gov.sibbr.api.core.exception;

import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataAccessResourceFailureException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.transaction.CannotCreateTransactionException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;
import javax.servlet.http.HttpServletRequest;
import java.net.ConnectException;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;

public class DatabaseExceptionHandler extends GlobalExceptionHandler {

    @ExceptionHandler(SQLException.class)
    public ResponseEntity handleDataSQLException(SQLException ex, HttpServletRequest http) {
        loggerError(ex);
        return internalServerError(message(500, 2001, http.getRequestURI(), ex.getMessage()));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity handleDataError(DataAccessException ex, HttpServletRequest http) {
        loggerError(ex);
        ex.printStackTrace();
        return internalServerError(message(500, 2002, http.getRequestURI(), ex.getMessage()));
    }

    @ExceptionHandler({CannotCreateTransactionException.class, DataAccessResourceFailureException.class})
    public ResponseEntity handleDataNotPresent(Exception ex, HttpServletRequest http) {
        loggerError(ex);
        return serviceUnavailable(message(503, 2004, http.getRequestURI(), ex.getMessage()));
    }

    @ExceptionHandler({CannotGetJdbcConnectionException.class, ConnectException.class})
    public ResponseEntity handleDataConnect(Exception ex, HttpServletRequest http) {
        loggerError(ex);
        return serviceUnavailable(message(503, 2005, http.getRequestURI(), ex.getMessage()));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity handleDataIntegrityViolation(DataIntegrityViolationException ex, HttpServletRequest http) {
        loggerError(ex);
        return conflict(message(409, 2007, http.getRequestURI(), "Restrição de integridade de dados violada: \n" + ex.getMostSpecificCause().getMessage()));
    }

    @ExceptionHandler(PersistenceException.class)
    public ResponseEntity<?> handleDataPersiste(PersistenceException ex, HttpServletRequest http) {
        loggerError(ex);
        return internalServerError(message(500, 2008, http.getRequestURI(), ex.getMessage()));
    }

    @ExceptionHandler(EntityExistsException.class)
    public ResponseEntity handleDataEntityExists(EntityExistsException ex, HttpServletRequest http) {
        loggerError(ex);
        return conflict(message(409, 2009, http.getRequestURI(), ex.getMessage()));
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity handleDataEntityNotFound(EntityNotFoundException ex, HttpServletRequest http) {
        loggerError(ex);
        return internalServerError(message(500, 2010, http.getRequestURI(), ex.getMessage()));
    }

    @ExceptionHandler(NoResultException.class)
    public ResponseEntity handleDataNoResult(NoResultException ex, HttpServletRequest http) {
        loggerError(ex);
        return internalServerError(message(500, 2011, http.getRequestURI(), ex.getMessage()));
    }

    @ExceptionHandler(DuplicateKeyException.class)
    public ResponseEntity handleDataDuplicateKey(DuplicateKeyException ex, HttpServletRequest http) {
        loggerError(ex);
        return conflict(message(409, 2012, http.getRequestURI(), ex.getMessage()));
    }

    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public ResponseEntity handleDataConstraintViolation(SQLIntegrityConstraintViolationException ex, HttpServletRequest http) {
        loggerError(ex);
        return internalServerError(message(500, 2016, http.getRequestURI(), ex.getMessage()));
    }

}
