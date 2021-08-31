package com.itmo.accounting.controller.dto;

import com.itmo.accounting.domain.entity.OrganizationType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
public class NewOrganizationDto implements Serializable {
    private String name;

    private OrganizationType organizationType;

    private String address;

    private Set<NewOrganizationEquipmentDto> equipment;
}
