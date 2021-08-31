package com.itmo.smartcontract.entity.dto;

import com.owlike.genson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;
import com.itmo.smartcontract.entity.equipment.EquipmentType;

import java.util.Arrays;

@Getter
@Setter
@NoArgsConstructor
@DataType
public class EquipmentDto {
    @Property()
    private Long equipmentId;

    @Property()
    private String equipmentSerialNumber;

    @Property(schema = {"enum", "ЦЕНТРОБЕЖНЫЙ_НАСОС,ПОРШНЕВОЙ_НАСОС,КОМПРЕССОР,ВИНТОВОЙ_ЗАБОЙНЫЙ_ДВИГАТЕЛЬ,СЕПАРАТОР,УЧАСТОК_ТРУБОПРОВОДА,РВС"})
    private String equipmentType;

    @Property()
    private SensorDto[] sensors;

    @Property()
    private EquipmentLifecycleDto equipmentLifecycle;

    @Property()
    private RelatedOrganizationsDto relatedOrganizations;

    @Property()
    private String description;

    public EquipmentDto(@JsonProperty("equipmentID") Long equipmentId,
                        @JsonProperty("equipmentSerialNumber") String equipmentSerialNumber,
                        @JsonProperty("equipmentType") EquipmentType equipmentType,
                        @JsonProperty("sensors") SensorDto[] sensors,
                        @JsonProperty("equipmentLifecycle") EquipmentLifecycleDto equipmentLifecycle,
                        @JsonProperty("relatedOrganizations") RelatedOrganizationsDto relatedOrganizations,
                        @JsonProperty("description") String description) {
        this.equipmentId = equipmentId;
        this.equipmentSerialNumber = equipmentSerialNumber;
        this.equipmentType = equipmentType.toString();
        this.sensors = sensors;
        this.equipmentLifecycle = equipmentLifecycle;
        this.relatedOrganizations = relatedOrganizations;
        this.description = description;
    }

    @Override
    public String toString() {
        return "EquipmentDto{" +
                "equipmentId=" + equipmentId +
                ", equipmentSerialNumber='" + equipmentSerialNumber + '\'' +
                ", equipmentType='" + equipmentType + '\'' +
                ", sensors=" + Arrays.toString(sensors) +
                ", equipmentLifecycle=" + equipmentLifecycle +
                ", relatedOrganizations=" + relatedOrganizations +
                ", description='" + description + '\'' +
                '}';
    }
}
