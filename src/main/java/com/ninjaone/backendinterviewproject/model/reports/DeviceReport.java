package com.ninjaone.backendinterviewproject.model.reports;


import lombok.Data;

@Data
public class DeviceReport implements IDeviceReport {
    private Long deviceId;
    private String deviceName;
    private Double currentCost;
}
