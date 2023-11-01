package com.tenten.linkhub.global.aws.dto;

public record ImageInfo(
        String path,
        String name
) {
    public static ImageInfo of(String path, String name){
        return new ImageInfo(path, name);
    }
}
