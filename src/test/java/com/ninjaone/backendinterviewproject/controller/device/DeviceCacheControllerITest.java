package com.ninjaone.backendinterviewproject.controller.device;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.backendinterviewproject.controller.AbstractControllerITest;
import com.ninjaone.backendinterviewproject.dto.ConfigServiceDeviceRelDto;
import com.ninjaone.backendinterviewproject.service.config.IConfigServiceDeviceRelService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.io.Serializable;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class DeviceCacheControllerITest extends AbstractControllerITest {

    @Autowired
    private IConfigServiceDeviceRelService configServiceDeviceRelService;

    @Autowired
    public DeviceCacheControllerITest(MockMvc mockMvc, ObjectMapper objectMapper) {
        super(mockMvc, objectMapper);
    }

    @ParameterizedTest(name = "(KEY[{1}]={0}) -> {2} \"{3}\"")
    @DisplayName("Cache. Get from cache.")
    @CsvSource(value = {
            // KEY, KEY_TYPE,
            "'Mac - 1','STR', 200, OK, 1,'Mac - 1'",
            "1,'NUM', 200, OK, 1,'Mac - 1'",
            "'Linux - 1','STR',404,NOT FOUND,,",
            "-10,'NUM',404,NOT FOUND,,",
            "'Mac - 1','STR', 200, OK, 1,'Mac - 1'",
            "1,'NUM', 200, OK, 1,'Mac - 1'",
    })
    void getFromCache(
            Serializable key,
            String keyType,
            int httpStatusCode,
            String message,
            Long resultId,
            String resultName
    ) throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get("/v1/device/cache/{value}", key)
        );

        if (httpStatusCode == 404) {
            resultActions.andExpect(status().isNotFound());
            return;
        }

        resultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.deviceId").value(resultId))
                .andExpect(jsonPath("$.deviceName").value(resultName))
                .andExpect(jsonPath("$.currentCost").isNumber())
        ;

    }

    @Test
    @DisplayName("Cache (on-demand) add service to device.")
    void getFromCacheOnDemandAdd() throws Exception {
        final String deviceName = "Windows - 2";
        final Long deviceId = 5L;
        testCacheAddService(deviceName, deviceName, deviceId);
    }

    @Test
    @DisplayName("Cache (preload) add service to device.")
    void getFromCachePreloadAdd() throws Exception {
        final String deviceName = "Windows - 2";
        final Long deviceId = 5L;
        testCacheAddService(deviceId, deviceName, deviceId);
    }

    @Test
    @DisplayName("Cache (on-demand) remove service from device.")
    void getFromCacheOnDemandRemove() throws Exception {
        final String deviceName = "Windows - 1";
        final Long deviceId = 4L;
        testCacheRemoveService(deviceName, deviceName, deviceId);
    }

    @Test
    @DisplayName("Cache (preload) remove service from device.")
    void getFromCachePreloadRemove() throws Exception {
        final String deviceName = "Windows - 1";
        final Long deviceId = 4L;
        testCacheRemoveService(deviceId, deviceName, deviceId);
    }

    private void testCacheAddService(Serializable key, String deviceName, Long deviceId) throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get("/v1/device/cache/{value}", key)
        );
        // Check the cost before creating the relationship.
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.deviceId").value(deviceId))
                .andExpect(jsonPath("$.deviceName").value(deviceName))
                .andExpect(jsonPath("$.currentCost").value(10.0))
        ;
        configServiceDeviceRelService.createRelationship(
                ConfigServiceDeviceRelDto.builder()
                        .service("Backup")
                        .device(deviceName)
                        .build()
        );
        resultActions = mockMvc.perform(
                get("/v1/device/cache/{value}", deviceName)
        );
        // Check the cost before creating the relationship.
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.deviceId").value(deviceId))
                .andExpect(jsonPath("$.deviceName").value(deviceName))
                .andExpect(jsonPath("$.currentCost").value(13.0))
        ;
        configServiceDeviceRelService.deleteRelationship(
                ConfigServiceDeviceRelDto.builder()
                        .service("Backup")
                        .device(deviceName)
                        .build()
        );
    }

    private void testCacheRemoveService(Serializable key, String deviceName, Long deviceId) throws Exception {
        ResultActions resultActions = mockMvc.perform(
                get("/v1/device/cache/{value}", key)
        );
        // Check the cost before creating the relationship.
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.deviceId").value(deviceId))
                .andExpect(jsonPath("$.deviceName").value(deviceName))
                .andExpect(jsonPath("$.currentCost").value(13.0))
        ;
        configServiceDeviceRelService.deleteRelationship(
                ConfigServiceDeviceRelDto.builder()
                        .service("Backup")
                        .device(deviceName)
                        .build()
        );
        resultActions = mockMvc.perform(
                get("/v1/device/cache/{value}", deviceName)
        );
        // Check the cost before creating the relationship.
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.deviceId").value(deviceId))
                .andExpect(jsonPath("$.deviceName").value(deviceName))
                .andExpect(jsonPath("$.currentCost").value(10.0))
        ;

        configServiceDeviceRelService.createRelationship(
                ConfigServiceDeviceRelDto.builder()
                        .service("Backup")
                        .device(deviceName)
                        .build()
        );
    }


}