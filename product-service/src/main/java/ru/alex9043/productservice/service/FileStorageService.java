package ru.alex9043.productservice.service;

public interface FileStorageService {
    void uploadFile(String key, byte[] file);

    String getFileUrl(String key);

    void deleteFile(String key);
}
