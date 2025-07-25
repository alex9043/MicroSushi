package ru.alex9043.productservice.utils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.alex9043.productservice.error.exception.InvalidImageException;
import ru.alex9043.productservice.service.FileStorageService;

import java.util.Base64;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ImageUtils {
    private final FileStorageService fileStorageService;

    public String uploadImageAndGetLink(String key, String base64Image) {
        log.info("Попытка загрузить изображение по ключу и получение ссылки на него");
        if (base64Image == null || base64Image.isBlank()) {
            log.error("Неправильный формат изображения");
            throw new InvalidImageException("Неправильный формат изображения");
        }

        try {
            byte[] decodedImage = Base64.getDecoder().decode(base64Image);

            fileStorageService.uploadFile(key, decodedImage);

            return fileStorageService.getFileUrl(key);
        } catch (IllegalArgumentException e) {
            log.error("Неправильный формат изображения");
            throw new InvalidImageException("Неправильный формат изображения");
        }
    }

    public void deleteImageByKey(String key) {
        fileStorageService.deleteFile(key);
    }

    public String getKey() {
        log.info("Генерация ключа для изображения");
        return UUID.randomUUID().toString();
    }
}
