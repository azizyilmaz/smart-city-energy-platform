package com.smartcity.consumer_service.controller;

import com.smartcity.consumer_service.client.MeterClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeterController {

    private final MeterClient meterClient;

    public MeterController(MeterClient meterClient) {
        this.meterClient = meterClient;
    }

    @GetMapping("/consume/{id}")
    public String consume(@PathVariable String id) {
        return meterClient.getMeter(id);
    }
}
