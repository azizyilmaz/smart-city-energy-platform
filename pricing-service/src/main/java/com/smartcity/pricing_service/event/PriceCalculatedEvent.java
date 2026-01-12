package com.smartcity.pricing_service.event;

public record PriceCalculatedEvent(String meterId, double consumption, double price) {
}