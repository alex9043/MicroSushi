package ru.alex9043.productservice.error.dto;

public record ValidationError(
        String field,
        String message,
        String rejectValue
) {
}
