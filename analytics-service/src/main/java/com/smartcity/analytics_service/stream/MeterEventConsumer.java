package com.smartcity.analytics_service.stream;

import com.smartcity.analytics_service.event.MeterReadingEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Consumer;

@Configuration
public class MeterEventConsumer {

    @Bean
    public Consumer<MeterReadingEvent> meter() {
        return meterReadingEvent -> {
            System.out.println("Analytics received MeterReadingEvent" + meterReadingEvent);
        };
    }
}
