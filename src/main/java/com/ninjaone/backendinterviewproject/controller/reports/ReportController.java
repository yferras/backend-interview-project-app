package com.ninjaone.backendinterviewproject.controller.reports;

import com.ninjaone.backendinterviewproject.dto.reports.TotalCostPerUser;
import com.ninjaone.backendinterviewproject.service.reports.IReportService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/reports")
@AllArgsConstructor
public class ReportController {

    private IReportService reportService;

    @GetMapping("/customers/{id}/totals")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public TotalCostPerUser calcTotalCost(@NonNull @PathVariable("id") Long id) {
        return reportService.calcTotalCost(id);
    }

}
