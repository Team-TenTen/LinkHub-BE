package com.tenten.linkhub.global.response;

import java.time.LocalDateTime;

public record ErrorWithDetailCodeResponse(
        String errorCode,
        String errorMessage,
        String requestURI,
        String time
) {
    public static ErrorWithDetailCodeResponse of(ErrorCode errorCode, String requestURI) {

        return new ErrorWithDetailCodeResponse(
                errorCode.getCode(),
                errorCode.getMessage(),
                requestURI,
                LocalDateTime.now().toString());
    }

}
