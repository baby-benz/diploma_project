package com.itmo.smartcontract.entity.equipment;

import com.owlike.genson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@Getter
@Setter
@DataType
@NoArgsConstructor
public class Sensor {
    @Property
    private Long id;
    @Property
    private String unit;
    @Property(schema = {"enum", "ДАТЧИК_ДАВЛЕНИЯ,ДАТЧИК_ТЕМПЕРАТУРЫ,ДАТЧИК_ОБОРОТОВ"})
    private String sensorType;
    @Property
    private Double indication;

    public SensorType getSensorType() {
        if (this.sensorType != null) {
            return SensorType.valueOf(this.sensorType);
        } else return null;
    }

    public void setEquipmentType(SensorType sensorType) {
        if (sensorType != null) {
            this.sensorType = sensorType.toString();
        } else {
            this.sensorType = null;
        }
    }

    public Sensor(@JsonProperty("id") Long id,
                  @JsonProperty("unit") String unit,
                  @JsonProperty("sensorType") SensorType sensorType,
                  @JsonProperty("indication") Double indication) {
        this.id = id;
        this.unit = unit;
        this.sensorType = sensorType.toString();
        this.indication = indication;
    }

    @Override
    public String toString() {
        return "Sensor{" +
                "id=" + id +
                ", unit='" + unit + '\'' +
                ", sensorType=" + sensorType +
                ", indication=" + indication +
                '}';
    }
}
