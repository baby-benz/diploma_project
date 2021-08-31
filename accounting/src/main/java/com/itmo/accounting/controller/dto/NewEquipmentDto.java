package com.itmo.accounting.controller.dto;

import com.itmo.accounting.domain.entity.EquipmentType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class NewEquipmentDto implements Serializable {
    private String serialNumber;

    private EquipmentType equipmentType;

    private String description;

    private LocalDate manufactureDate;

    private LocalDate commissioningDate;

    private Set<NewSensorDto> sensors;

    private Boolean isTakenIntoAccount;

    private Set<NewEquipmentOrganizationDto> organizations;
}
