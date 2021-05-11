package com.itmo.accounting.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class EquipmentOrganizationIdDto implements Serializable {
    private Long equipmentId;
}
