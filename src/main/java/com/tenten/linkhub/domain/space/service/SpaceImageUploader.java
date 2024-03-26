package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.global.aws.dto.ImageInfo;
import com.tenten.linkhub.global.aws.dto.ImageSaveRequest;
import com.tenten.linkhub.global.aws.s3.ImageFileUploader;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Component
public class SpaceImageUploader {
    private static final String SPACE_IMAGE_FOLDER = "space-image/";
    private final String SPACE_DEFAULT_IMAGE_PATH;

    private final ImageFileUploader imageFileUploader;

    public SpaceImageUploader(
            @Value("${cloud.aws.s3.space-default-image-path}")
            String spaceDefaultImagePath,
            ImageFileUploader imageFileUploader
    ) {
        this.SPACE_DEFAULT_IMAGE_PATH = spaceDefaultImagePath;
        this.imageFileUploader = imageFileUploader;
    }

    public Optional<ImageInfo> getNewImageInfoOrEmptyImageInfo(MultipartFile file){
        if (file == null){
            return Optional.empty();
        }

        ImageSaveRequest imageSaveRequest = ImageSaveRequest.of(file, SPACE_IMAGE_FOLDER);
        return Optional.ofNullable(imageFileUploader.saveImage(imageSaveRequest));
    }

    public ImageInfo getNewImageInfoOrDefaultImageInfo(MultipartFile file) {
        if (file == null){
            return ImageInfo.of(SPACE_DEFAULT_IMAGE_PATH, "default-image");
        }

        ImageSaveRequest imageSaveRequest = ImageSaveRequest.of(file, SPACE_IMAGE_FOLDER);
        return imageFileUploader.saveImage(imageSaveRequest);
    }

}
