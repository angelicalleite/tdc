package br.gov.sibbr.api.core.exception;

import br.gov.sibbr.api.core.helper.Loggable;
import br.gov.sibbr.api.core.http.ErrorResponse;
import br.gov.sibbr.api.core.http.RestResponse;

import java.util.Arrays;
import java.util.List;

public interface InterfaceExceptionHandler extends RestResponse, Loggable {

    default ErrorResponse message(int status, int code, String path, String message, List<String> errors) {
        return new ErrorResponse(code, path, message, status, errors);
    }

    default ErrorResponse message(int status, int code, String path, String message, String... errors) {
        return new ErrorResponse(code, path, message, status, Arrays.asList(errors));
    }
}
