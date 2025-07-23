package ru.alex9043.productservice.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link ru.alex9043.productservice.model.Product}
 */
public record UpdateProductDto(
        @Size(message = "Имя не может быть меньше 2 и больше 255 символов", min = 2, max = 255) String name,
        @Min(message = "Цена не может быть меньше 1", value = 1) BigDecimal price,
        @JsonRawValue
        String base64Image) implements Serializable {
}