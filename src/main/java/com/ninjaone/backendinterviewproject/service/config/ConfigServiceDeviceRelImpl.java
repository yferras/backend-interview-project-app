package com.ninjaone.backendinterviewproject.service.config;

import com.ninjaone.backendinterviewproject.common.exception.BusinessRuntimeException;
import com.ninjaone.backendinterviewproject.common.exception.IllegalDataCombinationException;
import com.ninjaone.backendinterviewproject.common.exception.NoDataException;
import com.ninjaone.backendinterviewproject.common.validation.DtoValidation;
import com.ninjaone.backendinterviewproject.database.device.DeviceRepository;
import com.ninjaone.backendinterviewproject.database.service.ServiceRepository;
import com.ninjaone.backendinterviewproject.dto.ConfigServiceDeviceRelDto;
import com.ninjaone.backendinterviewproject.model.Device;
import com.ninjaone.backendinterviewproject.model.Service;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.LongFunction;

@org.springframework.stereotype.Service
@Slf4j
public class ConfigServiceDeviceRelImpl implements IConfigServiceDeviceRelService {

    public static final String NAME_0_TEMPLATE = "name = \"{0}\"";
    public static final String ID_0_TEMPLATE = "id = {0,number,#}";

    public static final String INVALID_TYPE_VAL_MESSAGE = "Invalid type. Allowed: String or Long.";
    public static final String CANNOT_BE_AN_EMPTY_STRING_VAL_MESSAGE = "Cannot be an empty string.";
    private final DeviceRepository deviceRepository;

    private final ServiceRepository serviceRepository;

    private final List<DtoValidation.Group<ConfigServiceDeviceRelDto>> validationGroupsBeforeInsert = new ArrayList<>();

    public ConfigServiceDeviceRelImpl(DeviceRepository deviceRepository, ServiceRepository serviceRepository) {
        this.deviceRepository = deviceRepository;
        this.serviceRepository = serviceRepository;
        validationGroupsBeforeInsert.add(getValidationsForDeviceField());
        validationGroupsBeforeInsert.add(getValidationsForServiceField());
    }

    private static DtoValidation.Group<ConfigServiceDeviceRelDto> getValidationsForServiceField() {
        DtoValidation.Group<ConfigServiceDeviceRelDto> serviceGroup = new DtoValidation.Group<>("service");
        serviceGroup.addValidation(new DtoValidation<>(
                dto -> dto.getService() == null,
                "cannot be null.",
                false
        ));
        // Checks for valid type: String
        serviceGroup.addValidation(new DtoValidation<>(
                dto -> {
                    if (dto.getService() instanceof String) {
                        String device = (String) dto.getService();
                        dto.setServiceType(String.class);
                        return device.isBlank();
                    }
                    return false;
                },
                CANNOT_BE_AN_EMPTY_STRING_VAL_MESSAGE,
                false
        ));
        // Checks for valid type: Number
        serviceGroup.addValidation(new DtoValidation<>(
                dto -> {
                    if (dto.getServiceType() != null) {
                        return false;
                    }
                    if (dto.getService() instanceof Number) {
                        dto.setServiceType(Number.class);
                        return false;
                    }
                    return true;
                },
                INVALID_TYPE_VAL_MESSAGE
        ));

        return serviceGroup;
    }

    private static DtoValidation.Group<ConfigServiceDeviceRelDto> getValidationsForDeviceField() {
        DtoValidation.Group<ConfigServiceDeviceRelDto> deviceGroup = new DtoValidation.Group<>("device");
        deviceGroup.addValidation(new DtoValidation<>(
                dto -> dto.getDevice() == null,
                "cannot be null.",
                false
        ));

        // Checks for valid type: String
        deviceGroup.addValidation(new DtoValidation<>(
                dto -> {
                    if (dto.getDevice() instanceof String) {
                        String device = (String) dto.getDevice();
                        dto.setDeviceType(String.class);
                        return device.isBlank();
                    }
                    return false;
                },
                CANNOT_BE_AN_EMPTY_STRING_VAL_MESSAGE,
                false
        ));
        // Checks for valid type: Number
        deviceGroup.addValidation(new DtoValidation<>(
                dto -> {
                    if (dto.getDeviceType() != null) {
                        return false;
                    }
                    if (dto.getDevice() instanceof Number) {
                        dto.setDeviceType(Number.class);
                        return false;
                    }
                    return true;
                },
                INVALID_TYPE_VAL_MESSAGE
        ));

        return deviceGroup;
    }

    @Override
    @Transactional
    public ConfigServiceDeviceRelDto createRelationship(ConfigServiceDeviceRelDto configServiceDevice)
            throws BusinessRuntimeException {
        configRelationship(configServiceDevice, true);
        return configServiceDevice;
    }

    @Override
    @Transactional
    public ConfigServiceDeviceRelDto deleteRelationship(ConfigServiceDeviceRelDto configServiceDevice)
            throws BusinessRuntimeException {
        configRelationship(configServiceDevice, false);
        return configServiceDevice;
    }

    private void configRelationship(ConfigServiceDeviceRelDto configServiceDevice, boolean enable) {
        configServiceDevice.setEnabled(null);
        DtoValidation.checkGroups(configServiceDevice, validationGroupsBeforeInsert);
        log.info("Validation complete!!!");
        Result<Device> deviceResult = getDevice(configServiceDevice);
        Result<Service> serviceResult = getService(configServiceDevice);

        checkResults(deviceResult, serviceResult);

        Device device = deviceResult.getEntity();
        Service service = serviceResult.getEntity();

        if (enable) {
            create(device, service);
        } else {
            device.getServices().remove(service);
        }
        configServiceDevice.setEnabled(enable);
    }

    private static void create(Device device, Service service) {
        if (!service.getDeviceTypes().contains(device.getType())) {
            String message = MessageFormat.format(
                    "Service \"{0}\" cannot be set to device: \"{1}\", because the service is not applicable to devices of type: \"{2}\".",
                    service.getName(),
                    device.getName(),
                    device.getType().getName()
            );
            throw new IllegalDataCombinationException(message);
        }

        device.getServices().add(service);
    }

    private static void checkResults(Result<Device> deviceResult, Result<Service> serviceResult) throws NoDataException {
        StringBuilder exceptionMessageBuilder = new StringBuilder();
        if (deviceResult.getEntity() == null) {
            exceptionMessageBuilder
                    .append(MessageFormat.format(
                            NoDataException.NOT_FOUND_TEMPLATE,
                            "Device",
                            deviceResult.getQuery()
                    ));
        }
        if (serviceResult.getEntity() == null) {
            exceptionMessageBuilder
                    .append(" ")
                    .append(MessageFormat.format(
                            NoDataException.NOT_FOUND_TEMPLATE,
                            "Service",
                            serviceResult.getQuery()
                    ));
        }
        String exceptionMessage = exceptionMessageBuilder.toString().trim();
        if (!exceptionMessage.isBlank()) {
            throw new NoDataException(exceptionMessage);
        }
    }

    private Result<Service> getService(ConfigServiceDeviceRelDto configServiceDevice) {

        if (String.class.equals(configServiceDevice.getServiceType())) {
            String name = configServiceDevice.getService().toString();
            return getEntityByName(serviceRepository::findByName, name);
        }

        if (Number.class.equals(configServiceDevice.getServiceType())) {
            Long id = ((Number) configServiceDevice.getService()).longValue();
            return getEntityById(serviceRepository::findById, id);
        }

        return new Result<>("", null);
    }

    private Result<Device> getDevice(ConfigServiceDeviceRelDto configServiceDevice) {

        if (String.class.equals(configServiceDevice.getDeviceType())) {
            String name = configServiceDevice.getDevice().toString();
            return getEntityByName(deviceRepository::findByName, name);
        }

        if (Number.class.equals(configServiceDevice.getDeviceType())) {
            Long id = ((Number) configServiceDevice.getDevice()).longValue();
            return getEntityById(deviceRepository::findById, id);
        }

        return new Result<>("", null);
    }

    private static <T> Result<T> getEntityByName(Function<String, Optional<T>> findByNameFunction, String name) {
        Optional<T> result = findByNameFunction.apply(name);
        String query = MessageFormat.format(NAME_0_TEMPLATE, name);
        return new Result<>(query, result.orElse(null));
    }

    private static <T> Result<T> getEntityById(LongFunction<Optional<T>> findByIdFunction, Long id) {
        Optional<T> result = findByIdFunction.apply(id);
        String query = MessageFormat.format(ID_0_TEMPLATE, id);
        return new Result<>(query, result.orElse(null));
    }

    @AllArgsConstructor
    @Getter
    private static class Result<T> {
        private final String query;

        private final T entity;

    }
}
