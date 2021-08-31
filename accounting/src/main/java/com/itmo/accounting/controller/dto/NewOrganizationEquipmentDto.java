package com.itmo.accounting.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class NewOrganizationEquipmentDto implements Serializable {
    private Long equipmentId;

    private LocalDate startDate;

    private LocalDate endDate;
}
