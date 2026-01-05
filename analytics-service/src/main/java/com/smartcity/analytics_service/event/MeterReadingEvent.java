package com.smartcity.analytics_service.event;

public record MeterReadingEvent(String id, double consumption) {
}
