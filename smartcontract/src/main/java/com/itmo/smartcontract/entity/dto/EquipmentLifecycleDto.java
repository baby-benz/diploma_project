package com.itmo.smartcontract.entity.dto;

import com.owlike.genson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@Getter
@Setter
@NoArgsConstructor
@DataType
public class EquipmentLifecycleDto {
    @Property
    private String manufactureDate;
    @Property
    private String commissioningDate;

    public EquipmentLifecycleDto(@JsonProperty("manufactureDate") String manufactureDate,
                                 @JsonProperty("commissioningDate") String commissioningDate) {
        this.manufactureDate = manufactureDate;
        this.commissioningDate = commissioningDate;
    }
}
