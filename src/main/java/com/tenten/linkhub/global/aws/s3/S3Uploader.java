package com.tenten.linkhub.global.aws.s3;

import com.tenten.linkhub.global.aws.dto.ImageInfo;
import org.springframework.web.multipart.MultipartFile;

public interface S3Uploader {

    ImageInfo saveImage(MultipartFile multipartFile, String folder);

    void deleteImage(String fileName);
}
