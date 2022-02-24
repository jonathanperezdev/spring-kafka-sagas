package com.aurora.paymentservice.listeners;

import com.aurora.base.domain.Order;
import com.aurora.paymentservice.services.OrderManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class OrderListener {

    @Autowired
    private OrderManageService orderManageService;

    @KafkaListener(id = "orders", topics = "orders", groupId = "payment")
    public void onEvent(Order o) {
        log.info("Received: {}" , o);
        if (o.getStatus().equals("NEW"))
            orderManageService.reserve(o);
        else
            orderManageService.confirm(o);
    }
}
