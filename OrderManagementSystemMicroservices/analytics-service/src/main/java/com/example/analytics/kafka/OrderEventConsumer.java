package com.example.analytics.kafka;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class OrderEventConsumer {

    private static final Logger log = LoggerFactory.getLogger(OrderEventConsumer.class);

    @KafkaListener(topics = "${kafka.topic.order-events}", groupId = "${spring.kafka.consumer.group-id}")
    public void onOrderEvent(String message) {
        log.info("[ANALYTICS] Received event. Updating business dashboards (simulating heavy calculation)...");
        try {
            // Artificial delay for demo
            Thread.sleep(5000); 
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        log.info("[ANALYTICS] Dashboard updated for: {}", message);
    }
}
