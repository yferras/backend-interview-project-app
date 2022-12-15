package com.ninjaone.backendinterviewproject.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ninjaone.backendinterviewproject.common.IBusinessDto;
import com.ninjaone.backendinterviewproject.common.IService;
import org.springframework.test.web.servlet.MockMvc;

import java.io.Serializable;


public abstract class AbstractEntityControllerITest<I extends Serializable, D extends IBusinessDto> extends AbstractControllerITest {

    protected final IService<I, D> service;

    public AbstractEntityControllerITest(MockMvc mockMvc, ObjectMapper objectMapper, IService<I, D> service) {
        super(mockMvc, objectMapper);
        this.service = service;
    }
}
