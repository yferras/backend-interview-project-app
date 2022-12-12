package com.ninjaone.backendinterviewproject.service.converter.service;

import com.ninjaone.backendinterviewproject.common.converter.ConverterLogDecorator;
import com.ninjaone.backendinterviewproject.dto.ServiceDto;
import com.ninjaone.backendinterviewproject.model.Service;
import org.springframework.stereotype.Component;

/**
 * Decorator to log the conversion from the entity {@link Service} to DTO {@link ServiceDto} and vice-versa.
 */

@Component
public final class ServiceConverterLogger extends ConverterLogDecorator<Service, ServiceDto> {

}
