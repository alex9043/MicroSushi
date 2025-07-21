package ru.alex9043.productservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * DTO for {@link ru.alex9043.productservice.model.Product}
 */
public record CreateProductDto(
        @Size(message = "Имя не может быть меньше 2 и больше 255 символов", min = 2, max = 255)
        @NotBlank(message = "Имя не может быть пустым")
        String name,
        @NotNull(message = "Цена не может быть пустой")
        @Min(message = "Цена не может быть меньше 1", value = 1)
        BigDecimal price
) implements Serializable {
}