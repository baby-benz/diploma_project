package com.itmo.owner.service;

import com.itmo.owner.domain.entity.Equipment;
import com.itmo.owner.domain.entity.Sensor;
import com.itmo.owner.repository.EquipmentRepository;
import com.itmo.owner.service.mqtt.SensorEmulator;
import com.itmo.owner.service.mqtt.TopicReader;
import lombok.RequiredArgsConstructor;
import org.hyperledger.fabric.gateway.ContractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeoutException;

@Service
@RequiredArgsConstructor
public class MQTTManager {
    private final TopicReader topicReader;
    private final EquipmentRepository equipmentRepository;

    @Value("${mqtt.emulation}")
    private boolean toEmulateSensors;

    @Transactional
    @EventListener
    public void onApplicationLoaded(ContextRefreshedEvent event) throws ContractException, IOException, InterruptedException, TimeoutException {
        Thread.sleep(8000);

        List<Equipment> takenIntoAccountEquipment = equipmentRepository.findAllByIsTakenIntoAccountTrue();

        if (toEmulateSensors) {
            SensorEmulator.startEmulatingSensors(takenIntoAccountEquipment);
        }

        topicReader.startRetrievingData(takenIntoAccountEquipment);
    }

    public String subscribeOrUnsubscribe(Long equipmentId, boolean toSubscribe) throws NoSuchFieldException, ContractException, IOException, InterruptedException, TimeoutException {
        Equipment equipmentToSubscribe = equipmentRepository.findById(equipmentId).orElseThrow();

        if (toSubscribe) {
            if (toEmulateSensors) {
                //sensorEmulator.startEmulatingSensors(equipmentToSubscribe);
                SensorEmulator.startEmulatingSensors(equipmentToSubscribe);
            }

            topicReader.startRetrievingData(equipmentToSubscribe);

            return "Успешно оформлена подписка на датчики оборудования с ID " + equipmentId;
        } else {
            for (Sensor sensorToSubscribe : equipmentToSubscribe.getSensors()) {
                String topicName = sensorToSubscribe.getEquipment().getId() + "/" + sensorToSubscribe.getId();

                topicReader.stopRetrievingData(topicName);
            }

            if (toEmulateSensors) {
                //sensorEmulator.stopEmulatingSensors(equipmentToSubscribe);
                SensorEmulator.stopEmulatingSensors(equipmentToSubscribe);
            }

            return "Успешно прекращена подписка на датчики оборудования с ID " + equipmentId;
        }
    }
}
