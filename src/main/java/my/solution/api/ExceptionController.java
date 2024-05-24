package my.solution.api;

import my.solution.api.exceptions.*;
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

    @ExceptionHandler(ClientNotExistsException.class)
    public ResponseEntity<ApiErrorResponse> invalidClientException(Exception e) {
        final var httpResponseCode = HttpStatus.BAD_REQUEST;
        final var apiErrorResponse = new ApiErrorResponse(
                "No client with such id.",
                String.valueOf(httpResponseCode.value()),
                e.getClass().getCanonicalName(),
                e.getMessage(),
                Arrays.stream(e.getStackTrace())
                        .map(StackTraceElement::toString)
                        .toList()
        );
        return new ResponseEntity<>(apiErrorResponse, httpResponseCode);
    }

    @ExceptionHandler(NotEnoughMoneyException.class)
    public ResponseEntity<ApiErrorResponse> notEnoughMoneyException(Exception e) {
        final var httpResponseCode = HttpStatus.BAD_REQUEST;
        final var apiErrorResponse = new ApiErrorResponse(
                "Not enough money.",
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

    @ExceptionHandler(ValueAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> fieldAlreadyExistsException(Exception e) {
        final var httpResponseCode = HttpStatus.BAD_REQUEST;
        final var apiErrorResponse = new ApiErrorResponse(
                "This contact contains field(email, login, phone, etc.) that is already belong to someone!",
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
