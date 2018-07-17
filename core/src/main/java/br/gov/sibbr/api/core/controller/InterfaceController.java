package br.gov.sibbr.api.core.controller;

import br.gov.sibbr.api.core.entity.InterfaceEntity;
import br.gov.sibbr.api.core.helper.Loggable;
import br.gov.sibbr.api.core.http.RestResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface InterfaceController<T extends InterfaceEntity<?>> extends RestResponse, Loggable {

    /**
     * Treatment and format error generater by BindResult in valid data post
     */
    default ResponseEntity errorFields(List<FieldError> fieldError) {

        Map<String, String> errors = new HashMap<>();
        fieldError.forEach(e -> errors.put(e.getField(), e.getDefaultMessage()));

        Map<String, Object> fields = new HashMap<>();
        fields.put("errorFields", errors);

        return badRequest(fields);
    }

    /**
     * Provider redirect of resource for url information
     */
    default void redirect(HttpServletResponse httpServletResponse, String url) throws IOException {
        httpServletResponse.sendRedirect(url);
    }

    /**
     * Provider redirect of resource for uri of route information and send resource in body request
     */
    default ResponseEntity redirect(UriComponentsBuilder uri, String route, Object body) {
        HttpHeaders headers = new HttpHeaders();

        UriComponents uriComponents = uri.path(route).build();
        headers.setLocation(uriComponents.toUri());
        // headers.setLocation(uri.path(route).build().toUri());

        return response(303, headers, body);
    }

}
