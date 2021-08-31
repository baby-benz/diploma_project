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
public class SensorDto {
    @Property
    private Long id;

    @Property
    private String unit;

    @Property(schema = {"enum", "ДАТЧИК_ДАВЛЕНИЯ,ДАТЧИК_ТЕМПЕРАТУРЫ,ДАТЧИК_ОБОРОТОВ"})
    private String sensorType;

    public SensorDto(@JsonProperty("id") Long id,
                     @JsonProperty("unit") String unit,
                     @JsonProperty("sensorType") SensorType sensorType) {
        this.id = id;
        this.unit = unit;
        this.sensorType = sensorType.toString();
    }
}
