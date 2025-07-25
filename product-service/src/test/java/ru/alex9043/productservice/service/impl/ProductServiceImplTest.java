package ru.alex9043.productservice.service.impl;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.alex9043.productservice.dto.CreateProductDto;
import ru.alex9043.productservice.dto.ProductDto;
import ru.alex9043.productservice.dto.ResponseProductDto;
import ru.alex9043.productservice.dto.UpdateProductDto;
import ru.alex9043.productservice.error.exception.DuplicateResourceException;
import ru.alex9043.productservice.error.exception.ResourceNotFoundException;
import ru.alex9043.productservice.mapper.ProductMapper;
import ru.alex9043.productservice.model.Product;
import ru.alex9043.productservice.repo.ProductRepository;
import ru.alex9043.productservice.utils.ImageUtils;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    ProductRepository repository;

    @Mock
    ProductMapper mapper;

    @Mock
    ImageUtils imageUtils;

    @InjectMocks
    ProductServiceImpl service;

    Product product;
    ResponseProductDto dto;

    @BeforeEach
    void SetUp() {
        UUID uuid = UUID.randomUUID();
        String name = "test";
        BigDecimal price = BigDecimal.ONE;
        String imageKey = "testKey";
        String url = "testUrl";
        product = new Product();
        product.setId(uuid);
        product.setName(name);
        product.setPrice(price);
        product.setImageKey(imageKey);
        product.setUrl(url);

        dto = new ResponseProductDto(uuid, name, price, url);
    }

    @AfterEach
    void TearDown() {
        verifyNoMoreInteractions(repository, mapper, imageUtils);
    }

    @Test
    void getAllProducts_ReturnsListProductsDto_WhenFound() {
        List<Product> products = List.of(product, product);
        List<ResponseProductDto> dtoList = List.of(dto, dto);

        when(repository.findAll()).thenReturn(products);
        when(mapper.toDtoList(any())).thenReturn(dtoList);

        List<ResponseProductDto> allProducts = service.getAllProducts();

        assertEquals(dtoList.size(), allProducts.size());
        assertEquals(dtoList, allProducts);

        verify(repository, times(1)).findAll();
        verify(mapper, times(1)).toDtoList(products);
    }

    @Test
    void getAllProducts_ReturnsEmptyList_WhenEmpty() {
        List<Product> emptyList = Collections.emptyList();
        when(repository.findAll()).thenReturn(emptyList);

        List<ResponseProductDto> allProducts = service.getAllProducts();

        assertEquals(0, allProducts.size());
        verify(repository, times(1)).findAll();
        verify(mapper, times(1)).toDtoList(emptyList);
    }

    @Test
    void getProduct_ReturnsDto_WhenFound() {
        UUID uuid = UUID.randomUUID();
        when(repository.findById(any())).thenReturn(Optional.of(product));
        when(mapper.toDto((Product) any())).thenReturn(dto);

        ResponseProductDto actual = service.getProduct(uuid);

        assertEquals(dto, actual);
        verify(repository, times(1)).findById(uuid);
        verify(mapper, times(1)).toDto(product);
    }

    @Test
    void getProduct_ThrowsResourceNotFoundException_WhenNotFound() {
        UUID uuid = UUID.randomUUID();

        when(repository.findById(any())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.getProduct(uuid));

        assertEquals("Продукта с id: " + uuid + " не существует", exception.getMessage());
        verify(repository, times(1)).findById(uuid);
    }

    @Test
    void createProduct_returnsDto_WhenSaved() {
        String imageUuid = UUID.randomUUID().toString();
        String url = "http://test.test";
        CreateProductDto testDto = new CreateProductDto("test", BigDecimal.ONE, "test");
        ProductDto productDto = new ProductDto("test", BigDecimal.ONE, "test", "test", "test");

        when(repository.existsByName(any())).thenReturn(false);
        when(mapper.toDto((CreateProductDto) any())).thenReturn(productDto);
        when(imageUtils.getKey()).thenReturn(imageUuid);
        when(imageUtils.uploadImageAndGetLink(any(), any())).thenReturn(url);
        when(mapper.toEntity(any())).thenReturn(product);
        when(repository.save(any())).thenReturn(product);
        when(mapper.toDto((Product) any())).thenReturn(dto);

        ResponseProductDto actual = service.createProduct(testDto);

        assertEquals(dto, actual);

        verify(repository, times(1)).existsByName(testDto.name());
        verify(mapper, times(1)).toDto(testDto);
        verify(imageUtils, times(1)).getKey();
        verify(imageUtils, times(1)).uploadImageAndGetLink(productDto.getImageKey(), productDto.getBase64Image());
        verify(mapper, times(1)).toEntity(productDto);
        verify(repository, times(1)).save(product);
        verify(mapper, times(1)).toDto(product);
    }

    @Test
    void createProduct_throwsDuplicateResourceException_WhenExist() {
        CreateProductDto testDto = new CreateProductDto("test", BigDecimal.ONE, "test");
        when(repository.existsByName(any())).thenReturn(true);

        DuplicateResourceException exception = assertThrows(
                DuplicateResourceException.class,
                () -> service.createProduct(testDto));

        assertEquals("Продукт с именем - " + testDto.name() + " уже существует", exception.getMessage());
        verify(repository, times(1)).existsByName(testDto.name());
    }

    @Test
    void updateProduct_ReturnsDto_WhenUpdated() {
        UUID uuid = UUID.randomUUID();
        String url = "http://test.test";
        UpdateProductDto testDto = new UpdateProductDto("test", BigDecimal.ONE, "test");
        ProductDto productDto = new ProductDto("test", BigDecimal.ONE, "test", "test", "test");

        when(repository.findById(any())).thenReturn(Optional.of(product));
        when(mapper.toDto((UpdateProductDto) any())).thenReturn(productDto);
        when(imageUtils.getKey()).thenReturn(uuid.toString());
        when(imageUtils.uploadImageAndGetLink(any(), any())).thenReturn(url);
        when(mapper.partialUpdate(any(), any())).thenReturn(product);
        when(repository.save(any())).thenReturn(product);
        when(mapper.toDto((Product) any())).thenReturn(dto);

        ResponseProductDto actual = service.updateProduct(uuid, testDto);

        assertEquals(dto, actual);

        verify(repository, times(1)).findById(uuid);
        verify(imageUtils, times(1)).deleteImageByKey(product.getImageKey());
        verify(mapper, times(1)).toDto(testDto);
        verify(imageUtils, times(1)).getKey();
        verify(imageUtils, times(1)).uploadImageAndGetLink(productDto.getImageKey(), productDto.getBase64Image());
        verify(mapper, times(1)).partialUpdate(productDto, product);
        verify(repository, times(1)).save(product);
        verify(mapper, times(1)).toDto(product);
    }

    @Test
    void updateProduct_ThrowsResourceNotFoundException_WhenNotFound() {
        UUID uuid = UUID.randomUUID();
        when(repository.findById(any())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.updateProduct(uuid, any()));

        assertEquals("Продукта с id: " + uuid + " не существует", exception.getMessage());
        verify(repository, times(1)).findById(uuid);
    }

    @Test
    void deleteProduct_ShouldDeleteProduct_WhenProductExists() {
        UUID uuid = UUID.randomUUID();
        when(repository.findById(any())).thenReturn(Optional.of(product));

        service.deleteProduct(uuid);

        verify(repository, times(1)).findById(uuid);
        verify(imageUtils, times(1)).deleteImageByKey(product.getImageKey());
        verify(repository, times(1)).deleteById(uuid);
    }

    @Test
    void deleteProduct_ThrowsResourceNotFoundException_WhenNotFound() {
        UUID uuid = UUID.randomUUID();
        when(repository.findById(any())).thenReturn(Optional.empty());

        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> service.deleteProduct(uuid));

        assertEquals("Продукта с id: " + uuid + " не существует", exception.getMessage());

        verify(repository, times(1)).findById(uuid);
    }
}