package com.itmo.accounting.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class EquipmentOrganizationForNewEquipmentDto implements Serializable {
    private Long organizationId;

    private Date startDate;

    private Date endDate;
}
