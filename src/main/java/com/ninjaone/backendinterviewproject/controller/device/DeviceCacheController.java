package com.ninjaone.backendinterviewproject.controller.device;

import com.ninjaone.backendinterviewproject.model.reports.IDeviceReport;
import com.ninjaone.backendinterviewproject.service.device.IDeviceCacheService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;

@RestController
@RequestMapping("/v1/devices/caches")
@AllArgsConstructor
public class DeviceCacheController {

    private final IDeviceCacheService<Long, IDeviceReport> devicePreloadCacheService;
    private final IDeviceCacheService<String, IDeviceReport> deviceOnDemandCacheService;

    @GetMapping("/{value}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public IDeviceReport getFromCache(@NonNull @PathVariable("value") Serializable value)  {
        String str = value.toString();
        try {
            Long id = Long.parseLong(str);
            return devicePreloadCacheService.get(id);
        } catch (NumberFormatException exception) {
            return deviceOnDemandCacheService.get(str);
        }
    }


}
