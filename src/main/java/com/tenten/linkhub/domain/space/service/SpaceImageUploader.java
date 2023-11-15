package com.tenten.linkhub.domain.space.service;

import com.tenten.linkhub.global.aws.dto.ImageInfo;
import com.tenten.linkhub.global.aws.dto.ImageSaveRequest;
import com.tenten.linkhub.global.aws.s3.S3Uploader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class SpaceImageUploader {
    private static final String SPACE_IMAGE_FOLDER = "space-image/";
    private static final String SPACE_DEFAULT_IMAGE_PATH = "https://team-10-bucket.s3.ap-northeast-2.amazonaws.com/space-image/space-default.png";

    private final S3Uploader s3Uploader;

    public Optional<ImageInfo> getNewImageInfoOrEmptyImageInfo(MultipartFile file){
        if (file == null){
            return Optional.empty();
        }

        ImageSaveRequest imageSaveRequest = ImageSaveRequest.of(file, SPACE_IMAGE_FOLDER);
        return Optional.ofNullable(s3Uploader.saveImage(imageSaveRequest));
    }

    public ImageInfo getNewImageInfoOrDefaultImageInfo(MultipartFile file) {
        if (file == null){
            return ImageInfo.of(SPACE_DEFAULT_IMAGE_PATH, "default-image");
        }

        ImageSaveRequest imageSaveRequest = ImageSaveRequest.of(file, SPACE_IMAGE_FOLDER);
        return s3Uploader.saveImage(imageSaveRequest);
    }

}
