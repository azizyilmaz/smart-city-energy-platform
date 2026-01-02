package com.smartcity.meter_service.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MeterController {

    @Value("${server.port}")
    private String port;

    @GetMapping("/meters/{id}")
    public String getMeter(@PathVariable String id) {
        return "Meter " + id + " from port " + port;
    }
}
