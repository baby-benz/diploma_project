package com.itmo.accounting.controller.dto.cc;

import com.itmo.accounting.domain.entity.SensorType;
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
