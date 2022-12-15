package com.ninjaone.backendinterviewproject.service.service;

import com.ninjaone.backendinterviewproject.common.AbstractService;
import com.ninjaone.backendinterviewproject.common.converter.IConverter;
import com.ninjaone.backendinterviewproject.common.validation.DtoValidation;
import com.ninjaone.backendinterviewproject.dto.ServiceDto;
import com.ninjaone.backendinterviewproject.model.Service;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;

@org.springframework.stereotype.Service
public class ServiceServiceImpl extends AbstractService<Long, ServiceDto, Service> implements IServiceService {

    public ServiceServiceImpl(
            @Qualifier("serviceConverter") IConverter<Service, ServiceDto> converter,
            JpaRepository<Service, Long> repository
    ) {
        super(converter, repository);

        DtoValidation.Group<ServiceDto> fieldNameValGroup = new DtoValidation.Group<>("name");
        fieldNameValGroup.addValidation(
                new DtoValidation<>(
                        serviceDto -> serviceDto.getName() == null,
                        "cannot be null.",
                        false
                )
        );
        fieldNameValGroup.addValidation(
                new DtoValidation<>(
                        serviceDto -> serviceDto.getName().isBlank(),
                        "cannot be an empty string."
                )
        );

        DtoValidation.Group<ServiceDto> priceFieldValGroup = new DtoValidation.Group<>("price");
        priceFieldValGroup.addValidation(
                new DtoValidation<>(
                        serviceDto -> serviceDto.getPrice() == null,
                        "cannot be null.",
                        false
                )
        );
        priceFieldValGroup.addValidation(
                new DtoValidation<>(
                        serviceDto -> serviceDto.getPrice() < 0.0,
                        "cannot be less than 0.0."
                )
        );

        validationGroupsBeforeInsert.add(fieldNameValGroup);
        validationGroupsBeforeInsert.add(priceFieldValGroup);
    }

}
