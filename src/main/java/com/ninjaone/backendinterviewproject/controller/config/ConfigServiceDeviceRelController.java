package com.ninjaone.backendinterviewproject.controller.config;


import com.ninjaone.backendinterviewproject.dto.ConfigServiceDeviceRelDto;
import com.ninjaone.backendinterviewproject.service.config.IConfigServiceDeviceRelService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/config")
@AllArgsConstructor
public class ConfigServiceDeviceRelController {

    private final IConfigServiceDeviceRelService iConfigServiceDeviceRelService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ConfigServiceDeviceRelDto createRelationship(@RequestBody ConfigServiceDeviceRelDto configServiceDevice) {
        return iConfigServiceDeviceRelService.createRelationship(configServiceDevice);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public ConfigServiceDeviceRelDto deleteRelationship(@RequestBody ConfigServiceDeviceRelDto configServiceDevice) {
        return iConfigServiceDeviceRelService.deleteRelationship(configServiceDevice);
    }
}
