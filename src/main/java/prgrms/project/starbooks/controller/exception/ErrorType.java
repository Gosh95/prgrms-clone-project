package prgrms.project.starbooks.controller.exception;

import static org.springframework.http.HttpStatus.*;

public enum ErrorType {

    INVALID_VALUE(BAD_REQUEST.value(), "Invalid Value"),

    ENTITY_NOT_FOUND(NOT_FOUND.value(), "Not Found"),

    SERVER_ERROR(INTERNAL_SERVER_ERROR.value(), "Server error");

    private final int statusCode;
    private final String error;

    ErrorType(int statusCode, String error) {
        this.statusCode = statusCode;
        this.error = error;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getError() {
        return error;
    }
}
