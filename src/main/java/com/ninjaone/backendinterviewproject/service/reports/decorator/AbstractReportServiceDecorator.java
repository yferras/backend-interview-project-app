package com.ninjaone.backendinterviewproject.service.reports.decorator;

import com.ninjaone.backendinterviewproject.service.reports.IReportService;
import lombok.Getter;
import lombok.Setter;

abstract class AbstractReportServiceDecorator implements IReportService {

    @Setter
    protected IReportService decorated;

    @Getter
    private final String name;

    AbstractReportServiceDecorator(String name) {
        this.name = name;
    }
}
