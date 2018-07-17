package br.gov.sibbr.api.core.http;

import java.util.Arrays;
import java.util.List;

/**
 * Mapper entity that representation message error http exception
 */
public class ErrorResponse {

    private int code;
    private int status;
    private String path;
    private String message;
    private List<String> errors;

    public ErrorResponse(String... errors) {
        this.errors = Arrays.asList(errors);
    }

    public ErrorResponse(int code, String path, String message, int status, List<String> errors) {
        this.code = code;
        this.path = path;
        this.message = message;
        this.status = status;
        this.errors = errors;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public List<String> getErrors() {
        return errors;
    }

    public void setErrors(List<String> errors) {
        this.errors = errors;
    }
}
