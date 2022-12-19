package com.ninjaone.backendinterviewproject.service.reports.decorator;


import com.ninjaone.backendinterviewproject.service.reports.IReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class DecoratorConfig {


    @Value("${app.params.include-iva}")
    private boolean includeIva;


    @Bean("reportService")
    IReportService getReportService(
            @Qualifier("reportDefaultService") IReportService defaultService,
            @Qualifier("reportIvaDecorator") AbstractReportServiceDecorator ivaDecorator) {

        if (includeIva) {
            ivaDecorator.setDecorated(defaultService);
            return ivaDecorator;
        }

        return defaultService;
    }


}
