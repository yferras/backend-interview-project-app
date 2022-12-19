package com.ninjaone.backendinterviewproject;

import com.ninjaone.backendinterviewproject.model.reports.IDeviceReport;
import com.ninjaone.backendinterviewproject.service.device.IDeviceCacheService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class CacheConfig {


    @Bean
    public CommandLineRunner loadData(
            List<IDeviceCacheService<Long, IDeviceReport>> cacheServiceList) {
        return args -> cacheServiceList.forEach(IDeviceCacheService::preload);
    }


}
