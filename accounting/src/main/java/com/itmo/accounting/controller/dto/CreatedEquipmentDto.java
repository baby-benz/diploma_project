package com.itmo.accounting.controller.dto;

import com.itmo.accounting.domain.entity.EquipmentType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class CreatedEquipmentDto implements Serializable {
    private Long id;

    private String serialNumber;

    private EquipmentType equipmentType;

    private String description;

    private LocalDate manufactureDate;

    private LocalDate commissioningDate;

    private Set<CreatedSensorDto> sensors;

    private Boolean isTakenIntoAccount;

    private Set<NewEquipmentOrganizationDto> organizations;
}
