package com.ninjaone.backendinterviewproject.service.device;

import com.ninjaone.backendinterviewproject.common.exception.NoDataException;
import com.ninjaone.backendinterviewproject.database.device.DeviceRepository;
import com.ninjaone.backendinterviewproject.model.reports.IDeviceReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;

@Service("deviceOnDemandCacheService")
@Slf4j
public class DeviceOnDemandCacheServiceImpl extends AbstractDeviceCacheService<String> {

    public DeviceOnDemandCacheServiceImpl(DeviceRepository deviceRepository) {
        super("devices-on-demand", deviceRepository);
    }

    @Override
    protected IDeviceReport delegateGet(String key) throws NoDataException {
        IDeviceReport report = deviceRepository.getDeviceCurrentCost(key);
        if (report == null) {
            throw new NoDataException(MessageFormat.format("There is not entry with the key: \"{0}\".", key));
        }
        log.info("CACHE({})::GET. report: (ID = {}, NAME = '{}', COST = ${}). From database.",
                getCacheName(),
                report.getDeviceId(),
                report.getDeviceName(),
                report.getCurrentCost()
        );
        return report;
    }
}
