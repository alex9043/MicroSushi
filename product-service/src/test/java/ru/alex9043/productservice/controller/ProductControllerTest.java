package ru.alex9043.productservice.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.alex9043.productservice.dto.ResponseProductDto;
import ru.alex9043.productservice.error.GlobalExceptionHandler;
import ru.alex9043.productservice.service.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = ProductController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = GlobalExceptionHandler.class
        )
)
class ProductControllerTest {

    ResponseProductDto dto;
    @Autowired
    private MockMvc mockMvc;
    @MockitoBean
    private ProductService productService;

    @BeforeEach
    void initEach() {
        dto = new ResponseProductDto(UUID.randomUUID(), "test", BigDecimal.ONE);
    }

    @Test
    void getAllProducts_ReturnsProducts_WhenFound() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of(dto, dto, dto));

        mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].id").value(dto.id().toString()))
                .andExpect(jsonPath("$[0].name").value(dto.name()))
                .andExpect(jsonPath("$[0].price").value(dto.price()))
                .andExpect(jsonPath("$[1].id").value(dto.id().toString()))
                .andExpect(jsonPath("$[1].name").value(dto.name()))
                .andExpect(jsonPath("$[1].price").value(dto.price()))
                .andExpect(jsonPath("$[2].id").value(dto.id().toString()))
                .andExpect(jsonPath("$[2].name").value(dto.name()))
                .andExpect(jsonPath("$[2].price").value(dto.price()));
    }

    @Test
    void getProduct_ReturnsProduct_WhenFound() throws Exception {
        when(productService.getProduct(any())).thenReturn(dto);


        mockMvc.perform(get("/products/" + dto.id()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(dto.id().toString()))
                .andExpect(jsonPath("$.name").value(dto.name()))
                .andExpect(jsonPath("$.price").value(dto.price()));

        verify(productService, times(1)).getProduct(dto.id());
    }

    @Test
    void createProduct_ReturnsProduct_WhenCreated() throws Exception {
        when(productService.createProduct(any())).thenReturn(dto);

        mockMvc.perform(post("/products")
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"id\":\"" + dto.id() + "\",\"name\":\"" + dto.name() + "\",\"price\":" + dto.price() + "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(dto.id().toString()))
                .andExpect(jsonPath("$.name").value(dto.name()))
                .andExpect(jsonPath("$.price").value(dto.price()));

        verify(productService, times(1)).createProduct(any());
    }

    @Test
    void updateProduct_ReturnsProduct_WhenUpdated() throws Exception {
        when(productService.updateProduct(any(), any())).thenReturn(dto);

        mockMvc.perform(put("/products/" + dto.id())
                        .accept(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"" + dto.name() + "\",\"price\":" + dto.price() + "}")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(dto.id().toString()))
                .andExpect(jsonPath("$.name").value(dto.name()))
                .andExpect(jsonPath("$.price").value(dto.price()));

        verify(productService, times(1)).updateProduct(eq(dto.id()), any());
    }

    @Test
    void deleteProduct_ReturnsNoContent_WhenDeleted() throws Exception {
        mockMvc.perform(delete("/products/" + dto.id()))
                .andExpect(status().isNoContent());
        verify(productService, times(1)).deleteProduct(dto.id());
    }
}