package ru.alex9043.productservice.service;

import ru.alex9043.productservice.dto.CreateProductDto;
import ru.alex9043.productservice.dto.ResponseProductDto;
import ru.alex9043.productservice.dto.UpdateProductDto;

import java.util.List;
import java.util.UUID;

public interface ProductService {

    List<ResponseProductDto> getAllProducts();

    ResponseProductDto getProduct(UUID id);

    ResponseProductDto createProduct(CreateProductDto createProductDto);

    ResponseProductDto updateProduct(UUID id, UpdateProductDto updateProductDto);

    void deleteProduct(UUID id);
}
