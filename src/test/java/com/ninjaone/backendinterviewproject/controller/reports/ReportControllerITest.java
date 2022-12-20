package com.ninjaone.backendinterviewproject.controller.reports;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.backendinterviewproject.common.exception.BusinessException;
import com.ninjaone.backendinterviewproject.controller.AbstractControllerITest;
import com.ninjaone.backendinterviewproject.dto.ConfigServiceDeviceRelDto;
import com.ninjaone.backendinterviewproject.dto.DeviceDto;
import com.ninjaone.backendinterviewproject.dto.DeviceTypeDto;
import com.ninjaone.backendinterviewproject.service.service.IConfigServiceDeviceRelService;
import com.ninjaone.backendinterviewproject.service.device.IDeviceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class ReportControllerITest extends AbstractControllerITest {

    public static final String ANTIVIRUS_FOR_MAC = "Antivirus for Mac";
    public static final String ANTIVIRUS_FOR_WINDOWS = "Antivirus for Windows";
    public static final String BACKUP = "Backup";
    public static final String SCREEN_SHARE = "Screen Share";
    public static final String MAC_1 = "M-1";
    public static final String MAC_2 = "M-2";
    public static final String MAC_3 = "M-3";
    public static final String WIN_1 = "W-1";
    public static final String WIN_2 = "W-2";
    public static final long CUSTOMER_ID = 100L;
    public static final String DEV_TYP_MAC = "mac";
    public static final String DEV_TYP_WINDOWS = "windows";
    private final IConfigServiceDeviceRelService configServiceDeviceRelService;
    private final IDeviceService deviceService;

    @Value("${app.params.iva}")
    private double iva;
    @Value("${app.params.include-iva}")
    private boolean includeIva;

    @Autowired
    public ReportControllerITest(MockMvc mockMvc, ObjectMapper objectMapper, IConfigServiceDeviceRelService service, IDeviceService deviceService) {
        super(mockMvc, objectMapper);
        this.configServiceDeviceRelService = service;
        this.deviceService = deviceService;
    }

    @Test
    @DisplayName("Report: Total cost per user.")
    void calcTotalCost() throws Exception {

        createDevices();
        double expectedCostBeforeConfig = 20.0;
        expectedCostBeforeConfig *= includeIva ? (1.0 + iva) : 1.0;
        mockMvc.perform(
                        get("/v1/reports/total-per-customer/{id}", CUSTOMER_ID)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.value").exists())
                .andExpect(jsonPath("$.value").isNumber())
                .andExpect(jsonPath("$.value").value(expectedCostBeforeConfig))
        ;

        configServicesInDevices();
        double expectedCostAfterConfig = 64.0;
        expectedCostAfterConfig *= includeIva ? (1.0 + iva) : 1.0;
        mockMvc.perform(
                        get("/v1/reports/total-per-customer/{id}", CUSTOMER_ID)
                )
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.value").exists())
                .andExpect(jsonPath("$.value").isNumber())
                .andExpect(jsonPath("$.value").value(expectedCostAfterConfig))
        ;

    }

    private void configServicesInDevices() {
        configServiceDeviceRelService.createRelationship(
                ConfigServiceDeviceRelDto.builder()
                        .device(MAC_1)
                        .service(ANTIVIRUS_FOR_MAC)
                        .build()
        );
        configServiceDeviceRelService.createRelationship(
                ConfigServiceDeviceRelDto.builder()
                        .device(MAC_1)
                        .service(BACKUP)
                        .build()
        );
        configServiceDeviceRelService.createRelationship(
                ConfigServiceDeviceRelDto.builder()
                        .device(MAC_1)
                        .service(SCREEN_SHARE)
                        .build()
        );

        configServiceDeviceRelService.createRelationship(
                ConfigServiceDeviceRelDto.builder()
                        .device(MAC_2)
                        .service(ANTIVIRUS_FOR_MAC)
                        .build()
        );
        configServiceDeviceRelService.createRelationship(
                ConfigServiceDeviceRelDto.builder()
                        .device(MAC_2)
                        .service(BACKUP)
                        .build()
        );

        configServiceDeviceRelService.createRelationship(
                ConfigServiceDeviceRelDto.builder()
                        .device(MAC_3)
                        .service(ANTIVIRUS_FOR_MAC)
                        .build()
        );
        configServiceDeviceRelService.createRelationship(
                ConfigServiceDeviceRelDto.builder()
                        .device(MAC_3)
                        .service(SCREEN_SHARE)
                        .build()
        );

        configServiceDeviceRelService.createRelationship(
                ConfigServiceDeviceRelDto.builder()
                        .device(WIN_1)
                        .service(ANTIVIRUS_FOR_WINDOWS)
                        .build()
        );
        configServiceDeviceRelService.createRelationship(
                ConfigServiceDeviceRelDto.builder()
                        .device(WIN_1)
                        .service(BACKUP)
                        .build()
        );
        configServiceDeviceRelService.createRelationship(
                ConfigServiceDeviceRelDto.builder()
                        .device(WIN_1)
                        .service(SCREEN_SHARE)
                        .build()
        );


        configServiceDeviceRelService.createRelationship(
                ConfigServiceDeviceRelDto.builder()
                        .device(WIN_2)
                        .service(ANTIVIRUS_FOR_WINDOWS)
                        .build()
        );
        configServiceDeviceRelService.createRelationship(
                ConfigServiceDeviceRelDto.builder()
                        .device(WIN_2)
                        .service(SCREEN_SHARE)
                        .build()
        );
    }

    private void createDevices() throws BusinessException {
        deviceService.addNew(
                DeviceDto.builder()
                        .name(MAC_1)
                        .customerId(CUSTOMER_ID)
                        .deviceType(DeviceTypeDto.builder().name(DEV_TYP_MAC).build())
                        .build()
        );
        deviceService.addNew(
                DeviceDto.builder()
                        .name(MAC_2)
                        .customerId(CUSTOMER_ID)
                        .deviceType(DeviceTypeDto.builder().name(DEV_TYP_MAC).build())
                        .build()
        );
        deviceService.addNew(
                DeviceDto.builder()
                        .name(MAC_3)
                        .customerId(CUSTOMER_ID)
                        .deviceType(DeviceTypeDto.builder().name(DEV_TYP_MAC).build())
                        .build()
        );
        deviceService.addNew(
                DeviceDto.builder()
                        .name(WIN_1)
                        .customerId(CUSTOMER_ID)
                        .deviceType(DeviceTypeDto.builder().name(DEV_TYP_WINDOWS).build())
                        .build()
        );
        deviceService.addNew(
                DeviceDto.builder()
                        .name(WIN_2)
                        .customerId(CUSTOMER_ID)
                        .deviceType(DeviceTypeDto.builder().name(DEV_TYP_WINDOWS).build())
                        .build()
        );
    }

    @Test
    @DisplayName("Report: Total cost per user. When the user doesn't exist.")
    void calcTotalCostUserNotFound() throws Exception {

        ResultActions resultActions = mockMvc.perform(
                get("/v1/reports/total-per-customer/{id}", -1)
        );

        resultActions
                .andExpect(status().isNotFound());


    }
}