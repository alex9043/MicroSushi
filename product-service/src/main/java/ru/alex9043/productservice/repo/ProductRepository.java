package ru.alex9043.productservice.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.alex9043.productservice.model.Product;

import java.util.UUID;

public interface ProductRepository extends JpaRepository<Product, UUID> {
}