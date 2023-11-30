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

import static com.tenten.linkhub.global.response.ErrorCode.INVALID_USER_INPUT;
import static com.tenten.linkhub.global.response.ErrorCode.NOT_SUPPORTED_HTTP_MEDIA_TYPE;

@RestControllerAdvice
@Slf4j
public class GlobalApiExceptionHandler {

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorWithDetailCodeResponse> handleHttpMediaTypeNotSupportedException(HttpServletRequest request, HttpMediaTypeNotSupportedException e) {
        ErrorWithDetailCodeResponse errorWithDetailCodeResponse = ErrorWithDetailCodeResponse.of(
                NOT_SUPPORTED_HTTP_MEDIA_TYPE.getCode(),
                e.getMessage(),
                request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorWithDetailCodeResponse);
    }

    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorWithDetailCodeResponse> handleBindException(HttpServletRequest request, BindException e) {
        String detailMessage = getDetailMessage(e);
        ErrorWithDetailCodeResponse errorWithDetailCodeResponse = ErrorWithDetailCodeResponse.of(
                INVALID_USER_INPUT.getCode(),
                detailMessage,
                request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorWithDetailCodeResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorWithDetailCodeResponse> handleIllegalArgumentException(HttpServletRequest request, IllegalArgumentException e) {
        ErrorWithDetailCodeResponse errorWithDetailCodeResponse = ErrorWithDetailCodeResponse.of(
                INVALID_USER_INPUT.getCode(),
                e.getMessage(),
                request.getRequestURI());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorWithDetailCodeResponse);
    }

    @ExceptionHandler(PolicyViolationException.class)
    public ResponseEntity<ErrorWithDetailCodeResponse> handlePolicyViolationException(HttpServletRequest request, PolicyViolationException e) {
        ErrorWithDetailCodeResponse errorResponse = ErrorWithDetailCodeResponse.of(
                e.getErrorCode(),
                request.getRequestURI()
        );

        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
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
                .status(HttpStatus.CONFLICT)
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
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
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
            stringBuilder.append(",\n");
        }
        return stringBuilder.toString();
    }

}
