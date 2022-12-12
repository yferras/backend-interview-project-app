package com.ninjaone.backendinterviewproject.service.converter.service;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@ConfigurationProperties(prefix = "app.service")
@Getter
public class ServiceSettings {
    private final List<String> applyDecorators = new ArrayList<>();
}
