package com.ninjaone.backendinterviewproject.service.device;

import com.ninjaone.backendinterviewproject.common.AbstractService;
import com.ninjaone.backendinterviewproject.common.converter.IConverter;
import com.ninjaone.backendinterviewproject.common.exception.BusinessException;
import com.ninjaone.backendinterviewproject.common.validation.DtoValidation;
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

        DtoValidation.Group<DeviceDto> fieldNameValGroup = new DtoValidation.Group<>("name");
        fieldNameValGroup.addValidation(
                new DtoValidation<>(
                        deviceDto -> deviceDto.getName() == null,
                        "cannot be null.",
                        false
                )
        );
        fieldNameValGroup.addValidation(
                new DtoValidation<>(
                        deviceDto -> deviceDto.getName().isBlank(),
                        "cannot be an empty string."
                )
        );

        DtoValidation.Group<DeviceDto> typeFieldValGroup = new DtoValidation.Group<>("deviceType");
        typeFieldValGroup.addValidation(
                new DtoValidation<>(
                        deviceDto -> deviceDto.getDeviceType() == null,
                        "cannot be null.",
                        false
                )
        );
        typeFieldValGroup.addValidation(
                new DtoValidation<>(
                        deviceDto -> {
                            boolean idDef = deviceDto.getDeviceType().getId() != null;
                            boolean nameDef = deviceDto.getDeviceType().getName() != null
                                    && !deviceDto.getDeviceType().getName().isBlank();
                            return idDef == nameDef;
                        },
                        "only one of the fields: 'id' or 'name' must be required."
                )
        );

        validationGroupsBeforeInsert.add(fieldNameValGroup);
        validationGroupsBeforeInsert.add(typeFieldValGroup);
    }

    @Override
    public DeviceDto addNew(DeviceDto dto) throws BusinessException {
        return super.addNew(dto);
    }
}
