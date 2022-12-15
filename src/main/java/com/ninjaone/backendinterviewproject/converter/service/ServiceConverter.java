package com.ninjaone.backendinterviewproject.converter.service;

import com.ninjaone.backendinterviewproject.common.converter.AbstractConverter;
import com.ninjaone.backendinterviewproject.dto.ServiceDto;
import com.ninjaone.backendinterviewproject.model.Service;

/**
 * Final implementation to convert from the entity {@link Service} to DTO {@link ServiceDto} and vice-versa.
 */
public final class ServiceConverter extends AbstractConverter<Service, ServiceDto> {
    @Override
    public ServiceDto convertToDto(Service entity) {
        return ServiceDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .price(entity.getPrice())
                .build();
    }

    @Override
    public Service convertToEntity(ServiceDto dto) {
        Service service = new Service();
        service.setId(dto.getId());
        service.setName(dto.getName());
        service.setPrice(dto.getPrice());
        return service;
    }
}
