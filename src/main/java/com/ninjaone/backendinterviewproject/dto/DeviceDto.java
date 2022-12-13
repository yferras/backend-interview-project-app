package com.ninjaone.backendinterviewproject.dto;

import com.ninjaone.backendinterviewproject.common.IBusinessDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class DeviceDto implements IBusinessDto {

    private Long id;

    private String name;

    private DeviceTypeDto deviceType;

    private Long customerId;

}
