package com.ninjaone.backendinterviewproject.service.reports.decorator;

import com.ninjaone.backendinterviewproject.dto.reports.TotalCostPerUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service("reportIvaDecorator")
@Slf4j
class IvaDecorator extends AbstractReportServiceDecorator {

    @Value("${app.params.iva}")
    private double iva;

    IvaDecorator() {
        super("iva-dec");
    }

    @Override
    public TotalCostPerUser calcTotalCost(long userId) {
        TotalCostPerUser totalCostPerUser = decorated.calcTotalCost(userId);
        double value = (1 + iva) * totalCostPerUser.getValue();
        log.info("Total cost for User(id = {}), after the IVA({}%): ${}",
                userId,
                iva * 100.0,
                Math.round(value * 100.0) / 100.0
        );
        return new TotalCostPerUser(value);
    }
}
