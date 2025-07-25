package ru.alex9043.productservice.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import ru.alex9043.productservice.dto.CreateProductDto;
import ru.alex9043.productservice.dto.ProductDto;
import ru.alex9043.productservice.dto.ResponseProductDto;
import ru.alex9043.productservice.dto.UpdateProductDto;
import ru.alex9043.productservice.error.exception.DuplicateResourceException;
import ru.alex9043.productservice.error.exception.ResourceNotFoundException;
import ru.alex9043.productservice.mapper.ProductMapper;
import ru.alex9043.productservice.model.Product;
import ru.alex9043.productservice.repo.ProductRepository;
import ru.alex9043.productservice.service.ProductService;
import ru.alex9043.productservice.utils.ImageUtils;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final String CACHE_NAME = "products";
    private final ProductRepository repository;
    private final ProductMapper mapper;
    private final ImageUtils imageUtils;

    @Override
    @Cacheable(cacheNames = CACHE_NAME, key = "'all'")
    public List<ResponseProductDto> getAllProducts() {
        List<Product> allProducts = repository.findAll();

        return mapper.toDtoList(allProducts);
    }

    @Override
    @Cacheable(cacheNames = CACHE_NAME, key = "#id")
    public ResponseProductDto getProduct(UUID id) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Продукта с id: " + id + " не существует"));

        return mapper.toDto(product);
    }

    @Override
    @Caching(
            evict = @CacheEvict(cacheNames = CACHE_NAME, key = "'all'"),
            put = @CachePut(cacheNames = CACHE_NAME, key = "#result.id()")
    )
    public ResponseProductDto createProduct(CreateProductDto createProductDto) {
        if (repository.existsByName(createProductDto.name())) {
            throw new DuplicateResourceException("Продукт с именем - " + createProductDto.name() + " уже существует");
        }

        ProductDto dto = mapper.toDto(createProductDto);
        dto.setImageKey(imageUtils.getKey());
        dto.setUrl(imageUtils.uploadImageAndGetLink(dto.getImageKey(), dto.getBase64Image()));

        Product rawProduct = mapper.toEntity(dto);

        Product savedProduct = repository.save(rawProduct);

        return mapper.toDto(savedProduct);
    }

    @Override
    @Caching(
            evict = @CacheEvict(cacheNames = CACHE_NAME, key = "'all'"),
            put = @CachePut(cacheNames = CACHE_NAME, key = "#id")
    )
    public ResponseProductDto updateProduct(UUID id, UpdateProductDto updateProductDto) {
        Product product = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Продукта с id: " + id + " не существует"));

        imageUtils.deleteImageByKey(product.getImageKey());

        ProductDto dto = mapper.toDto(updateProductDto);
        dto.setImageKey(imageUtils.getKey());
        dto.setUrl(imageUtils.uploadImageAndGetLink(dto.getImageKey(), dto.getBase64Image()));

        Product rawProduct = mapper.partialUpdate(dto, product);

        Product savedProduct = repository.save(rawProduct);

        return mapper.toDto(savedProduct);
    }

    @Caching(
            evict = {
                    @CacheEvict(cacheNames = CACHE_NAME, key = "'all'"),
                    @CacheEvict(cacheNames = CACHE_NAME, key = "#id")
            }
    )
    @Override
    public void deleteProduct(UUID id) {
        Product product = repository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Продукта с id: " + id + " не существует")
        );

        imageUtils.deleteImageByKey(product.getImageKey());

        repository.deleteById(id);
    }
}
