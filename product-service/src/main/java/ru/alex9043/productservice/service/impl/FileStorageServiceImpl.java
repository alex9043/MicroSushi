package ru.alex9043.productservice.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {
    private final S3Client s3Client;

    @Value("${minio.bucket}")
    private String bucketName;

    @Override
    public void uploadFile(String key, byte[] file) {
        log.info("Попытка получить изображение по ключу");
        log.debug("Ключ - {}", key);
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
        log.info("Изображение получено");
    }

    private void createBucket() {
        log.info("Попытка создать bucket");
        s3Client.createBucket(request -> request.bucket(bucketName));
        log.info("bucket создан");
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
        log.info("В bucket установлены права для чтения");
    }

    @Override
    public String getFileUrl(String key) {
        log.info("Попытка получить url для изображения по ключу");
        log.debug("Ключ - {}", key);
        return s3Client.utilities()
                .getUrl(builder -> builder
                        .bucket(bucketName)
                        .key(key)).toString();
    }

    @Override
    public void deleteFile(String key) {
        log.info("Попытка удалить изображение по ключу");
        log.debug("Ключ - {}", key);
        s3Client.deleteObject(request -> request
                .bucket(bucketName)
                .key(key));
    }

    private boolean bucketExists(String bucketName) {
        log.info("Проверка на существование bucket с именем - {}", bucketName);
        try {
            s3Client.headBucket(request -> request.bucket(bucketName));
            log.info("Bucket существует");
            return true;
        } catch (NoSuchBucketException e) {
            log.error("Bucket не существует");
            return false;
        }
    }
}
