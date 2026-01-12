package com.smartcity.analytics_service.event;

public record PriceCalculatedEvent(String meterId, double consumption, double price) {
}
