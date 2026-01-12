package com.smartcity.meter_service.stream;

import com.smartcity.meter_service.event.MeterReadingEvent;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Component;

@Component
public class MeterProducer {

    private final StreamBridge streamBridge;

    public MeterProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publish(MeterReadingEvent meterReadingEvent) {
        boolean x = streamBridge.send("meter-out-0", meterReadingEvent);
        System.out.println(x);
    }
}
