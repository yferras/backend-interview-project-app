package com.ninjaone.backendinterviewproject.service.config;

import com.ninjaone.backendinterviewproject.dto.ConfigServiceDeviceRelDto;

public interface IConfigServiceDeviceRelService {

    ConfigServiceDeviceRelDto createRelationship(ConfigServiceDeviceRelDto configServiceDevice);
    ConfigServiceDeviceRelDto deleteRelationship(ConfigServiceDeviceRelDto configServiceDevice);

}
