package ru.alex9043.productservice.error.exception;

public class InvalidImageException extends RuntimeException {
    public InvalidImageException(String message) {
        super(message);
    }
}
