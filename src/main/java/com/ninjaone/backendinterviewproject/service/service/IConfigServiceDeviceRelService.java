package com.ninjaone.backendinterviewproject.service.service;

import com.ninjaone.backendinterviewproject.dto.ConfigServiceDeviceRelDto;

public interface IConfigServiceDeviceRelService {

    ConfigServiceDeviceRelDto createRelationship(ConfigServiceDeviceRelDto configServiceDevice);
    ConfigServiceDeviceRelDto deleteRelationship(ConfigServiceDeviceRelDto configServiceDevice);

}
