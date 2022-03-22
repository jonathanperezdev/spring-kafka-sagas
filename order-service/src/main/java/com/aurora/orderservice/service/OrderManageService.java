package com.aurora.orderservice.service;

import com.aurora.base.domain.Order;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.state.KeyValueIterator;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.config.StreamsBuilderFactoryBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
@Slf4j
public class OrderManageService {

    @Autowired
    private StreamsBuilderFactoryBean kafkaStreamsFactory;

    @Autowired
    private KafkaTemplate<Long, Order> template;

    private AtomicLong id = new AtomicLong();
    private static final String SOURCE = "order";

    public Order confirm(Order orderPayment, Order orderStock) {
        Order order = new Order();
        order.setId(orderPayment.getId());
        order.setCustomerId(orderPayment.getCustomerId());
        order.setProductId(orderPayment.getProductId());
        order.setProductCount(orderPayment.getProductCount());
        order.setPrice(orderPayment.getPrice());
        order.setSource(orderPayment.getSource());

        if (orderPayment.getStatus().equals("ACCEPT") &&
                orderStock.getStatus().equals("ACCEPT")) {
            order.setStatus("CONFIRMED");
        } else if (orderPayment.getStatus().equals("REJECT") &&
                orderStock.getStatus().equals("REJECT")) {
            order.setStatus("REJECTED");
        } else if (orderPayment.getStatus().equals("REJECT") ||
                orderStock.getStatus().equals("REJECT")) {
            String source = orderPayment.getStatus().equals("REJECT")
                    ? "PAYMENT" : "STOCK";
            order.setStatus("ROLLBACK");
            order.setSource(source);
        }

        return order;
    }



    public Order createOrder(Order order){
        order.setId(id.incrementAndGet());
        order.setSource(SOURCE);
        template.send("orders", order.getId(), order);
        log.info("Sent: {}", order);

        return order;
    }

    public List<Order> getAllOrders(){
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
}
