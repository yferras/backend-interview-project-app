package com.ninjaone.backendinterviewproject.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.ninjaone.backendinterviewproject.common.IBusinessDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * DTO to create the relationship between {@link com.ninjaone.backendinterviewproject.model.Device}
 * and {@link com.ninjaone.backendinterviewproject.model.Service}.
 */
@Getter
@AllArgsConstructor
@Builder
public class ConfigServiceDeviceRelDto implements IBusinessDto {

    /// --> Relative to Device
    private Serializable device;

    /// --> Relative to Service
    private Serializable service;

    /// --> Relationship status.
    @Setter
    private Boolean enabled;

    @JsonIgnore
    @Setter
    private Class<?> deviceType;
    @JsonIgnore
    @Setter
    private Class<?> serviceType;
}
