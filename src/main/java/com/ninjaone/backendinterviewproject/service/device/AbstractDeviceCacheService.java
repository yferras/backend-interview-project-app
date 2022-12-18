package com.ninjaone.backendinterviewproject.service.device;

import com.ninjaone.backendinterviewproject.database.device.DeviceRepository;
import com.ninjaone.backendinterviewproject.model.reports.IDeviceReport;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public abstract class AbstractDeviceCacheService<K extends Serializable>
        implements IDeviceCacheService<K, IDeviceReport> {

    private static final ConcurrentHashMap<String, ConcurrentHashMap<Serializable, IDeviceReport>> CACHES = new ConcurrentHashMap<>();

    @Getter(AccessLevel.PROTECTED)
    private final String cacheName;
    protected final DeviceRepository deviceRepository;

    protected AbstractDeviceCacheService(String cacheName, DeviceRepository deviceRepository) {
        this.cacheName = cacheName;
        this.deviceRepository = deviceRepository;
        CACHES.putIfAbsent(cacheName, new ConcurrentHashMap<>(0));
    }

    @Override
    public IDeviceReport get(K key) {
        IDeviceReport report = getCache().get(key);
        if (report == null) {
            report = delegateGet(key);
            getCache().put(key, report);
        } else {
            log.info("CACHE({})::GET. report: (ID = {}, NAME = '{}', COST = ${})",
                    getCacheName(),
                    report.getDeviceId(),
                    report.getDeviceName(),
                    report.getCurrentCost()
            );
        }
        return report;
    }

    protected abstract IDeviceReport delegateGet(K key);

    @Override
    public void put(K key, IDeviceReport value) {
        IDeviceReport report = getCache().put(key, value);
        if (report == null) {
            log.info("CACHE({})::INSERT. report: (ID = {}, NAME = '{}', COST = ${}).",
                    getCacheName(),
                    value.getDeviceId(),
                    value.getDeviceName(),
                    value.getCurrentCost()
            );
        } else {
            log.info("CACHE({})::UPDATE. report: (ID = {}, NAME = '{}', LAST_COST = ${}, NEW_COST = ${}).",
                    getCacheName(),
                    report.getDeviceId(),
                    report.getDeviceName(),
                    report.getCurrentCost(),
                    value.getCurrentCost()
            );
        }
    }

    @Override
    public void delete(K key) {
        IDeviceReport report = getCache().remove(key);
        if (report == null) {
            log.info("CACHE({})::DELETE. The given key: <{}>, is not present.",
                    getCacheName(),
                    key
            );
            return;
        }
        log.info("CACHE({})::DELETE. report: (ID = {}, NAME = '{}', COST = ${}), was removed.",
                getCacheName(),
                report.getDeviceId(),
                report.getDeviceName(),
                report.getCurrentCost()
        );
    }

    protected final Map<Serializable, IDeviceReport> getCache() {
        return CACHES.get(getCacheName());
    }
}
