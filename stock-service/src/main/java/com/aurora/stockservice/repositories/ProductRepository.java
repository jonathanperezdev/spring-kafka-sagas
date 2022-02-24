package com.aurora.stockservice.repositories;

import com.aurora.stockservice.domain.Product;
import org.springframework.data.repository.CrudRepository;

public interface ProductRepository extends CrudRepository<Product, Long> {
}
