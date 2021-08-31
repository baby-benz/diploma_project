package com.itmo.owner.controller.dto.cc;

import com.itmo.owner.domain.entity.SensorType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Sensor {
    private Long id;
    private String unit;
    private SensorType sensorType;
    private Double indication;

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
