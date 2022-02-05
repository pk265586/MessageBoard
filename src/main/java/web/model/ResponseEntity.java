package web.model;

import lombok.Getter;
import lombok.Setter;
import web.infrastructure.ResponseStatus;

/**
 * Class encapsulating some entity, response status and optional error message for web response.
 */
public class ResponseEntity<T> {
    @Getter @Setter
    private T body;

    @Getter @Setter
    private int statusCode;

    @Getter @Setter
    private String errorMessage;

    public boolean isError() {
        return errorMessage != null && errorMessage.length() > 0;
    }

    public ResponseEntity() {
    }

    /**
     * Constructor for creation of response with "Ok" status.
     * @param body Response body.
     */
    public ResponseEntity(T body) {
        this(body, ResponseStatus.OK, null);
    }

    public ResponseEntity(T body, int statusCode, String errorMessage) {
        this.body = body;
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }
}
