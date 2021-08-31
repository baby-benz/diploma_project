package com.itmo.inspection.equipment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SensorWithIndication extends Sensor {
    private Double indication;

    public SensorWithIndication(Long id, String unit, SensorType sensorType, Double indication) {
        super(id, unit, sensorType);
        this.indication = indication;
    }
}
