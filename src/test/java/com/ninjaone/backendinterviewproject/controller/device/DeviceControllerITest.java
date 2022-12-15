package com.ninjaone.backendinterviewproject.controller.device;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.backendinterviewproject.controller.AbstractEntityControllerITest;
import com.ninjaone.backendinterviewproject.dto.DeviceDto;
import com.ninjaone.backendinterviewproject.dto.DeviceTypeDto;
import com.ninjaone.backendinterviewproject.service.device.IDeviceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.text.MessageFormat;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class DeviceControllerITest extends AbstractEntityControllerITest<Long, DeviceDto> {

    @Autowired
    public DeviceControllerITest(
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            IDeviceService service
    ) {
        super(mockMvc, objectMapper, service);
    }

    @ParameterizedTest(name = "(NAME=\"{0}\",TYPE.NAME=\"{1}\",TYPE.ID={2}) -> {3} \"{4}\"")
    @DisplayName("Add new device.")
    @CsvSource(value = {
            // NAME{0}, TYPE.NAME{1}, TYPE.ID{2}, HTTP_STATUS_CODE{3}, MESSAGE{4}
            // OK
            "'Computer-1','linux',,201,'CREATED'",  // valid data -> 201 Created.
            "'Computer-2',,1,201,'CREATED'",        // valid data -> 201 Created. 1 -> linux.
            // Data duplication
            "'Computer-1',,2,409,'Data duplication [Device].'", // 409 Conflict, The device 'Computer-1' already exists.
            // Data validation
            ",,1,422,'name'",  // name is null, validation error.
            "'',,1,422,'name'",  // name is empty, validation error.
            "'     ',,1,422,'name'",  // name is blank, validation error.
            "'Computer-3',,,422,'deviceType'",  // name is ok but: type will be null because: type.name and type.id are null.
            "'Computer-4','linux',2,422,'deviceType'",  // type.name and type.id are not allowed at the same time.
            // Non-existent dependencies.
            "'Computer-5','invalid',,404,'NOT FOUND'",  // type.name = 'invalid', doesn't exist in database.
            "'Computer-6',,-100,404,'NOT FOUND'",  // type.id = -100, doesn't exist in database.
    })
    void addNew(
            String name,
            String typeName,
            Long typeId,
            int httpStatusCode,
            String message
    ) throws Exception {

        DeviceTypeDto deviceType;
        if (typeName == null && typeId == null) {
            deviceType = null;
        } else {
            deviceType = DeviceTypeDto.builder()
                    .name(typeName)
                    .id(typeId)
                    .build();
        }

        DeviceDto dto = DeviceDto.builder()
                .name(name)
                .deviceType(deviceType)
                .build();


        ResultActions resultActions = mockMvc.perform(
                post("/v1/device")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto))
        );

        switch (httpStatusCode) {
            case 409:  // Conflicts with the database
                checkIsConflict(message, resultActions);
                break;
            case 422:  // Validation
                checkUnprocessableEntity(message, resultActions);
                break;
            case 404:  // Not Found
                MessageNotFoundDescriptor descriptor = checkNotFound(resultActions);
                assertEquals("DeviceType", descriptor.getEntityName());
                assertEquals(typeId != null ? "id" : "name", descriptor.getFieldName());
                if (typeId != null) {
                    assertEquals(typeId, Long.parseLong(descriptor.getValue()));
                } else {
                    assertEquals(MessageFormat.format("\"{0}\"", typeName), descriptor.getValue());
                }
                break;
            default:
                // CREATED
                resultActions
                        .andExpect(status().isCreated())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id").isNumber())
                        .andExpect(jsonPath("$.name").value(name))
                        .andExpect(jsonPath("$.deviceType").exists())
                        .andExpect(jsonPath("$.deviceType.id").exists())
                        .andExpect(jsonPath("$.deviceType.name").exists())
                ;
                break;
        }
    }

    @Test
    @DisplayName("Delete device by ID when the ID exists.")
    void deleteByIdOk() throws Exception {

        DeviceDto device = DeviceDto.builder()
                .name("device-to-delete")
                .deviceType(DeviceTypeDto.builder().name("linux").build())
                .build();
        device = service.addNew(device);


        mockMvc.perform(
                        delete("/v1/device/{id}", device.getId())
                )
                .andExpect(status().isOk());

        Optional<DeviceDto> optional = service.getById(device.getId());

        assertTrue(optional.isEmpty());


    }

    @Test
    @DisplayName("Delete device by ID when the ID doesn't exists.")
    void deleteByIdNotFound() throws Exception {
        final Long id = -1000L;
        ResultActions resultActions = mockMvc.perform(
                        delete("/v1/device/{id}", id)
                );
        MessageNotFoundDescriptor descriptor = checkNotFound(resultActions);
        assertEquals("Device", descriptor.getEntityName());
        assertEquals("ID", descriptor.getFieldName());
        assertEquals(id, Long.parseLong(descriptor.getValue()));

    }
}