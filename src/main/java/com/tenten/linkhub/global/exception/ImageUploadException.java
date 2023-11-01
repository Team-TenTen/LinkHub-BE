package com.tenten.linkhub.global.exception;

import com.tenten.linkhub.global.response.ErrorCode;
import lombok.Getter;

@Getter
public class ImageUploadException extends RuntimeException {

    private final ErrorCode errorCode;

    public ImageUploadException(final ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}
