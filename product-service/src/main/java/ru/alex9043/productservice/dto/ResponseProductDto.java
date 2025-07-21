package ru.alex9043.productservice.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.UUID;

/**
 * DTO for {@link ru.alex9043.productservice.model.Product}
 */
public record ResponseProductDto(UUID id, String name, BigDecimal price) implements Serializable {
}