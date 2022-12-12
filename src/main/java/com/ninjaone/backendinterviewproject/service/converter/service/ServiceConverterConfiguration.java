package com.ninjaone.backendinterviewproject.service.converter.service;

import com.ninjaone.backendinterviewproject.common.converter.AbstractConverterDecorator;
import com.ninjaone.backendinterviewproject.common.converter.IConverter;
import com.ninjaone.backendinterviewproject.dto.ServiceDto;
import com.ninjaone.backendinterviewproject.model.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@Slf4j
public class ServiceConverterConfiguration {

    @Autowired
    private ServiceSettings settings;


    @Bean("serviceConverter")
    public IConverter<Service, ServiceDto> getServiceConverter(@NonNull List<AbstractConverterDecorator<Service, ServiceDto>> decorators) {
        ServiceConverter converter = new ServiceConverter();

        if (decorators.isEmpty()) {
            return converter;
        }

        if (decorators.size() == 1) {
            decorators.get(0).setConverter(converter);
            return decorators.get(0);
        }

        if (settings.getApplyDecorators().isEmpty()) {
            return converter;
        }

        Map<String, AbstractConverterDecorator<Service, ServiceDto>> map =
                decorators.stream()
                        .collect(Collectors.toMap(AbstractConverterDecorator::getName, v -> v));

        AbstractConverterDecorator<Service, ServiceDto> chain = map.get(settings.getApplyDecorators().get(0));
        chain.setConverter(converter);

        List<String> decoratorNameList = settings.getApplyDecorators();
        for (int i = 1; i < decoratorNameList.size(); i++) {
            String name = decoratorNameList.get(i);
            AbstractConverterDecorator<Service, ServiceDto> next = map.get(name);
            next.setConverter(chain);
            chain = next;
        }

        return chain;
    }

}
