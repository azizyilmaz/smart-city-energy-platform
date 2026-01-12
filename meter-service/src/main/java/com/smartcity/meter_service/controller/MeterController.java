package com.smartcity.meter_service.controller;

import com.smartcity.meter_service.stream.MeterProducer;
import com.smartcity.meter_service.event.MeterReadingEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeterController {

    private final MeterProducer meterEventProducer;

    @Value("${server.port}")
    private String port;

    public MeterController(MeterProducer meterEventProducer) {
        this.meterEventProducer = meterEventProducer;
    }

    @GetMapping("/meters/{id}")
    public String getMeter(@PathVariable String id) {
        MeterReadingEvent event = new MeterReadingEvent(id, 42);
        meterEventProducer.publish(event);
        return "Meter " + id + " event published from port " + port;
    }
}
