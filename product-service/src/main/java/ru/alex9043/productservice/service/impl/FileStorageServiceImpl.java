package ru.alex9043.productservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.alex9043.productservice.service.FileStorageService;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.NoSuchBucketException;
import software.amazon.awssdk.services.s3.model.PutBucketPolicyRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

@Service
@RequiredArgsConstructor
public class FileStorageServiceImpl implements FileStorageService {
    private final S3Client s3Client;

    @Value("${minio.bucket}")
    private String bucketName;

    @Override
    public void uploadFile(String key, byte[] file) {
        if (!bucketExists(bucketName)) {
            createBucket();
        }
        s3Client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucketName)
                        .key(key)
                        .contentType("image/jpeg")
                        .build(),
                RequestBody.fromBytes(file)
        );
    }

    private void createBucket() {
        s3Client.createBucket(request -> request.bucket(bucketName));

        String policyJson = String.format("""
                {
                    "Version": "2012-10-17",
                    "Statement": [
                        {
                            "Effect": "Allow",
                            "Principal": "*",
                            "Action": "s3:GetObject",
                            "Resource": "arn:aws:s3:::%s/*"
                        }
                    ]
                }
                """, bucketName);

        s3Client.putBucketPolicy(PutBucketPolicyRequest.builder()
                .bucket(bucketName)
                .policy(policyJson)
                .build());
    }

    @Override
    public String getFileUrl(String key) {
        return s3Client.utilities()
                .getUrl(builder -> builder
                        .bucket(bucketName)
                        .key(key)).toString();
    }

    @Override
    public void deleteFile(String key) {
        s3Client.deleteObject(request -> request
                .bucket(bucketName)
                .key(key));
    }

    private boolean bucketExists(String bucketName) {
        try {
            s3Client.headBucket(request -> request.bucket(bucketName));
            return true;
        } catch (NoSuchBucketException e) {
            return false;
        }
    }
}
