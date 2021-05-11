package com.itmo.accounting.controller.dto;

import com.itmo.accounting.domain.entity.SensorType;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CreatedSensorDto implements Serializable {
    private Long id;

    private Integer rangeMin;

    private Integer rangeMax;

    private String unit;

    private SensorType sensorType;

    private Long equipmentId;
}
