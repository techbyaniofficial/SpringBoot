package com.example.RedisForOrderManagementSystem.exception;

import com.example.RedisForOrderManagementSystem.dto.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto(
                "USER_NOT_FOUND", ex.getMessage()
        ));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleOrderNotFoundException(OrderNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto(
                "ORDER_NOT_FOUND", ex.getMessage()
        ));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleProductNotFoundException(ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto(
                "PRODUCT_NOT_FOUND", ex.getMessage()
        ));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDto> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponseDto(
                "EMAIL_ALREADY_EXISTS", ex.getMessage()
        ));
    }

    @ExceptionHandler(DuplicateProductNameException.class)
    public ResponseEntity<ErrorResponseDto> handleDuplicateProductNameException(DuplicateProductNameException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponseDto(
                "DUPLICATE_PRODUCT_NAME", ex.getMessage()
        ));
    }

    @ExceptionHandler(OrderAccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleOrderAccessDeniedException(OrderAccessDeniedException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponseDto(
                "ORDER_ACCESS_DENIED", ex.getMessage()
        ));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponseDto> handleAccessDeniedException(AccessDeniedException ignored) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new ErrorResponseDto(
                "ACCESS_DENIED", "You are not allowed to perform this action"
        ));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponseDto> handleAuthenticationException(AuthenticationException ignored) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorResponseDto(
                "AUTH_FAILED", "Invalid email or password"
        ));
    }

    @ExceptionHandler(ProductInactiveException.class)
    public ResponseEntity<ErrorResponseDto> handleProductInactiveException(ProductInactiveException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDto(
                "PRODUCT_INACTIVE", ex.getMessage()
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        HashMap<String, String> fieldErrors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage()));
        StringBuilder errorMsg = new StringBuilder();
        boolean isFirst = true;
        for (String field : fieldErrors.keySet()) {
            if (!isFirst) {
                errorMsg.append(", ");
            }
            isFirst = false;
            errorMsg.append(field).append(" : ").append(fieldErrors.get(field));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDto(
                "INVALID_INPUT", errorMsg.toString()
        ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMessageNotReadableException(HttpMessageNotReadableException ignored) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDto(
                "MALFORMED_REQUEST", "Request body is missing or invalid JSON"
        ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorResponseDto> handleMethodArgumentTypeMismatchException(
            MethodArgumentTypeMismatchException ex) {
        String msg = "Invalid value for parameter '" + ex.getName() + "'";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDto(
                "INVALID_PARAMETER", msg
        ));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErrorResponseDto> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException ex) {
        String msg = "Required query parameter '" + ex.getParameterName() + "' is missing";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponseDto(
                "MISSING_PARAMETER", msg
        ));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED).body(new ErrorResponseDto(
                "METHOD_NOT_ALLOWED", ex.getMessage()
        ));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponseDto> handleHttpMediaTypeNotSupportedException(
            HttpMediaTypeNotSupportedException ex) {
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body(new ErrorResponseDto(
                "UNSUPPORTED_MEDIA_TYPE", ex.getMessage()
        ));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorResponseDto> handleNoResourceFoundException(NoResourceFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorResponseDto(
                "ENDPOINT_NOT_FOUND", "No handler for " + ex.getResourcePath()
        ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponseDto> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.debug("Data integrity violation: {}", ex.getMostSpecificCause().getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(new ErrorResponseDto(
                "DATA_INTEGRITY_VIOLATION", "The request could not be completed due to conflicting data"
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDto> handleGenericException(Exception ex) {
        log.error("Unhandled exception", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponseDto(
                "INTERNAL_ERROR", "An unexpected error occurred"
        ));
    }
}
