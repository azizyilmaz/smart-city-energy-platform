package com.smartcity.analytics_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@RefreshScope
@Component
public class PricingConfig {

    @Value("${pricing.base-rate}")
    private double baseRate;

    public double getBaseRate() {
        return baseRate;
    }
}
