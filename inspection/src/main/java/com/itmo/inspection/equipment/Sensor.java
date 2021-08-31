package com.itmo.inspection.equipment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Sensor {
    private Long id;

    private String unit;

    private SensorType sensorType;
}
