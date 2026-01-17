package com.smartcity.analytics_service.stream;

import com.smartcity.analytics_service.event.PriceCalculatedEvent;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
@RefreshScope
public class PricingFunction {

    private static final double TAX_RATE = 0.18;
    private static final double DISCOUNT_RATE = 0.05;

    @Bean
    public Function<PriceCalculatedEvent, PriceCalculatedEvent> tax() {
        return event -> {
            double taxed = event.price() * (1 + TAX_RATE);
            System.out.println("taxed: " + taxed);
            return new PriceCalculatedEvent(event.meterId(), event.consumption(), taxed);
        };
    }

    @Bean
    public Function<PriceCalculatedEvent, PriceCalculatedEvent> discount() {
        return event -> {
            double finalPrice = event.price() * (1 - DISCOUNT_RATE);
            System.out.println("finalPrice: " + finalPrice);
            return new PriceCalculatedEvent(event.meterId(), event.consumption(), finalPrice);
        };
    }

}
