package com.tenten.linkhub.global.aws.s3;

import com.tenten.linkhub.global.aws.dto.ImageInfo;
import com.tenten.linkhub.global.aws.dto.ImageSaveRequest;

public interface S3Uploader {

    ImageInfo saveImage(ImageSaveRequest request);

    void deleteImage(String fileName);

}
