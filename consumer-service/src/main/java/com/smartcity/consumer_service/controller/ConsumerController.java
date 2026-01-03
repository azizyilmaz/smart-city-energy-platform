package com.smartcity.consumer_service.controller;

import com.smartcity.consumer_service.client.MeterClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ConsumerController {

    private final MeterClient meterClient;

    public ConsumerController(MeterClient meterClient) {
        this.meterClient = meterClient;
    }

    @GetMapping("/consume/{id}")
    @CircuitBreaker(name = "meterServiceCB", fallbackMethod = "fallback")
    public String consume(@PathVariable String id) {
        return meterClient.getMeter(id);
    }

    public String fallback(String id, Throwable ex) {
        return "CircuitBreaker fallback for meterServiceCB " + id;
    }
}
