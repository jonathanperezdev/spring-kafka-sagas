package com.aurora.orderservice.controller;

import com.aurora.base.domain.Order;
import com.aurora.orderservice.service.OrderGeneratorService;
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
    private StreamsBuilderFactoryBean kafkaStreamsFactory;

    @Autowired
    private KafkaTemplate<Long, Order> template;

    @Autowired
    private OrderGeneratorService orderGeneratorService;

    private AtomicLong id = new AtomicLong();

    @PostMapping
    public Order create(@RequestBody Order order) {
        order.setId(id.incrementAndGet());
        template.send("orders", order.getId(), order);
        log.info("Sent: {}", order);

        return order;
    }

    @GetMapping
    public List<Order> all() {
        List<Order> orders = new ArrayList<>();

        ReadOnlyKeyValueStore<Long, Order> store = kafkaStreamsFactory
                .getKafkaStreams()
                .store(StoreQueryParameters.fromNameAndType(
                        "orders",
                        QueryableStoreTypes.keyValueStore()));
        KeyValueIterator<Long, Order> it = store.all();
        it.forEachRemaining(kv -> orders.add(kv.value));
        return orders;
    }

    @PostMapping("/generate")
    public boolean create() {
        orderGeneratorService.generate();
        return true;
    }
}
