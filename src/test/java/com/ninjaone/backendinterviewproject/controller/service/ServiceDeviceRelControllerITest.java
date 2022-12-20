package com.ninjaone.backendinterviewproject.controller.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.backendinterviewproject.controller.AbstractControllerITest;
import com.ninjaone.backendinterviewproject.dto.ConfigServiceDeviceRelDto;
import com.ninjaone.backendinterviewproject.service.service.IConfigServiceDeviceRelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ServiceDeviceRelControllerITest extends AbstractControllerITest {

    private final IConfigServiceDeviceRelService service;

    @Autowired
    public ServiceDeviceRelControllerITest(
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            IConfigServiceDeviceRelService service
    ) {
        super(mockMvc, objectMapper);
        this.service = service;
    }

    @ParameterizedTest(name = "(DEVICE[{2}]=\"{0}\",SERVICE[{3}]=\"{1}\") -> {4} \"{5}\"")
    @DisplayName("Add new relationship between service and device.")
    @CsvSource(value = {
            // device{0}, service{1}
            // Data validation for null
            ",,'*','*',422,'device,service'",       // device = null and service = null.
            "1,,'NUM','*',422,'service'",           // device is a NUM and service = null.
            ",1,'*','NUM',422,'device'",            // device = null and service is a NUM.
            "'device-1',,'STR','*',422,'service'",  // device is a STR and service = null.
            ",'service-1','*','STR',422,'device'",  // device = null and service is a STR.
            // Data validation for empty string
            "'','','STR','STR',422,'device,service'",   // device and service are empty.
            "1,'','NUM','STR',422,'service'",           // device is a NUM and service is empty.
            "'',1,'STR','NUM',422,'device'",            // device is empty and service a NUM.
            "'device-1','','STR','STR',422,'service'",  // device is not empty and service is empty.
            "'','service-1','STR','STR',422,'device'",  // device is empty and service is not empty.
            // Data validation for blank string
            "'   ','   ','STR','STR',422,'device,service'",// device and service are blank strings.
            "1,'   ','NUM','STR',422,'service'",           // device is a NUM and service is a blank string.
            "'   ',1,'STR','NUM',422,'device'",            // device is a blank string and service a NUM.
            "'device-1','   ','STR','STR',422,'service'",  // device is not empty and service is a blank string.
            "'   ','service-1','STR','STR',422,'device'",  // device is a blank string and service is not empty.
            // Non-existent combinations. 404 Not Found
            "'device-1','service-1','STR','STR',404,'NOT FOUND'",// Device(name = "device-1") -> doesn't exists, Service(name = "service-1") -> doesn't exists.
            "'Mac - 1','service-1','STR','STR',404,'NOT FOUND'", // Device(name = "Mac - 1") -> exists, Service(name = "service-1") -> doesn't exists.
            "'device-1','Default','STR','STR',404,'NOT FOUND'",  // Device(name = "device-1") -> doesn't exists, Service(name = "Default") -> exists.
            "-1,-1,'NUM','NUM',404,'NOT FOUND'",// Device(id = -1) -> doesn't exists, Service(id = -1) -> doesn't exists.
            "1,-1,'NUM','NUM',404,'NOT FOUND'", // Device(id = 1) -> exists., Service(id = -1) -> doesn't exists.
            "-1,1,'NUM','NUM',404,'NOT FOUND'", // Device(id = -1) -> doesn't exists, Service(id = 1) -> exists.

            // VALID
            "'Mac - 1','Antivirus for Mac','STR','STR',201,CREATED", // (deviceId = 2, serviceId = 2)
            "1,4,'NUM','NUM',201,CREATED",// (deviceName = linux, serviceName = Backup)
            // CONFLICT
            "'Mac - 1','Antivirus for Windows','STR','STR',409,'Service \"Antivirus for Windows\" cannot be set to device: \"Mac - 1\", because the service is not applicable to devices of type: \"mac\".'",
            "'Windows - 1','Antivirus for Mac','STR','STR',409,'Service \"Antivirus for Mac\" cannot be set to device: \"Windows - 1\", because the service is not applicable to devices of type: \"windows\".'",
    })
    void createRelationship(
            Serializable device,
            Serializable service,
            final String deviceType, // Only for info. NUM is for a number, STR is for a string and * is for both.
            final String serviceType,// Only for info. NUM is for a number, STR is for a string and * is for both.
            final int httpStatusCode,
            final String message
    ) throws Exception {

        if (device != null) {
            String str = device.toString();
            device = "STR".equals(deviceType) ? str : Long.parseLong(str);
        }
        if (service != null) {
            String str = service.toString();
            service = "STR".equals(serviceType) ? str : Long.parseLong(str);
        }
        ConfigServiceDeviceRelDto dto = ConfigServiceDeviceRelDto.builder()
                .device(device)
                .service(service)
                .build();

        ResultActions resultActions = mockMvc.perform(
                post("/v1/services/rels/device")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto))
        );

        switch (httpStatusCode) {
            case 409:  // Conflicts with the database
                checkIsConflict(message, resultActions);
                break;
            case 422:  // Validation
                String[] array = message.split(",");
                for (String fieldName : array) {
                    checkUnprocessableEntity(fieldName, resultActions);
                }
                break;
            case 404:  // Not Found
                checkNotFound(resultActions);
                break;
            default:
                // CREATED
                resultActions
                        .andExpect(status().isCreated())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.enabled").isBoolean())
                        .andExpect(jsonPath("$.enabled").value(true))
                ;
                break;
        }
    }

    @Test
    @DisplayName("Delete the relationship between service and device.")
    void deleteRelationship() throws Exception {

        ConfigServiceDeviceRelDto dto = ConfigServiceDeviceRelDto.builder()
                .device("Windows - 1")
                .service("Backup")
                .build();
        dto = service.createRelationship(dto);

        assertTrue(dto.getEnabled());

        ResultActions resultActions = mockMvc.perform(
                delete("/v1/services/rels/device")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto))
        );

        resultActions
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.enabled").isBoolean())
                .andExpect(jsonPath("$.enabled").value(false))
        ;

    }


}