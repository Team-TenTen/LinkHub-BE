package com.tenten.linkhub.global.aws.dto;

import org.springframework.web.multipart.MultipartFile;

public record ImageSaveRequest(MultipartFile file, String folder) {

    public static ImageSaveRequest of(MultipartFile file, String folder){
        return new ImageSaveRequest(file, folder);
    }

}
