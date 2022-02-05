package web.model;

import lombok.Getter;
import lombok.Setter;
import web.infrastructure.ResponseStatus;

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

    public ResponseEntity(T body) {
        this(body, ResponseStatus.OK, null);
    }

    public ResponseEntity(T body, int statusCode, String errorMessage) {
        this.body = body;
        this.statusCode = statusCode;
        this.errorMessage = errorMessage;
    }
}
