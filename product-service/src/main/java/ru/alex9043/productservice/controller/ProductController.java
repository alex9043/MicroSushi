package ru.alex9043.productservice.controller;

import lombok.RequiredArgsConstructor;
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
public class ProductController {
    private final ProductService productService;

    @GetMapping
    public ResponseEntity<List<ResponseProductDto>> getAllProducts() {
        return new ResponseEntity<>(productService.getAllProducts(), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ResponseProductDto> getProduct(@PathVariable("id") UUID id) {
        return new ResponseEntity<>(productService.getProduct(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseProductDto> createProduct(@Validated @RequestBody CreateProductDto createProductDto) {
        return new ResponseEntity<>(productService.createProduct(createProductDto), HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<ResponseProductDto> updateProduct(@PathVariable("id") UUID id,
                                                            @Validated @RequestBody UpdateProductDto updateProductDto) {
        return new ResponseEntity<>(productService.updateProduct(id, updateProductDto), HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") UUID id) {
        productService.deleteProduct(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
