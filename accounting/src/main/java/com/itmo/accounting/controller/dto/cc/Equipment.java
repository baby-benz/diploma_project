package com.itmo.accounting.controller.dto.cc;

import com.itmo.accounting.domain.entity.EquipmentType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class Equipment implements Serializable {
    private Long equipmentId;

    private String equipmentSerialNumber;

    private EquipmentType equipmentType;

    private List<Sensor> sensors;

    private EquipmentLifecycle equipmentLifecycle;

    private RelatedOrganizations relatedOrganizations;

    private String description;

    public Equipment(Long equipmentId, String equipmentSerialNumber, EquipmentType equipmentType, List<Sensor> sensors,
                     EquipmentLifecycle equipmentLifecycle, RelatedOrganizations relatedOrganizations, String description) {
        this.equipmentId = equipmentId;
        this.equipmentSerialNumber = equipmentSerialNumber;
        this.equipmentType = equipmentType;
        this.sensors = sensors;
        this.equipmentLifecycle = equipmentLifecycle;
        this.relatedOrganizations = relatedOrganizations;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Equipment{" +
                "equipmentId=" + equipmentId +
                ", equipmentSerialNumber='" + equipmentSerialNumber + '\'' +
                ", equipmentType=" + equipmentType +
                ", sensors=" + sensors +
                ", equipmentLifecycle=" + equipmentLifecycle +
                ", relatedOrganizations=" + relatedOrganizations +
                ", description='" + description + '\'' +
                '}';
    }
}
