package com.aurora.stockservice.services;

import com.aurora.stockservice.domain.Product;
import com.aurora.stockservice.repositories.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;

@Component
@Slf4j
public class DataGenerator {

    @Autowired
    private ProductRepository repository;

    @PostConstruct
    public void generateData(){
        Random r = new Random();
        for (int i = 0; i < 50; i++) {

            Product product = new Product();
            product.setName("Product "+i);
            product.setAvailableItems(r.nextInt(1000));
            product.setReservedItems(0);

            log.info("Saving: "+product);
            repository.save(product);
        }
    }
}
