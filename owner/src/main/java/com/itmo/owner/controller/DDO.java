package com.itmo.owner.controller;

import com.itmo.owner.domain.entity.EquipmentType;
import com.itmo.owner.domain.entity.Sensor;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalTime;
import java.util.Set;

@Getter
@Setter
public class DDO implements Serializable {
    private Long equipmentId;
    private String equipmentSerialNumber;
    private EquipmentType equipmentType;
    private Set<Sensor> sensors;
    private EquipmentLifecycle equipmentLifecycle;
    private String ownerOrg;
    private String operationOrg;
    private String description;
    private LocalTime timestamp;
}
