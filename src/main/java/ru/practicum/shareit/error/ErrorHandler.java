package ru.practicum.shareit.error;

import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.exception.AccessDeniedException;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFound(final NotFoundException exception) {
        log.warn("400 {}", exception.getMessage(), exception);
        return new ErrorResponse(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleDuplicatedData(final DuplicatedDataException exception) {
        log.warn("409 {}", exception.getMessage(), exception);
        return new ErrorResponse(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleAccessDenied(final AccessDeniedException exception) {
        log.warn("403 {}", exception.getMessage(), exception);
        return new ErrorResponse(HttpStatus.FORBIDDEN, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleValidationException(final ValidationException exception) {
        log.warn("400 {}", exception.getMessage(), exception);
        return new ErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleIllegalArgument(final IllegalArgumentException exception) {
        log.warn("400 {}", exception.getMessage(), exception);
        return new ErrorResponse(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleConstraintValidationException(final ConstraintViolationException exception) {
        log.warn("400 {}", exception.getMessage(), exception);

        final List<Violation> violations = exception.getConstraintViolations().stream()
                .map(violation -> new Violation(violation.getPropertyPath().toString(), violation.getMessage()))
                .toList();

        return new ValidationErrorResponse(HttpStatus.BAD_REQUEST, violations);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ValidationErrorResponse handleMethodArgumentNotValid(final MethodArgumentNotValidException exception) {
        log.warn("400 {}", exception.getMessage(), exception);

        final List<Violation> violations = exception.getBindingResult().getFieldErrors().stream()
                .map(error -> new Violation(error.getField(), error.getDefaultMessage()))
                .toList();

        return new ValidationErrorResponse(HttpStatus.BAD_REQUEST, violations);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleOther(final Throwable exception) {
        log.warn("500 {}", exception.getMessage(), exception);
        return new ErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage());
    }
}
