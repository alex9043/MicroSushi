package ru.alex9043.productservice.mapper;

import org.mapstruct.*;
import ru.alex9043.productservice.dto.CreateProductDto;
import ru.alex9043.productservice.dto.ResponseProductDto;
import ru.alex9043.productservice.dto.UpdateProductDto;
import ru.alex9043.productservice.model.Product;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProductMapper {
    Product toEntity(CreateProductDto responseProductDto);

    ResponseProductDto toDto(Product product);

    List<ResponseProductDto> toDtoList(List<Product> products);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Product partialUpdate(UpdateProductDto updateProductDto, @MappingTarget Product product);
}