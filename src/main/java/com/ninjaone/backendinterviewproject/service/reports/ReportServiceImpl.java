package com.ninjaone.backendinterviewproject.service.reports;

import com.ninjaone.backendinterviewproject.common.exception.NoDataException;
import com.ninjaone.backendinterviewproject.database.device.DeviceRepository;
import com.ninjaone.backendinterviewproject.database.service.ServiceRepository;
import com.ninjaone.backendinterviewproject.dto.reports.TotalCostPerUser;
import com.ninjaone.backendinterviewproject.model.Service;
import com.ninjaone.backendinterviewproject.model.reports.ITotalCostByUser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.text.MessageFormat;
import java.util.List;
import java.util.Set;

@org.springframework.stereotype.Service("reportDefaultService")
@AllArgsConstructor
@Slf4j
public class ReportServiceImpl implements IReportService {

    private final transient DeviceRepository deviceRepository;
    private final transient ServiceRepository serviceRepository;

    @Override
    public TotalCostPerUser calcTotalCost(long userId) {

        if (!deviceRepository.existsByUserId(userId)) {
            String criteria = "userId = ".concat(String.valueOf(userId));
            throw new NoDataException(
                    MessageFormat.format(
                            NoDataException.NOT_FOUND_TEMPLATE,
                            "Device", criteria
                    ));
        }

        long totalDevicesPerUser = deviceRepository.countByUserId(userId);
        if (totalDevicesPerUser == 0) {
            return new TotalCostPerUser(0.0);
        }
        List<ITotalCostByUser> report =
                deviceRepository.getTotalDevicesPerServiceByUserId(userId);

        double totalCost = report.stream()
                .mapToDouble(r -> r.getServicePrice() * r.getTotalDevices())
                .sum();

        Set<Service> defaultServices = serviceRepository.findByApplyToAll(true);
        totalCost += defaultServices.stream()
                .mapToDouble(defaultService -> defaultService.getPrice() * totalDevicesPerUser)
                .sum();
        
        log.info("Total cost for User(id = {}): ${}",
                userId,
                totalCost
        );
        return new TotalCostPerUser(totalCost);
    }
}
