package com.aurora.paymentservice.services;

import com.aurora.paymentservice.domain.Customer;
import com.aurora.paymentservice.repositories.CustomerRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.Random;

@Component
@Slf4j
public class DataGenerator {

    @Autowired
    private CustomerRepository repository;

    @PostConstruct
    public void generateData(){
        Random r = new Random();
        for (int i = 0; i < 50; i++) {
            Customer customer = new Customer();
            customer.setName("name"+i);
            customer.setAmountAvailable(r.nextInt(1000));
            customer.setAmountReserved(0);
            log.info("Saving: "+customer);
            repository.save(customer);
        }
    }
}
