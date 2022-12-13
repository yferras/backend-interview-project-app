package com.ninjaone.backendinterviewproject.service.converter.device;

import com.ninjaone.backendinterviewproject.common.converter.AbstractConverter;
import com.ninjaone.backendinterviewproject.common.exception.NoDataException;
import com.ninjaone.backendinterviewproject.database.DeviceTypeRepository;
import com.ninjaone.backendinterviewproject.dto.DeviceDto;
import com.ninjaone.backendinterviewproject.dto.DeviceTypeDto;
import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.DeviceType;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Final implementation to convert from the entity {@link Device} to DTO {@link DeviceDto} and vice-versa.
 */
@Component("deviceConverter")
@AllArgsConstructor
public final class DeviceConverter extends AbstractConverter<Device, DeviceDto> {

    private final DeviceTypeRepository deviceTypeRepository;

    @Override
    public DeviceDto convertToDto(Device entity) {

        return DeviceDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .customerId(entity.getUserId())
                .deviceType(DeviceTypeDto.builder()
                        .id(entity.getType().getId())
                        .name(entity.getType().getName())
                        .build())
                .build();
    }

    @Override
    public Device convertToEntity(DeviceDto dto) {
        Optional<DeviceType> type;
        // Here is guaranteed that name or id are not null
        // thanks to the validations.
        String name = dto.getDeviceType().getName();
        String field;
        Object value;
        if (name != null) {
            field = "name";
            value = MessageFormat.format("\"{0}\"", name);
            type = deviceTypeRepository.findByNameIgnoreCase(name);
        } else {
            field = "id";
            Long id = dto.getDeviceType().getId();
            value = id;
            type = deviceTypeRepository.findById(id);
        }

        if (type.isEmpty()) {
            String deviceTypes = deviceTypeRepository.findAll().stream()
                    .map(dt -> MessageFormat.format(
                            "DeviceType(id = {0,number,#}, name = \"{1}\")",
                            dt.getId(),
                            dt.getName())
                    )
                    .collect(Collectors.joining(" or "));
            String stringBuilder = MessageFormat.format("E::DeviceType({0} = {1}) Not found.", field, value) +
                    " Valid combinations are: " +
                    deviceTypes;
            throw new NoDataException(stringBuilder, null);
        }

        Device device = new Device();
        device.setId(dto.getId());
        device.setName(dto.getName());
        device.setUserId(dto.getCustomerId() == null ? 0 : dto.getCustomerId());
        device.setType(type.get());
        return device;
    }
}
