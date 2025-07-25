package ru.alex9043.productservice.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.alex9043.productservice.error.exception.InvalidImageException;
import ru.alex9043.productservice.service.FileStorageService;

import java.util.Base64;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ImageUtils {
    private final FileStorageService fileStorageService;

    public String uploadImageAndGetLink(String key, String base64Image) {
        if (base64Image == null || base64Image.isBlank())
            throw new InvalidImageException("Неправильный формат изображения");

        try {
            byte[] decodedImage = Base64.getDecoder().decode(base64Image);

            fileStorageService.uploadFile(key, decodedImage);

            return fileStorageService.getFileUrl(key);
        } catch (IllegalArgumentException e) {
            throw new InvalidImageException("Неправильный формат изображения");
        }
    }

    public void deleteImageByKey(String key) {
        fileStorageService.deleteFile(key);
    }

    public String getKey() {
        return UUID.randomUUID().toString();
    }
}
