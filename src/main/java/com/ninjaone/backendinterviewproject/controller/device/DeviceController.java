package com.ninjaone.backendinterviewproject.controller.device;

import com.ninjaone.backendinterviewproject.common.exception.BusinessException;
import com.ninjaone.backendinterviewproject.dto.DeviceDto;
import com.ninjaone.backendinterviewproject.service.device.IDeviceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/devices")
@AllArgsConstructor
public class DeviceController {

    private IDeviceService deviceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeviceDto addNew(@RequestBody DeviceDto device) throws BusinessException {
        return deviceService.addNew(device);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@NonNull @PathVariable("id") Long id) throws BusinessException {
        deviceService.deleteById(id);
    }


}
