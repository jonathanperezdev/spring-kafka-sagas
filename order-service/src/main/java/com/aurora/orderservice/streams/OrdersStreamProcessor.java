package com.aurora.orderservice.streams;

import com.aurora.base.domain.Order;
import com.aurora.orderservice.service.OrderManageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.JoinWindows;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.StreamJoined;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;

@Configuration
@Slf4j
public class OrdersStreamProcessor {

    @Autowired
    private OrderManageService orderManageService;

    @Bean
    public KStream<Long, Order> stream(StreamsBuilder builder) {
        JsonSerde<Order> orderSerde = new JsonSerde<>(Order.class);
        KStream<Long, Order> stream = builder
                .stream("payment-orders", Consumed.with(Serdes.Long(), orderSerde));

        stream.join(
                builder.stream("stock-orders"),
                orderManageService::confirm,
                JoinWindows.of(Duration.ofSeconds(10)),
                StreamJoined.with(Serdes.Long(), orderSerde, orderSerde))
                .peek((k, o) -> log.info("Output: {}", o))
                .to("orders");

        return stream;
    }
}
