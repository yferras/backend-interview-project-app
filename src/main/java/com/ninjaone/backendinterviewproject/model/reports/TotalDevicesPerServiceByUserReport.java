package com.ninjaone.backendinterviewproject.model.reports;


/**
 * POJO for store the total devices per service by user.
 */
public interface TotalDevicesPerServiceByUserReport {
    long getUserId();
    long getTotalDevices();

    String getServiceName();

    double getServicePrice();

}
