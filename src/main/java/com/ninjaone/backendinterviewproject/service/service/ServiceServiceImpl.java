package com.ninjaone.backendinterviewproject.service.service;

import com.ninjaone.backendinterviewproject.common.AbstractService;
import com.ninjaone.backendinterviewproject.common.converter.IConverter;
import com.ninjaone.backendinterviewproject.common.validation.IGroupValidation;
import com.ninjaone.backendinterviewproject.common.validation.IValidation;
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

        IGroupValidation<ServiceDto> fieldNameValGroup = IGroupValidation.of("name", true);
        fieldNameValGroup
                .addValidation(
                        IValidation.requireNonNull(
                                ServiceDto::getName,
                                "cannot be null.",
                                false
                        )
                )
                .addValidation(
                        IValidation.requireNonBlank(
                                ServiceDto::getName,
                                "cannot be an empty or blank string.",
                                true
                        )
                );
        IGroupValidation<ServiceDto> priceFieldValGroup = IGroupValidation.of("price", true);
        priceFieldValGroup
                .addValidation(
                        IValidation.requireNonNull(
                                ServiceDto::getPrice,
                                "cannot be null.",
                                false
                        )
                )
                .addValidation(
                        IValidation.of(
                                ServiceDto::getPrice,
                                price -> price <  0.0,
                                "cannot be less than 0.0.",
                                false
                        )
                );

        validationGroupsBeforeInsert.add(fieldNameValGroup);
        validationGroupsBeforeInsert.add(priceFieldValGroup);
    }

}
