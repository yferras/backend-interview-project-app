package com.ninjaone.backendinterviewproject.service.device;

import com.ninjaone.backendinterviewproject.database.device.DeviceRepository;
import com.ninjaone.backendinterviewproject.model.reports.IDeviceReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service("deviceOnDemandCacheService")
@Slf4j
public class DeviceOnDemandCacheServiceImpl extends AbstractDeviceCacheService<String> {

    public DeviceOnDemandCacheServiceImpl(DeviceRepository deviceRepository) {
        super("devices-on-demand", deviceRepository);
    }

    @Override
    protected IDeviceReport delegateGet(String key) {
        IDeviceReport report = deviceRepository.getDeviceCurrentCost(key);
        log.info("CACHE({})::GET. report: (ID = {}, NAME = '{}', COST = ${}). From database.",
                getCacheName(),
                report.getDeviceId(),
                report.getDeviceName(),
                report.getCurrentCost()
        );
        return report;
    }
}
