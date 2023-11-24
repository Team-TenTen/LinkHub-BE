package com.tenten.linkhub.global.exception;

import com.tenten.linkhub.global.response.ErrorResponse;
import com.tenten.linkhub.global.response.ErrorWithDetailCodeResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalApiExceptionHandler {

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleHttpMediaTypeNotSupportedException(HttpServletRequest request, HttpMediaTypeNotSupportedException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getMessage(), request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResponse> handleBindException(HttpServletRequest request, BindException e) {
        String detailMessage = getDetailMessage(e);
        ErrorResponse errorResponse = ErrorResponse.of(detailMessage, request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(HttpServletRequest request, IllegalArgumentException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getMessage(), request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErrorResponse> handleIllegalStateException(HttpServletRequest request, IllegalStateException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getMessage(), request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorResponse);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleDataNotFoundException(HttpServletRequest request, DataNotFoundException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getMessage(), request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccessException(HttpServletRequest request, UnauthorizedAccessException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getMessage(), request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(errorResponse);
    }

    @ExceptionHandler(ImageUploadException.class)
    public ResponseEntity<ErrorWithDetailCodeResponse> handleImageUploadException(HttpServletRequest request, ImageUploadException e) {
        ErrorWithDetailCodeResponse errorResponse = ErrorWithDetailCodeResponse.of(
                e.getErrorCode(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(errorResponse);
    }

    @ExceptionHandler(DataDuplicateException.class)
    public ResponseEntity<ErrorWithDetailCodeResponse> handleDataDuplicateException(HttpServletRequest request,
                                                                                    DataDuplicateException e) {
        ErrorWithDetailCodeResponse errorResponse = ErrorWithDetailCodeResponse.of(
                e.getErrorCode(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Void> handleException(HttpServletRequest request, Exception e) {
        log.error("Sever Exception Request URI {}: ", request.getRequestURI(), e);

        return ResponseEntity.internalServerError().build();
    }

    private static String getDetailMessage(BindException e) {
        BindingResult bindingResult = e.getBindingResult();
        StringBuilder stringBuilder = new StringBuilder();

        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            stringBuilder.append(fieldError.getField()).append(":");
            stringBuilder.append(fieldError.getDefaultMessage());
            stringBuilder.append(", ");
        }
        return stringBuilder.toString();
    }

}
