package com.ninjaone.backendinterviewproject.controller.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.backendinterviewproject.controller.AbstractControllerITest;
import com.ninjaone.backendinterviewproject.dto.ServiceDto;
import com.ninjaone.backendinterviewproject.service.service.IServiceService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


class ServiceControllerITest extends AbstractControllerITest<Long, ServiceDto> {

    @Autowired
    public ServiceControllerITest(
            MockMvc mockMvc,
            ObjectMapper objectMapper,
            IServiceService service) {
        super(mockMvc, objectMapper, service);
    }

    @ParameterizedTest(name = "(NAME=\"{0}\",PRICE=\"{1}\") -> {2} \"{3}\"")
    @DisplayName("Add new service.")
    @CsvSource(value = {
            // NAME{0}, PRICE{1}, HTTP_STATUS_CODE{2}, MESSAGE{3}
            // OK
            "'Service-6',10,201,'CREATED'",      // 201 Created.
            "'Service-7',0,201,'CREATED'",       // 201 Created, price 0 must be valid.
            // Data duplication
            "'Service-6',10,409,'Data duplication [Service].'", // 409 Conflict, The service 'Service-6' already exists.
            // Data validation
            ",10,422,'name'", // name is null, validation error.
            "'',10,422,'name'", // name is empty, validation error.
            "'    ',10,422,'name'", // name is blank, validation error.
            "'service-valid-name',-1.5,422,'price'", // valid name; invalid price = -1.5, less than 0.00.
            "'service-valid-name',,422,'price'" // valid name; invalid price = null.
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

        switch (httpStatusCode) {
            case 409:  // Conflicts with the database
                checkIsConflict(message, resultActions);
                break;
            case 422:  // Validation
                checkUnprocessableEntity(message, resultActions);
                break;
            default:
                // CREATED
                resultActions
                        .andExpect(status().isCreated())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(jsonPath("$.id").isNumber())
                        .andExpect(jsonPath("$.name").value(name))
                        .andExpect(jsonPath("$.price").value(price));
                break;
        }
    }

    @Test
    @DisplayName("Delete service by ID when the ID exists.")
    void deleteByIdOk() throws Exception {

        ServiceDto dto = ServiceDto.builder()
                .name("service-to-delete")
                .price(10.0)
                .build();
        dto = service.addNew(dto);

        mockMvc.perform(
                        delete("/v1/service/{id}", dto.getId())
                )
                .andExpect(status().isOk());

        Optional<ServiceDto> optional = service.getById(dto.getId());

        assertTrue(optional.isEmpty());


    }

    @Test
    @DisplayName("Delete service by ID when the ID doesn't exists.")
    void deleteByIdNotFound() throws Exception {

        final Long id = -1000L;
        ResultActions resultActions = mockMvc.perform(
                        delete("/v1/service/{id}", id)
                );
        MessageNotFoundDescriptor descriptor = checkNotFound(resultActions);
        assertEquals("Service", descriptor.getEntityName());
        assertEquals("ID", descriptor.getFieldName());
        assertEquals(id, Long.parseLong(descriptor.getValue()));

    }
}