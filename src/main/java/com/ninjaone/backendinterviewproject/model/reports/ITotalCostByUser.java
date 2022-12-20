package com.ninjaone.backendinterviewproject.model.reports;


/**
 * Total cost by user.
 */
public interface ITotalCostByUser {
    long getUserId();
    long getTotalDevices();

    String getServiceName();

    double getServicePrice();

}
