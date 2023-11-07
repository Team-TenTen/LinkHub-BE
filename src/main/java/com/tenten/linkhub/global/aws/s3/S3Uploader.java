package com.tenten.linkhub.global.aws.s3;

import com.tenten.linkhub.global.aws.dto.ImageInfo;
import com.tenten.linkhub.global.aws.dto.ImageSaveRequest;

import java.util.List;

public interface S3Uploader {

    ImageInfo saveImage(ImageSaveRequest request);

    void deleteImage(String fileName);

    void deleteImages(List<String> fileNames);

}
