package ru.alex9043.productservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.alex9043.productservice.dto.CreateProductDto;
import ru.alex9043.productservice.dto.ResponseProductDto;
import ru.alex9043.productservice.dto.UpdateProductDto;
import ru.alex9043.productservice.error.exception.DuplicateResourceException;
import ru.alex9043.productservice.error.exception.ResourceNotFoundException;
import ru.alex9043.productservice.mapper.ProductMapper;
import ru.alex9043.productservice.model.Product;
import ru.alex9043.productservice.repo.ProductRepository;
import ru.alex9043.productservice.service.ProductService;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository repository;
    private final ProductMapper mapper;

    @Override
    public List<ResponseProductDto> getAllProducts() {
        List<Product> allProducts = repository.findAll();

        return mapper.toDtoList(allProducts);
    }

    @Override
    public ResponseProductDto getProduct(UUID id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Продукта с id: " + id + " не существует"));

        return mapper.toDto(product);
    }

    @Override
    public ResponseProductDto createProduct(CreateProductDto createProductDto) {
        if (repository.existsByName(createProductDto.name())) {
            throw new DuplicateResourceException("Продукт с именем - " + createProductDto.name() + " уже существует");
        }
        Product requestProduct = mapper.toEntity(createProductDto);

        Product savedProduct = repository.save(requestProduct);

        return mapper.toDto(savedProduct);
    }

    @Override
    public ResponseProductDto updateProduct(UUID id, UpdateProductDto updateProductDto) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Продукта с id: " + id + " не существует"));

        Product updatedProductEntity = mapper.partialUpdate(updateProductDto, product);

        Product updatedProduct = repository.save(updatedProductEntity);
        return mapper.toDto(updatedProduct);
    }

    @Override
    public void deleteProduct(UUID id) {
        if (!repository.existsById(id)) {
            throw new ResourceNotFoundException("Продукта с id: " + id + " не существует");
        }
        repository.deleteById(id);
    }
}
