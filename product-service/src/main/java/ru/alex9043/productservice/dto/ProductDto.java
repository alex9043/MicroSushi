package ru.alex9043.productservice.dto;

import com.fasterxml.jackson.annotation.JsonRawValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
        private String name;
        private BigDecimal price;
    private String description;
    @JsonRawValue
    private String base64Image;
        private String imageKey;
        private String url;
}
