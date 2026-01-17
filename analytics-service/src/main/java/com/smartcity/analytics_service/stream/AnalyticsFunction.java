package com.smartcity.analytics_service.stream;

import com.smartcity.analytics_service.config.PricingConfig;
import com.smartcity.analytics_service.event.MeterReadingEvent;
import com.smartcity.analytics_service.event.PriceCalculatedEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class AnalyticsFunction {

    private final PricingConfig pricingConfig;

    public AnalyticsFunction(PricingConfig pricingConfig) {
        this.pricingConfig = pricingConfig;
    }

    @Bean
    public Function<MeterReadingEvent, PriceCalculatedEvent> price() {
        return event -> {
            double price = event.consumption() * pricingConfig.getBaseRate();
            System.out.println("Analytics received: " + event);
            System.out.println("Calculated price: " + price);
            return new PriceCalculatedEvent(event.id(), event.consumption(), price);
        };
    }
}
