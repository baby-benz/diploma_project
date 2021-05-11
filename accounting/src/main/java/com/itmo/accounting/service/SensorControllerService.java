package com.itmo.accounting.service;

import com.itmo.accounting.controller.error.EmptyThresholdsException;
import com.itmo.accounting.domain.entity.Equipment;
import com.itmo.accounting.domain.entity.Sensor;
import com.itmo.accounting.domain.entity.SensorType;
import com.itmo.accounting.repository.EquipmentRepository;
import com.itmo.accounting.repository.SensorRepository;
import com.itmo.accounting.util.OwnerOrgRequestUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SensorControllerService {
    private final SensorRepository sensorRepository;
    private final EquipmentRepository equipmentRepository;

    public Sensor createSensor(Long equipmentId, Sensor sensor) {
        Equipment equipmentOfSensor = equipmentRepository.findById(equipmentId).orElseThrow();

        sensor.setEquipment(equipmentOfSensor);

        Sensor savedSensor = sensorRepository.save(sensor);

        if(equipmentOfSensor.getIsTakenIntoAccount()) {
            OwnerOrgRequestUtil.notifyOwnerAboutTerminationOfSubscription(savedSensor.getId(), true);
        }

        return savedSensor;
    }

    public Sensor fullyUpdateSensor(Long id, Sensor sensor) {
        Optional<Sensor> sensorToUpdate = sensorRepository.findById(id);
        if(sensorToUpdate.isPresent()) {
            sensor.setId(id);
            return sensorRepository.save(sensor);
        }
        return sensorRepository.save(sensor);
    }

    public void changeThreshold(Long sensorId, Optional<Integer> rangeMin, Optional<Integer> rangeMax) {
        rangeMin.ifPresentOrElse((min) -> rangeMax.ifPresentOrElse((max) -> {
            if (max > min) {
                Sensor sensorToChange = sensorRepository.findById(sensorId).orElseThrow();
                sensorToChange.setRangeMin(min);
                sensorToChange.setRangeMax(max);
                sensorRepository.save(sensorToChange);
            } else {
                throw new IllegalArgumentException("Значение нижней границы диапазона должно быть меньше верхней.");
            }
        }, () -> {
            Sensor sensorToChange = sensorRepository.findById(sensorId).orElseThrow();
            sensorToChange.setRangeMin(min);
            sensorRepository.save(sensorToChange);
        }), () -> rangeMax.ifPresentOrElse((max) -> {
            Sensor sensorToChange = sensorRepository.findById(sensorId).orElseThrow();
            sensorToChange.setRangeMax(max);
            sensorRepository.save(sensorToChange);
        }, () -> {
            throw new EmptyThresholdsException("Одновременно обе границы диапазона не могут быть пустыми.");
        }));
    }

    public Sensor updateSensorType(Long id, SensorType sensorType) {
        Sensor sensorToUpdate = sensorRepository.findById(id).orElseThrow();
        sensorToUpdate.setSensorType(sensorType);
        return sensorRepository.save(sensorToUpdate);
    }

    public void deleteSensor(Long id) {
        Sensor sensorToDelete = sensorRepository.findById(id).orElseThrow();
        sensorRepository.delete(sensorToDelete);
        OwnerOrgRequestUtil.notifyOwnerAboutTerminationOfSubscription(id, false);
    }
}