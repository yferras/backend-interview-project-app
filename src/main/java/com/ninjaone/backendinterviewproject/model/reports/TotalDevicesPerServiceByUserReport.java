package com.ninjaone.backendinterviewproject.model.reports;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * POJO for store the total devices per service by user.
 */
@Getter
@AllArgsConstructor
public class TotalDevicesPerServiceByUserReport {
    private final long userId;
    private final long totalDevices;

    private final String serviceName;

    private final double servicePrice;

}
