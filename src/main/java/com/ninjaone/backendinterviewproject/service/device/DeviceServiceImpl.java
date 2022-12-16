package com.ninjaone.backendinterviewproject.service.device;

import com.ninjaone.backendinterviewproject.common.AbstractService;
import com.ninjaone.backendinterviewproject.common.converter.IConverter;
import com.ninjaone.backendinterviewproject.common.validation.IGroupValidation;
import com.ninjaone.backendinterviewproject.common.validation.IValidation;
import com.ninjaone.backendinterviewproject.dto.DeviceDto;
import com.ninjaone.backendinterviewproject.model.Device;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class DeviceServiceImpl extends AbstractService<Long, DeviceDto, Device> implements IDeviceService {

    public DeviceServiceImpl(
            IConverter<Device, DeviceDto> converter,
            JpaRepository<Device, Long> repository
    ) {
        super(converter, repository);

        IGroupValidation<DeviceDto> fieldNameValGroup = IGroupValidation.of("name", true);

        fieldNameValGroup
                .addValidation(
                        IValidation.requireNonNull(
                                DeviceDto::getName,
                                "cannot be null.",
                                false
                        )
                )
                .addValidation(
                        IValidation.requireNonBlank(
                                DeviceDto::getName,
                                "cannot be an empty string.",
                                false
                        )
                );

        IGroupValidation<DeviceDto> typeFieldValGroup = IGroupValidation.of("deviceType", true);
        typeFieldValGroup
                .addValidation(
                        IValidation.requireNonNull(
                                DeviceDto::getDeviceType,
                                "cannot be null.",
                                false
                        )
                )
                .addValidation(
                        IValidation.of(
                                DeviceDto::getDeviceType,
                                deviceTypeDto -> {
                                    boolean idDef = deviceTypeDto.getId() != null;
                                    boolean nameDef = deviceTypeDto.getName() != null
                                            && !deviceTypeDto.getName().isBlank();
                                    return idDef == nameDef;
                                },
                                "only one of the fields: 'id' or 'name' is required.",
                                false

                        )
                );

        validationGroupsBeforeInsert.add(fieldNameValGroup);
        validationGroupsBeforeInsert.add(typeFieldValGroup);
    }

}
