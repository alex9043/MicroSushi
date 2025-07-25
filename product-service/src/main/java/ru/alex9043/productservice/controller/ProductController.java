package ru.alex9043.productservice.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.alex9043.productservice.dto.CreateProductDto;
import ru.alex9043.productservice.dto.ResponseProductDto;
import ru.alex9043.productservice.dto.UpdateProductDto;
import ru.alex9043.productservice.service.ProductService;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
@Slf4j
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ResponseProductDto>> getAllProducts() {
        log.info("Запрос на получение всех продуктов");
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseProductDto> getProduct(@PathVariable("id") UUID id) {
        log.info("Запрос на получение продукта с id - {}", id);
        return new ResponseEntity<>(productService.getProduct(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseProductDto> createProduct(@Validated @RequestBody CreateProductDto createProductDto) {
        log.info("Запрос на создание продукта");
        return new ResponseEntity<>(productService.createProduct(createProductDto), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseProductDto> updateProduct(@PathVariable("id") UUID id,
                                                            @Validated @RequestBody UpdateProductDto updateProductDto) {
        log.info("Запрос на обновление продукта с id - {}", id);
        return new ResponseEntity<>(productService.updateProduct(id, updateProductDto), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") UUID id) {
        log.info("Запрос на удаление продукта с id - {}", id);
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
