package com.itmo.accounting.controller.dto;

import com.itmo.accounting.domain.entity.EquipmentType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

@Getter
@Setter
public class CreatedEquipmentDto implements Serializable {
    private String serialNumber;

    private EquipmentType equipmentType;

    private String description;

    private Date manufactureDate;

    private Date commissioningDate;

    private Set<CreatedSensorDto> sensors;

    private Boolean isTakenIntoAccount;

    private Set<EquipmentOrganizationForNewEquipmentDto> organizations;
}
