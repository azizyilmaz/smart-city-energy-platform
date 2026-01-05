package com.smartcity.meter_service.event;

public record MeterReadingEvent(String id, double consumption) {
}
