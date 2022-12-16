package com.ninjaone.backendinterviewproject.service.reports.decorator;


import com.ninjaone.backendinterviewproject.service.reports.IReportService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class DecoratorConfig {


    @Value("${app.params.include-iva}")
    private boolean includeIva;
    @Autowired
    @Qualifier("reportDefaultService")
    private IReportService defaultService;

    @Autowired
    @Qualifier("reportIvaDecorator")
    private AbstractReportServiceDecorator ivaDecorator;

    @Bean("reportService")
    IReportService getReportService() {

        if (includeIva) {
            ivaDecorator.setDecorated(defaultService);
            return ivaDecorator;
        }

        return defaultService;
    }


}
