package com.ninjaone.backendinterviewproject.dto;

import com.ninjaone.backendinterviewproject.common.IBusinessDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * Class that represents the service DTO.
 */
@Getter
@AllArgsConstructor
@Builder
public class ServiceDto implements IBusinessDto {

    private Long id;

    private String name;

    private Double price;

}