package com.smartcity.consumer_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "meter-service")
public interface MeterClient {

    @GetMapping("/meters/{id}")
    String getMeter(@PathVariable String id);
}
