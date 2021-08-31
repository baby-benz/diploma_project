package com.itmo.accounting.controller;

import com.itmo.accounting.controller.dto.CreatedSensorDto;
import com.itmo.accounting.controller.dto.NewSensorDto;
import com.itmo.accounting.domain.entity.Sensor;
import com.itmo.accounting.domain.entity.SensorType;
import com.itmo.accounting.service.SensorControllerService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequestMapping("/accounting/api/sensor")
@RestController
@RequiredArgsConstructor
public class SensorController {
    private final SensorControllerService sensorControllerService;
    private final ModelMapper modelMapper;

    @PostMapping("/{equipmentId}")
    public CreatedSensorDto createSensor(@PathVariable Long equipmentId, @RequestBody NewSensorDto sensor) {
        return modelMapper.map(sensorControllerService.createSensor(equipmentId, modelMapper.map(sensor, Sensor.class)), CreatedSensorDto.class);
    }

    @PutMapping("/{id}")
    public CreatedSensorDto fullyUpdateSensor(@PathVariable Long id, @RequestBody NewSensorDto sensor) {
        return modelMapper.map(sensorControllerService.fullyUpdateSensor(id, modelMapper.map(sensor, Sensor.class)), CreatedSensorDto.class);
    }

    @PatchMapping("/sensor-type/{id}")
    public CreatedSensorDto updateSensorType(@PathVariable Long id, @RequestParam SensorType sensorType) {
        return modelMapper.map(sensorControllerService.updateSensorType(id, sensorType), CreatedSensorDto.class);
    }

    @PatchMapping("/range/{id}")
    public CreatedSensorDto updateThreshold(@PathVariable Long id, @RequestParam Optional<Integer> rangeMin, @RequestParam Optional<Integer> rangeMax) {
        return modelMapper.map(sensorControllerService.updateThreshold(id, rangeMin, rangeMax), CreatedSensorDto.class);
    }

    @DeleteMapping("/{id}")
    public void deleteSensor(@PathVariable Long id) {
        sensorControllerService.deleteSensor(id);
    }
}
