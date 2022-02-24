package com.aurora.orderservice.service;

import com.aurora.base.domain.Order;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderGeneratorService {

    private static Random RAND = new Random();
    private AtomicLong id = new AtomicLong();


    private KafkaTemplate<Long, Order> template;

    @Async("kafkaSenderExecutor")
    public void generate() {
        for (int i = 0; i < 100; i++) {
            int x = RAND.nextInt(5) + 1;

            Order order = new Order();
            order.setId(id.incrementAndGet());
            order.setCustomerId(RAND.nextLong() + 1);
            order.setProductId(RAND.nextLong() + 1);
            order.setStatus("NEW");
            order.setPrice(100 * x);
            order.setProductCount(x);

            template.send("orders", order.getId(), order);
        }
    }
}
