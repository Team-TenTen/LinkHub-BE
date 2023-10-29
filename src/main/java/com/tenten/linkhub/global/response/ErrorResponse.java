package com.tenten.linkhub.global.response;

import java.time.LocalDateTime;

public record ErrorResponse(
        String errorMessage,
        String requestURI,
        String time
) {
    public static ErrorResponse of(String errorMessage, String requestURI) {
        return new ErrorResponse(
                errorMessage,
                requestURI,
                LocalDateTime.now().toString());
    }

}
