package com.ninjaone.backendinterviewproject.service.reports;

import com.ninjaone.backendinterviewproject.dto.reports.TotalCostPerUser;

import java.io.Serializable;

public interface IReportService extends Serializable {

    /**
     * Gets the total cost by customer.
     *
     * @param userId the user's identifier.
     * @return the total amount.
     */
    TotalCostPerUser calcTotalCost(long userId);
}
