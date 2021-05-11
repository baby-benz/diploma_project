package com.itmo.smartcontract.equipment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Sensor {
    private Long id;

    private Integer rangeMin;

    private Integer rangeMax;

    private String unit;

    private SensorType sensorType;
}
