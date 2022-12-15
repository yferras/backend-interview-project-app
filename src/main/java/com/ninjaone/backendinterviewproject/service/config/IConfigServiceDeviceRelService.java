package com.ninjaone.backendinterviewproject.service.config;

import com.ninjaone.backendinterviewproject.common.exception.BusinessRuntimeException;
import com.ninjaone.backendinterviewproject.dto.ConfigServiceDeviceRelDto;

public interface IConfigServiceDeviceRelService {

    ConfigServiceDeviceRelDto createRelationship(ConfigServiceDeviceRelDto configServiceDevice) throws BusinessRuntimeException;
    ConfigServiceDeviceRelDto deleteRelationship(ConfigServiceDeviceRelDto configServiceDevice) throws BusinessRuntimeException;

}
