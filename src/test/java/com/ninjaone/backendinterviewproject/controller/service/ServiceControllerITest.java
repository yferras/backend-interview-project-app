package com.ninjaone.backendinterviewproject.controller.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.backendinterviewproject.BackendInterviewProjectApplication;
import com.ninjaone.backendinterviewproject.dto.ServiceDto;
import com.ninjaone.backendinterviewproject.service.service.IServiceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.text.MessageFormat;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest(
        classes = {BackendInterviewProjectApplication.class}
)
@AutoConfigureMockMvc
class ServiceControllerITest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private IServiceService serviceService;

    @ParameterizedTest(name = "(NAME=\"{0}\",PRICE=\"{1}\") -> {2} \"{3}\"")
    @DisplayName("Add new service.")
    @CsvSource(value = {
            // NAME{0}, PRICE{1}, HTTP_STATUS_CODE{2}, MESSAGE{3}
            "'Service-6',10,201,''",      // 201 Created
            "'Service-7',0,201,''",       // 201 Created, price 0 must be valid.
            "'Backup',10,409,'Data duplication [Service].'", // 409 Conflict, The service Backup already exists.
            ",10,422,'name'", // name is null, validation error
            "'',10,422,'name'", // name is empty, validation error
            "'    ',10,422,'name'", // name is blank, validation error
            "'service-valid-name',-1.5,422,'price'", // valid name invalid price
            "'service-valid-name',,422,'price'" // valid name invalid price
    })
    void addNew(
            String name,
            Double price,
            int httpStatusCode,
            String message
    ) throws Exception {
        ServiceDto dto = ServiceDto.builder()
                .name(name)
                .price(price)
                .build();

        ResultActions resultActions = mockMvc.perform(
                post("/v1/service")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(dto))
        );

        if (httpStatusCode == 409) { // Some conflicts with the database
            resultActions
                    .andExpect(status().isConflict())
                    .andExpect(content().string(message));
            return;
        }

        if (httpStatusCode == 422) { // Validation
            resultActions
                    .andExpect(status().isUnprocessableEntity())
                    .andExpect(jsonPath(MessageFormat.format("$.{0}", message)).isArray());
            return;
        }

        // CREATED
        resultActions
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").isNumber())
                .andExpect(jsonPath("$.name").value(name))
                .andExpect(jsonPath("$.price").value(price));
    }

    @Test
    @DisplayName("Delete service by ID when the ID exists.")
    void deleteByIdOk() throws Exception {

        ServiceDto service = ServiceDto.builder()
                .name("service-to-delete")
                .price(10.0)
                .build();
        service = serviceService.addNew(service);


        mockMvc.perform(
                        delete("/v1/service/{id}", service.getId())
                )
                .andExpect(status().isOk());

        Optional<ServiceDto> optional = serviceService.getById(service.getId());

        assertTrue(optional.isEmpty());


    }

    @Test
    @DisplayName("Delete service by ID when the ID doesn't exists.")
    void deleteByIdNotFound() throws Exception {

        mockMvc.perform(
                        delete("/v1/service/{id}", -1000L)
                )
                .andExpect(status().isNotFound())
                .andExpect(content().string("E::Service(ID = -1000) Not found."));


    }
}