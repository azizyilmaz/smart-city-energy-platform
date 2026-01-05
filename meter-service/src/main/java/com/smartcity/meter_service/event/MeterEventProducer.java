package com.smartcity.meter_service.event;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class MeterEventProducer {

    private final StreamBridge streamBridge;

    public MeterEventProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publish(MeterReadingEvent meterReadingEvent) {
        boolean x = streamBridge.send("meter-out-0", meterReadingEvent);
        System.out.println(x);
    }
}
