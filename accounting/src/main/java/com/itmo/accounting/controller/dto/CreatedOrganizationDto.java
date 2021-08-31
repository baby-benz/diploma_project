package com.itmo.accounting.controller.dto;

import com.itmo.accounting.domain.entity.OrganizationType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Set;

@Getter
@Setter
public class CreatedOrganizationDto implements Serializable {
    private Long id;

    private String name;

    private OrganizationType organizationType;

    private String address;

    private Set<NewOrganizationEquipmentDto> equipment;
}
