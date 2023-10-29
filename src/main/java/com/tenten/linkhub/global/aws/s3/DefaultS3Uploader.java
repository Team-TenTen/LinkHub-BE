package com.tenten.linkhub.global.aws.s3;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.tenten.linkhub.global.aws.dto.ImageInfo;
import com.tenten.linkhub.global.exception.ImageUploadException;
import com.tenten.linkhub.global.response.ErrorCode;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Slf4j
@Service
public class DefaultS3Uploader implements S3Uploader{

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    private final AmazonS3Client amazonS3Client;

    public DefaultS3Uploader(AmazonS3Client amazonS3Client) {
        this.amazonS3Client = amazonS3Client;
    }

    @Transactional
    @Override
    public ImageInfo saveImage(MultipartFile multipartFile, String folder) {
        String originalFileName = multipartFile.getOriginalFilename();
        String changedFileName = changeFileName(originalFileName, folder);
        try {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentType(multipartFile.getContentType());
            objectMetadata.setContentLength(multipartFile.getInputStream().available());

            amazonS3Client.putObject(bucket, changedFileName, multipartFile.getInputStream(), objectMetadata);

            String url = amazonS3Client.getUrl(bucket, changedFileName).toString();
            return new ImageInfo(url, changedFileName);

        } catch (IOException e) {
            throw new ImageUploadException(ErrorCode.FAIL_TO_UPLOAD_IMAGES);
        } catch (AmazonServiceException e) {
            log.error("uploadToAWS AmazonServiceException filePath={}, error={}", changedFileName, e.getMessage());
            throw new ImageUploadException(ErrorCode.INTERNAL_SERVER_ERROR);
        } catch (SdkClientException e) {
            log.error("uploadToAWS SdkClientException filePath={}, error = {}", changedFileName, e.getMessage());
            throw new ImageUploadException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void deleteImage(String fileName) {
        try {
            amazonS3Client.deleteObject(bucket, fileName);
        } catch (AmazonServiceException e) {
            log.error("uploadToAWS AmazonServiceException filePath={}, error={}", fileName, e.getMessage());
            throw new ImageUploadException(ErrorCode.INTERNAL_SERVER_ERROR);
        } catch (SdkClientException e) {
            log.error("uploadToAWS SdkClientException filePath={}, error = {}", fileName, e.getMessage());
            throw new ImageUploadException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private String changeFileName(String fileName, String folder) {
        String uuid = UUID.randomUUID().toString();
        return folder + uuid + "-" + fileName;
    }

}
