package com.smartcity.pricing_service.stream;

import com.smartcity.pricing_service.event.PriceCalculatedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class PricingConsumer {

    @Bean
    public Consumer<PriceCalculatedEvent> analytics() {
        return event -> {
            System.out.println("Pricing received:");
            System.out.println("MeterId: " + event.meterId());
            System.out.println("Consumption: " + event.consumption());
            System.out.println("Price: " + event.price());
        };
    }
}
