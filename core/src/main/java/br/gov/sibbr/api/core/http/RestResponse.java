package br.gov.sibbr.api.core.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import java.util.List;

/**
 * Mapper responses http, return value generic for ResponseEntity<?> in controllers application
 */
public interface RestResponse {

    default ResponseEntity<Void> ok() {
        return ResponseEntity.ok().build();
    }

    default ResponseEntity<Void> notFound() {
        return ResponseEntity.notFound().build();
    }

    default ResponseEntity<Void> noContent() {
        return ResponseEntity.noContent().build();
    }

    default ResponseEntity<Void> badRequest() {
        return ResponseEntity.badRequest().build();
    }

    default ResponseEntity<Void> unprocessableEntity() {
        return ResponseEntity.unprocessableEntity().build();
    }

    default <B> ResponseEntity<B> ok(B body) {
        return response(200, body);
    }

    default <B> ResponseEntity<B> created(B body) {
        return response(201, body);
    }

    default <B> ResponseEntity<B> accepted(B body) {
        return response(202, body);
    }

    default <B> ResponseEntity<B> nonAuthoritativeInformation(B body) {
        return response(203, body);
    }

    default <B> ResponseEntity<B> noContent(B body) {
        return response(204, body);
    }

    default <B> ResponseEntity<B> resetContent(B body) {
        return response(205, body);
    }

    default <B> ResponseEntity<B> partialContent(B body) {
        return response(206, body);
    }

    default <B> ResponseEntity<B> multiStatus(B body) {
        return response(207, body);
    }

    default <B> ResponseEntity<B> alreadyReported(B body) {
        return response(208, body);
    }

    default <B> ResponseEntity<B> imUsed(B body) {
        return response(226, body);
    }

    default <B> ResponseEntity<B> multipleChoices(B body) {
        return response(300, body);
    }

    default <B> ResponseEntity<B> movedPermanently(B body) {
        return response(301, body);
    }

    default <B> ResponseEntity<B> found(B body) {
        return response(302, body);
    }

    default <B> ResponseEntity<B> SeeOther(B body) {
        return response(303, body);
    }

    default <B> ResponseEntity<B> notModified(B body) {
        return response(304, body);
    }

    default <B> ResponseEntity<B> temporaryRedirect(B body) {
        return response(307, body);
    }

    default <B> ResponseEntity<B> permanentRedirect(B body) {
        return response(308, body);
    }

    default <B> ResponseEntity<B> badRequest(B body) {
        return response(400, body);
    }

    default <B> ResponseEntity<B> unauthorized(B body) {
        return response(401, body);
    }

    default <B> ResponseEntity<B> paymentRequired(B body) {
        return response(402, body);
    }

    default <B> ResponseEntity<B> forbidden(B body) {
        return response(403, body);
    }

    default <B> ResponseEntity<B> notFound(B body) {
        return response(404, body);
    }

    default <B> ResponseEntity<B> methodNotAllowed(B body) {
        return response(405, body);
    }

    default <B> ResponseEntity<B> notAcceptable(B body) {
        return response(406, body);
    }

    default <B> ResponseEntity<B> proxyAuthenticationRequired(B body) {
        return response(407, body);
    }

    default <B> ResponseEntity<B> requestTimeout(B body) {
        return response(408, body);
    }

    default <B> ResponseEntity<B> conflict(B body) {
        return response(409, body);
    }

    default <B> ResponseEntity<B> gone(B body) {
        return response(410, body);
    }

    default <B> ResponseEntity<B> lengthRequired(B body) {
        return response(411, body);
    }

    default <B> ResponseEntity<B> preconditionFailed(B body) {
        return response(412, body);
    }

    default <B> ResponseEntity<B> payloadTooLarge(B body) {
        return response(413, body);
    }

    default <B> ResponseEntity<B> uriTooLong(B body) {
        return response(414, body);
    }

    default <B> ResponseEntity<B> unsupportedMediaType(B body) {
        return response(415, body);
    }

    default <B> ResponseEntity<B> requestedRangeNotSatisfiable(B body) {
        return response(416, body);
    }

    default <B> ResponseEntity<B> expectationFailed(B body) {
        return response(417, body);
    }

    default <B> ResponseEntity<B> iAmATeapot(B body) {
        return response(418, body);
    }

    default <B> ResponseEntity<B> unprocessableEntity(B body) {
        return response(422, body);
    }

    default <B> ResponseEntity<B> locked(B body) {
        return response(423, body);
    }

    default <B> ResponseEntity<B> failedDependency(B body) {
        return response(424, body);
    }

    default <B> ResponseEntity<B> upgradeRequired(B body) {
        return response(426, body);
    }

    default <B> ResponseEntity<B> preconditionRequired(B body) {
        return response(428, body);
    }

    default <B> ResponseEntity<B> tooManyRequests(B body) {
        return response(429, body);
    }

    default <B> ResponseEntity<B> requestHeaderFieldsTooLarge(B body) {
        return response(431, body);
    }

    default <B> ResponseEntity<B> unavailableForLegalReasons(B body) {
        return response(451, body);
    }

    default <B> ResponseEntity<B> internalServerError(B body) {
        return response(500, body);
    }

    default <B> ResponseEntity<B> notImplemented(B body) {
        return response(501, body);
    }

    default <B> ResponseEntity<B> badGateway(B body) {
        return response(502, body);
    }

    default <B> ResponseEntity<B> serviceUnavailable(B body) {
        return response(503, body);
    }

    default <B> ResponseEntity<B> gatewayTimeout(B body) {
        return response(504, body);
    }

    default <B> ResponseEntity<B> httpVersionNotSupported(B body) {
        return response(505, body);
    }

    default <B> ResponseEntity<B> variantAlsoNegotiates(B body) {
        return response(506, body);
    }

    default <B> ResponseEntity<B> insufficientStorage(B body) {
        return response(507, body);
    }

    default <B> ResponseEntity<B> loopDetected(B body) {
        return response(508, body);
    }

    default <B> ResponseEntity<B> bandwidthLimitExceeded(B body) {
        return response(509, body);
    }

    default <B> ResponseEntity<B> notExtended(B body) {
        return response(510, body);
    }

    default <B> ResponseEntity<B> networkAuthenticationRequired(B body) {
        return response(511, body);
    }

    default ResponseEntity<?> errors(int status, String message) {
        return response(status, new ErrorResponse(message));
    }

    default ResponseEntity<?> errors(int code, String path, String message, int status, List<String> errors) {
        return response(status, new ErrorResponse(code, path, message, status, errors));
    }

    default ResponseEntity<?> message(int status, String message) {
        return response(status, new MessageResponse(message));
    }

    default <B> ResponseEntity<B> response(int status, B body) {
        return ResponseEntity.status(status).body(body);
    }

    default <B> ResponseEntity<B> response(int status, HttpHeaders headers, B body) {
        return ResponseEntity.status(status).headers(headers).body(body);
    }
}
