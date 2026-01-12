package com.smartcity.meter_service.controller;

import com.smartcity.meter_service.config.PricingConfig;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PricingController {

    private final PricingConfig pricingConfig;

    public PricingController(PricingConfig pricingConfig) {
        this.pricingConfig = pricingConfig;
    }

    @GetMapping("/pricing/base-rate")
    public String getBaseRate() {
        double baseRate = pricingConfig.getBaseRate();
        System.out.println("Current baseRate: " + baseRate);
        return String.format("%s %.2f", "Current baseRate is", baseRate);
    }
}
