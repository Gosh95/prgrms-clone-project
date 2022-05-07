package prgrms.project.starbooks.controller.exception;

public record ErrorResponse(
        int statusCode,
        String error,
        String errorMessage
) {

    public static ErrorResponse responseOf(ErrorType type, String errorMessage) {
        return new ErrorResponse(type.getStatusCode(), type.getError(), errorMessage);
    }
}