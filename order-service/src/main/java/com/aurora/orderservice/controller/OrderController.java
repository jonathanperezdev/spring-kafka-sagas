package com.aurora.orderservice.controller;

import com.aurora.base.domain.Order;
import com.aurora.orderservice.service.OrderGeneratorService;
import com.aurora.orderservice.service.OrderManageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/orders")
@Slf4j
public class OrderController {

    @Autowired
    private OrderGeneratorService orderGeneratorService;

    @Autowired
    private OrderManageService orderManageService;



    @PostMapping
    public Order create(@RequestBody Order order) {
        return orderManageService.createOrder(order);
    }

    @GetMapping
    public List<Order> all() {
        return orderManageService.getAllOrders();
    }

    @PostMapping("/generate")
    public boolean create() {
        orderGeneratorService.generate();
        return true;
    }
}
