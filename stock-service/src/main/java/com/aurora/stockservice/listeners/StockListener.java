package com.aurora.stockservice.listeners;

import com.aurora.base.domain.Order;
import com.aurora.stockservice.services.OrderManageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StockListener {

    @Autowired
    private OrderManageService orderManageService;

    @KafkaListener(id = "orders", topics = "orders", groupId = "stock")
    public void onEvent(Order o) {
        log.info("Received: {}" , o);
        if (o.getStatus().equals("NEW"))
            orderManageService.reserve(o);
        else
            orderManageService.confirm(o);
    }
}
