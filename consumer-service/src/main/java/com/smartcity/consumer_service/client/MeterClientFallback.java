package com.smartcity.consumer_service.client;

import org.springframework.stereotype.Component;

@Component
public class MeterClientFallback implements MeterClient {
    @Override
    public String getMeter(String id) {
        return "Fallback: Meter data temporarily not available for meter " + id;
    }
}
