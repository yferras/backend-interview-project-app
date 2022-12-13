package com.ninjaone.backendinterviewproject.service.converter.service;

import com.ninjaone.backendinterviewproject.common.converter.AbstractConverterDecorator;
import com.ninjaone.backendinterviewproject.dto.ServiceDto;
import com.ninjaone.backendinterviewproject.model.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ServiceConverterIVADecorator extends AbstractConverterDecorator<Service, ServiceDto> {

    public static final String NAME = "iva-dec";

    @Value("${app.service.decorator-config.iva-dec.value:0}")
    private double iva;

    public ServiceConverterIVADecorator() {
        super(NAME);
    }

    @Override
    protected ServiceDto toDto(Service entity) {
        ServiceDto dto = getConverter().convertToDto(entity);
        double price = dto.getPrice();
        ServiceDto dtoIVA = ServiceDto.builder()
                .name(dto.getName())
                .id(dto.getId())
                .price(price * (1.0 + iva))
                .build();
        log.info("Service price plus IVA({}): {} -> {}", iva, price, dtoIVA.getPrice());
        return dtoIVA;
    }

    @Override
    protected Service toEntity(ServiceDto dto) {
        return getConverter().convertToEntity(dto);
    }
}
