package prgrms.project.starbooks.controller.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import prgrms.project.starbooks.util.exception.DataUnchangedException;

import java.util.NoSuchElementException;

import static prgrms.project.starbooks.controller.exception.ErrorResponse.responseOf;
import static prgrms.project.starbooks.controller.exception.ErrorType.*;

@Slf4j
@RestControllerAdvice(basePackages = "prgrms.project.starbooks.controller")
public class RestExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> catchBadRequest(IllegalArgumentException e) {
        log.error("Got invalid value: {}", e.getMessage());

        return ResponseEntity.badRequest().body(responseOf(INVALID_VALUE, e.getMessage()));
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ErrorResponse> catchNotFound(NoSuchElementException e) {
        log.error("Can't find entity: {}", e.getMessage());

        return ResponseEntity.badRequest().body(responseOf(ENTITY_NOT_FOUND, e.getMessage()));
    }

    @ExceptionHandler(DataUnchangedException.class)
    public ResponseEntity<ErrorResponse> catchServerError(DataUnchangedException e) {
        log.error("Got service error: {}", e.getMessage());

        return ResponseEntity.badRequest().body(responseOf(SERVER_ERROR, e.getMessage()));
    }
}
