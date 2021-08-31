package com.itmo.smartcontract.entity.equipment;

import com.owlike.genson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType
@NoArgsConstructor
public class EquipmentLifecycle {
    @Property
    @Getter
    @Setter
    private String manufactureDate;
    @Property
    @Getter
    @Setter
    private String commissioningDate;
    @Property
    private String EOLDate;

    @JsonProperty("EOLDate")
    public String getEOLDate() {
        return EOLDate;
    }

    @JsonProperty("EOLDate")
    public void setEOLDate(String EOLDate) {
        this.EOLDate = EOLDate;
    }

    public EquipmentLifecycle(@JsonProperty("manufactureDate") String manufactureDate,
                              @JsonProperty("commissioningDate") String commissioningDate,
                              @JsonProperty("EOLDate") String EOLDate) {
        this.manufactureDate = manufactureDate;
        this.commissioningDate = commissioningDate;
        this.EOLDate = EOLDate;
    }

    @Override
    public String toString() {
        return "EquipmentLifecycle{" +
                "manufactureDate='" + manufactureDate + '\'' +
                ", commissioningDate='" + commissioningDate + '\'' +
                ", EOLDate='" + EOLDate + '\'' +
                '}';
    }
}
