package com.tenten.linkhub.global.aws.dto;

public record ImageInfo(
        String path,
        String filename
) {
    public static ImageInfo of(String path, String filename){
        return new ImageInfo(path, filename);
    }
}
