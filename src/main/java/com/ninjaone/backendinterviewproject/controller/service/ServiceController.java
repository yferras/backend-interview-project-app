package com.ninjaone.backendinterviewproject.controller.service;

import com.ninjaone.backendinterviewproject.common.exception.BusinessException;
import com.ninjaone.backendinterviewproject.dto.ServiceDto;
import com.ninjaone.backendinterviewproject.service.service.IServiceService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/service")
@AllArgsConstructor
public class ServiceController {

    private IServiceService serviceService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ServiceDto addNew(@RequestBody ServiceDto service) throws BusinessException {
        return serviceService.addNew(service);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void delete(@NonNull @PathVariable("id") Long id) throws BusinessException {
        serviceService.deleteById(id);
    }


}
