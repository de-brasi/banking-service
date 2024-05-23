package my.solution.api;

import my.solution.api.exceptions.ContactValueAlreadyExistsException;
import my.solution.api.exceptions.InsufficientFundsException;
import my.solution.api.exceptions.LastContactRemoveException;
import my.solution.dto.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;

@RestControllerAdvice
public class ExceptionController {
    @ExceptionHandler(LastContactRemoveException.class)
    public ResponseEntity<ApiErrorResponse> deletingLastContactOfClientExceptionHandler(Exception e) {
        final var httpResponseCode = HttpStatus.BAD_REQUEST;
        final var apiErrorResponse = new ApiErrorResponse(
                "Trying to delete last significant client's contact.",
                String.valueOf(httpResponseCode.value()),
                e.getClass().getCanonicalName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::toString)
                        .toList()
        );
        return new ResponseEntity<>(apiErrorResponse, httpResponseCode);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> invalidRequestBodyException(Exception e) {
        final var httpResponseCode = HttpStatus.BAD_REQUEST;
        final var apiErrorResponse = new ApiErrorResponse(
                "Validation of request body failed.",
                String.valueOf(httpResponseCode.value()),
                e.getClass().getCanonicalName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::toString)
                        .toList()
        );
        return new ResponseEntity<>(apiErrorResponse, httpResponseCode);
    }

    @ExceptionHandler(InsufficientFundsException.class)
    public ResponseEntity<ApiErrorResponse> insufficientFundsExceptionHandler(Exception e) {
        final var httpResponseCode = HttpStatus.BAD_REQUEST;
        final var apiErrorResponse = new ApiErrorResponse(
                "Insufficient funds on account",
                String.valueOf(httpResponseCode.value()),
                e.getClass().getCanonicalName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::toString)
                        .toList()
        );
        return new ResponseEntity<>(apiErrorResponse, httpResponseCode);
    }

    @ExceptionHandler(ContactValueAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> alreadyExistsContactException(Exception e) {
        final var httpResponseCode = HttpStatus.BAD_REQUEST;
        final var apiErrorResponse = new ApiErrorResponse(
                "This contact is already belong to someone!",
                String.valueOf(httpResponseCode.value()),
                e.getClass().getCanonicalName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::toString)
                        .toList()
        );
        return new ResponseEntity<>(apiErrorResponse, httpResponseCode);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> internalServerExceptionHandler(Exception e) {
        final var httpResponseCode = HttpStatus.INTERNAL_SERVER_ERROR;
        final var apiErrorResponse = new ApiErrorResponse(
                "Internal server error.",
                String.valueOf(httpResponseCode.value()),
                e.getClass().getCanonicalName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::toString)
                        .toList()
        );
        return new ResponseEntity<>(apiErrorResponse, httpResponseCode);
    }
}
