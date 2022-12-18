package com.ninjaone.backendinterviewproject.model.reports;


import java.io.Serializable;

public interface IDeviceReport  extends Serializable {
    Long getDeviceId();

    void setDeviceId(Long deviceId);

    String getDeviceName();

    void setDeviceName(String deviceName);

    Double getCurrentCost();

    void setCurrentCost(Double cost);
}
