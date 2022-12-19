package com.ninjaone.backendinterviewproject.service.device;

import com.ninjaone.backendinterviewproject.common.exception.NoDataException;
import com.ninjaone.backendinterviewproject.database.device.DeviceRepository;
import com.ninjaone.backendinterviewproject.model.reports.IDeviceReport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.List;


@Service("devicePreloadCacheService")
@Slf4j
public class DevicePreloadCacheServiceImpl extends AbstractDeviceCacheService<Long> {

    public DevicePreloadCacheServiceImpl(DeviceRepository deviceRepository) {
        super("devices-preloaded", deviceRepository);
    }

    @Override
    public void preload() {
        List<IDeviceReport> list = deviceRepository.getDevicesCurrentCost();
        log.info("Preloaded in cache: {}, {} reports.", getCacheName(), list.size());
        list.forEach(deviceRepository -> super.put(deviceRepository.getDeviceId(), deviceRepository));
    }

    @Override
    protected IDeviceReport delegateGet(Long key) throws NoDataException {
        IDeviceReport report = deviceRepository.getDeviceCurrentCost(key);
        if (report == null) {
            throw new NoDataException(MessageFormat.format("There is not entry with the key: {0}.", key));
        }
        log.info("GET. report: (ID = {}, NAME = '{}', COST = ${}). Insert into: {}.",
                report.getDeviceId(),
                report.getDeviceName(),
                report.getCurrentCost(),
                getCacheName()
        );
        return report;
    }
}
